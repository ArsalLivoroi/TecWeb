package generatore.problema;


import generatore.global.Classe;
public class ClasseProblema extends Classe {
	
//	private Set<Riferimento<ClasseProblema>> riferimenti;

	public ClasseProblema(String nome, String nomePlurale) {
		super(nome, nomePlurale);
		if(nome.equals(nomePlurale)&&!nome.contains("Mapping")) {
			System.err.println("Attenzione!!!");
			System.err.print("nome e nomePlurale sono uguali: ");
			System.out.println(nome);
		}
//		this.riferimenti = new LinkedHashSet<Riferimento<ClasseProblema>>();
		// TODO Auto-generated constructor stub
	}

	
//	@Override
//	public void addRiferimento(Riferimento<?> riferimento) {
//		riferimenti.add((Riferimento<ClasseProblema>) riferimento);
//	}
	
}
