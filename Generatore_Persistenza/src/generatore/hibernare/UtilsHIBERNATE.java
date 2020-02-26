package generatore.hibernare;

import java.util.LinkedHashSet;
import java.util.Set;

import generatore.Generatore;
import generatore.problema.Attributo;
import generatore.problema.MappingMetodoQuery;
import generatore.problema.MetodoFind;

public class UtilsHIBERNATE {
	
	private static void MMQinizializato(MappingMetodoQuery mmq) {
		//MappingMetodoQuery mmq = new MappingMetodoQuery();

//		mmq.m1.add("");
//		mmq.m2.add("");
//		mmq.m3.add("connection = this.dataSource.getConnection();");
//		mmq.m4a.add("");
//		mmq.m4b.add("");
//		mmq.m4c.add("");
//		mmq.m4d.add("");
//		mmq.m4e.add("");
//		mmq.m4f.add("");
//		mmq.m5.add("throw new PersistenceException(e.getMessage());");
//		mmq.m6.add("DB2DAOFactory.closeConnection(conn);");
//		mmq.m7.add("");
		//return mmq;
	}
	
	
//	private static String generaCheck(Set<Attributo> list) {
//		String result="";
//		if(!list.isEmpty()) {
//			for(Attributo a: list)
//				result+=getCheck(a) + " || " ;
//			result = result.substring(0, result.length() - 4);
//		}
//		return result;
//	}
//
//	private static String getCheck(Manager c) {	
//		return getCheck(new Attributo(c.getNomeBean(), c.getNomeOggetto()));
//	}
	
//	private static String getCheck(Attributo a) {
//		String result;
//		switch (a.getTipo().toLowerCase()) {
//		case "int"  : result = a.getNome() +" < 0"; break;
//		case "long"  : result = a.getNome() +" < 0"; break;
//		case "float"  : result = a.getNome() +" < 0"; break;
//		case "double"  : result = a.getNome() +" < 0"; break;
//		case "string" : result = a.getNome() + " == null || "+ a.getNome()+".isEmpty() "; break;
//		default: result = a.getNome() + " == null" ;
//		}
//		return result;
//	}

	public static MappingMetodoQuery inizializzaMMQCreate(Manager manager) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo("createTable");
		mmq.setTipoRitorno("boolean");

		mmq.byAttributi = "";
	
		mmq.m1.add("boolean result = false;");
		mmq.m4a.add("Statement statement = connection.createStatement();");
	
		mmq.m4c.add("statement.executeUpdate(create);");
		mmq.m4f.add("result = true;");
		mmq.m4f.add("statement.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQDrop(Manager manager) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo("drop");
		mmq.setTipoRitorno("boolean");

		mmq.byAttributi = "";
	
		mmq.m1.add("boolean result = false;");
		mmq.m4a.add("Statement statement = connection.createStatement();");
	
		mmq.m4c.add("statement.executeUpdate(drop);");
		mmq.m4f.add("result = true;");
		mmq.m4f.add("statement.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQUpdate(Manager manager,String metodo) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(metodo);
		mmq.setTipoRitorno("boolean");

		Attributo a = (new Attributo(manager.getNomeBean(), manager.getNomeOggetto()));

		mmq.byAttributi = a.getTipo() + " " + a.getNome();
	
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Session session = factory.openSession();");
		mmq.getM1().add("Transaction tx = null;");

		mmq.m4a.add("tx = session.beginTransaction();");

		String name = manager.getNomeOggetto();
		
		
		
		mmq.m4c.add("session."+metodo+"("+name+");");
		mmq.m4e.add("tx.commit();");
		mmq.m4f.add("result = true;");
		mmq.m5.add("if (tx!=null)");
		mmq.m5.add("	tx.rollback();");
		mmq.m6.add("session.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQInsert(Manager manager) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo("insert");
		mmq.setTipoRitorno("boolean");

		Attributo a = (new Attributo(manager.getNomeBean(), manager.getNomeOggetto()));

		mmq.byAttributi = a.getTipo() + " " + a.getNome();
	
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Session session = factory.openSession();");
		mmq.getM1().add("Transaction tx = null;");

		mmq.m4a.add("tx = session.beginTransaction();");

		String name = manager.getNomeOggetto();
		
		
		
		mmq.m4c.add("session.save("+name+");");
		mmq.m4e.add("tx.commit();");
		mmq.m4f.add("result = true;");
		mmq.m5.add("if (tx!=null)");
		mmq.m5.add("	tx.rollback();");
		mmq.m6.add("session.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQFind(Manager manager, MetodoFind<Manager> mf, String nomeQuery,
			String nomeMetodoQuery, LinkedHashSet<Attributo> attributi) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(nomeQuery);
		mmq.setTipoRitorno("Set<"+mf.getClasseTarget().getNomeBean()+">");


		String atribs = "";
		for(Attributo a: attributi)
			atribs += a.getTipo() + " " + a.getNome() + ", ";
		atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
	
		mmq.m1.add("Set<"+mf.getClasseTarget().getNomeBean()+"> result = new HashSet<"+mf.getClasseTarget().getNomeBean()+">();");
		mmq.m1.add("Session session = factory.openSession();");
		mmq.m4b.add("Query query= session.createSQLQuery("+nomeQuery+");");
		int index = 0;
		for(Attributo atr : attributi)
		{
			String tmp;
			if(!atr.getTipo().equals(Generatore.DATE)) { //TODO da adattare per hibernate
				tmp = atr.getNome();
			}else {
				mmq.m4b.add(dateJavaToSQL(atr));
				tmp="dataSQL";
			}
			mmq.m4b.add("query.set"+getMapingType(atr)+"("+index+","+tmp+");");
			index++;
		}
		mmq.m4e.add("result=(HashSet<"+mf.getClasseTarget().getNomeBean()+">) query.list();");
	;
		mmq.m6.add("session.close();");
		mmq.m7.add("return result;");
		return mmq;
	}
	
	private static String dateJavaToSQL(Attributo atr) {//TODO su hhibernate è diverso
		String result="";
		result+=	"	long sec = "+ atr.getNome() + ".getTime();\n";
		result+=	"	java.sql.Date dataSQL = new java.sql.Date(sec);\n";
		return result;
	}
	
	private static String dateSQLtoJAVA(Attributo atr) {
		String result="\n";
		result+="\t\t\t\tlong secs = rs.get"+atr.getTipo()+"(\""+ atr.getNome() + "\").getTime();\n";
		result+="\t\t\t\tjava.util.Date dataJAVA = new java.util.Date(secs);";
		return result;
	}
	
	
	private static String getMapingType(Attributo a) {
		String result;
		switch (a.getTipo().toLowerCase()) {//TODO da completare
		case "int"  : result = "Integer"; break;
		case "long"  : result = "Long"; break;
		case "float"  : result ="Float"; break;
		case "double"  : result ="Double"; break;
		case "string" : result = "String"; break;
		default: result = "Parameter" ;
		}
		return result;
	}

	public static MappingMetodoQuery inizializzaMMQDelete(Manager manager, String nomeQuery, String nomeMetodoQuery,
			Set<Attributo> primaryKeys) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(nomeMetodoQuery);
		mmq.setTipoRitorno("boolean");

		String atribs = "";
		for(Attributo a: primaryKeys)
			atribs += a.getTipo() + " " + a.getNome() + ", ";
		atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
	
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Session session = factory.openSession();");
		mmq.getM1().add("Transaction tx = null;");

		mmq.m4a.add("tx = session.beginTransaction();");
		
		mmq.m4b.add("Query query= session.createSQLQuery("+nomeQuery+");");
		
		int index = 0;
		for(Attributo atr : primaryKeys)
		{
			String tmp;
			if(!atr.getTipo().equals(Generatore.DATE)) { //TODO da adattare per hibernate
				tmp = atr.getNome();
			}else {
				mmq.m4b.add(dateJavaToSQL(atr));
				tmp="dataSQL";
			}
			mmq.m4b.add("query.set"+getMapingType(atr)+"("+index+","+tmp+");");
			index++;
		}
		
		mmq.m4e.add("query.executeUpdate();");
		mmq.m4e.add("tx.commit();");
		mmq.m4f.add("result = true;");
		mmq.m5.add("if (tx!=null)");
		mmq.m5.add("	tx.rollback();");
		mmq.m6.add("session.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQRead(Manager manager, String nomeQuery, String nomeMetodoQuery,
			Set<Attributo> primaryKeys) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
