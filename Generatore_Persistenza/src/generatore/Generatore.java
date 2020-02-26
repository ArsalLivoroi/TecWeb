package generatore;

import java.util.*;

import generatore.dao.GeneratoreDAO;
import generatore.hibernare.GeneratoreHIBERNATE;
import generatore.jdbc.GeneratoreJDBC;
import generatore.problema.Attributo;
import generatore.problema.ClasseProblema;
import generatore.problema.Problema;
import generatore.problema.Riferimento;
import generatore.problema.Unique;


public class Generatore {

	private static Set<ClasseProblema> classi;
	public static Problema problema;
	

	public final static String INT ="int";
	public final static String STRING ="String";
	public final static String DATE ="Date";
	public final static String TIME ="Time";
	public final static String LONG ="long";
	public final static String FLOAT ="float";
	public final static String DOUBLE ="double";

	public static final String DAO = "dao";
	public static final String HIBERNATE = "hibernate";
	public static final String JDBC = "jdbc";
	
	public final static String LAZY_LOAD ="lazy-load";
	public final static String EAGER ="eager";
		
	public static final String ONE_TO_ONE = "11";
	public static final String ONE_TO_MANY = "1n";
	public static final String MANY_TO_ONE = "n1";
	public static final String MANY_TO_MANY = "nm";

	private static Problema inizializzaProblema() {

		//--1.Tecnologia richiesta  (JDBC | DAO | HIBERNATE)
		problema = new Problema(DAO); 

		//--2.Classi UML	new ClasseProblema("nome classe", "nome plurale");
		ClasseProblema c1 = new ClasseProblema("Progetto", "Progetti");
		ClasseProblema c2 = new ClasseProblema("Work Package", "Work Packages");
		ClasseProblema c3 = new ClasseProblema("Partner", "Partners");
		
		//--3.aggiungi le classi UML al problema
		problema.addClasseProblema(c1);
		problema.addClasseProblema(c2);
		problema.addClasseProblema(c3);
		
		//--4.per ogni relazione tra 2 classi, creare 2 riferimenti (una per ogni direzione di lettura)
		//(crea in automatico la classe mapping se necessario)
		//Riferimento(from, to, tipoRelazione, tipoFetch, navigabile?)
		//se tipoFetch == null, ma è navigabile, viene scelto il tipo di caricamento più appropriato
		Riferimento<ClasseProblema> c1_c2 = new Riferimento<ClasseProblema>(c1, c2, ONE_TO_MANY, LAZY_LOAD, true);
		Riferimento<ClasseProblema> c2_c1 = new Riferimento<ClasseProblema>(c2, c1, MANY_TO_ONE, LAZY_LOAD, true);
		Riferimento<ClasseProblema> c2_c3 = new Riferimento<ClasseProblema>(c2, c3, MANY_TO_MANY, LAZY_LOAD, true);
		Riferimento<ClasseProblema> c3_c2 = new Riferimento<ClasseProblema>(c3, c2, MANY_TO_MANY, LAZY_LOAD, true);

		//--5.PrimatyKey Se specificato, altrimenti viene aggiunto in automatico l'ID surrogato
		//c1.addPrimaryKey(new Attributo(STRING, "targa"));	
		//c2.addPrimaryKey(new Attributo(INT, "id"));		
		//c3.addPrimaryKey(new Attributo(INT, "id"));

		//--6.UNIQUE (gli atributi sottolineati), possono esserci più attributi per unique (mai capitato)
		Unique uC1 = new Unique();
		uC1.addAttributo(new Attributo(STRING, "Codice Progetto"));
//		uC1.addAttributo(new Attributo(INT, "idon"));  //SE PIU DI UNO
		c1.addUnique(uC1);

		Unique uC2 = (new Unique());
		uC2.addAttributo(new Attributo(STRING, "Nome WP"));
		c2.addUnique(uC2);
		
		Unique uC3 = (new Unique());
		uC3.addAttributo(new Attributo(STRING, "Sigla Partner"));
		c3.addUnique(uC3);
		
		
		//--7.Attributi, tutto ciò che non è sottolineato
		c1.addAttributo(new Attributo(STRING, "Nome Progetto"));
		c1.addAttributo(new Attributo(INT, "Anno Inizio"));
		c1.addAttributo(new Attributo(INT, "Durata"));
		
		c2.addAttributo(new Attributo(STRING, "Titolo"));
		c2.addAttributo(new Attributo(STRING, "Descrizione"));
		//c2.addAttributo(new Attributo(STRING, "descrizione Tipo Acc"));
		
		c3.addAttributo(new Attributo(STRING, "nome"));
		//c3.addAttributo(new Attributo(STRING, "citta"));
		//c3.addAttributo(new Attributo(STRING, "indirizzo"));

		return problema;	
	}

	public static void main(String[] args) throws Exception {

			Problema problema = inizializzaProblema();
			problema.check();
			classi = problema.getClassi();
			show();
			
			switch(problema.getTecnologia()) {
			case DAO: (new GeneratoreDAO(classi)).generate();
			break;
			case JDBC: (new GeneratoreJDBC(classi)).generate();
			break;
			case HIBERNATE: (new GeneratoreHIBERNATE(classi)).generate();
			break;
			default :
				System.out.println("nome tecnologia errato!");
			}
	}


	private static void show() {
		//System.out.println();
		System.out.println(problema.getTecnologia());
		for(ClasseProblema c : classi)
			System.out.println(c);
		
	}
}
