package generatore.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import generatore.Generatore;
import generatore.global.Classe;
import generatore.problema.Attributo;
import generatore.problema.MappingMetodoQuery;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;

public class UtilsDAO {

	
	private static void MMQinizializato(MappingMetodoQuery mmq) {
		//MappingMetodoQuery mmq = new MappingMetodoQuery();

//		mmq.m1.add("");
//		mmq.m2.add("");
		mmq.m3.add("conn = DB2DAOFactory.createConnection(); ");
//		mmq.m4a.add("");
//		mmq.m4b.add("");
//		mmq.m4c.add("");
//		mmq.m4d.add("");
//		mmq.m4e.add("");
//		mmq.m4f.add("");
		mmq.m5.add("e.printStackTrace();");
		mmq.m6.add("DB2DAOFactory.closeConnection(conn);");
//		mmq.m7.add("");
		//return mmq;
	}
	
	public static MappingMetodoQuery inizializzaMMQCreate(DB2DAO db2dao) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);

		mmq.setNomeMetodo("createTable");
		mmq.setTipoRitorno("boolean");
		mmq.setByAttributi("");
		
		mmq.getM1().add("boolean result = false;");
		mmq.m1.add("Connection conn = null;");

		//mmq.m2="";

		mmq.m4a.add("Statement stmt = conn.createStatement();");

		//mmq.m4b="";
		mmq.m4c.add("stmt.execute(create);");

		mmq.m4d.add("result = true;");
		mmq.m4f.add("stmt.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQDrop(DB2DAO db2dao) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);

		mmq.setNomeMetodo("drop");
		mmq.setTipoRitorno("boolean");
		mmq.setByAttributi("");
		
		mmq.getM1().add("boolean result = false;");
		mmq.m1.add("Connection conn = null;");
		//mmq.m2="";
		mmq.m4a.add("Statement stmt = conn.createStatement();");
		//mmq.m4b="";
		mmq.m4c.add("stmt.execute(drop);");
		mmq.m4d.add("result = true;");
		mmq.m4f.add("stmt.close();");
		mmq.m7.add("return result;");
		return mmq;
	}
	
	private static String generaCheck(Set<Attributo> list) {
		String result="";
		if(!list.isEmpty()) {
			for(Attributo a: list)
				result+=getCheck(a) + " || " ;
			result = result.substring(0, result.length() - 4);
		}
		return result;
	}

	private static String getCheck(DB2DAO c) {	
		return getCheck(new Attributo(c.getNomeBean(), c.getNomeOggetto()));
	}
	
	private static String getCheck(Attributo a) {
		String result;
		switch (a.getTipo().toLowerCase()) {
		case "int"  : result = a.getNome() +" < 0"; break;
		case "long"  : result = a.getNome() +" < 0"; break;
		case "float"  : result = a.getNome() +" < 0"; break;
		case "double"  : result = a.getNome() +" < 0"; break;
		case "string" : result = a.getNome() + " == null || "+ a.getNome()+".isEmpty() "; break;
		default: result = a.getNome() + " == null" ;
		}
		return result;
	}
	
	public static MappingMetodoQuery inizializzaMMQUpdate(DB2DAO db2dao) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);

		mmq.setNomeMetodo("update");
		mmq.setTipoRitorno("boolean");
		
		String atribs = "";
		List<Attributo> list = new ArrayList<Attributo>();
		if(db2dao.getHaveUML())
			list.add(new Attributo(db2dao.getNomeBean(), db2dao.getNomeOggetto()));
		else 
			list.addAll(db2dao.getAllAttributi());
		
			for(Attributo a: list)
				atribs += a.getTipo() + " " + a.getNome() + ", ";
			atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
	
		
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Connection conn = null;");
		mmq.m2.add("if("+getCheck(db2dao)+")");
		mmq.m2.add(	"	return result;");
		
		mmq.m3.add(	"//if (conn.getMetaData().supportsTransactions()) // se si vuole verificare e gestire il supporto alle transazioni");
		mmq.m3.add(	"conn.setAutoCommit(false); // start transaction block");
//		mmq.m3.add(	"//	se non supporta le transazioni il metodo commit() non esegue alcuna operazione ");
//		mmq.m3.add(	"//	e il livello di isolamento è sempre TRANSACTION_NONE");
//		mmq.m3.add(	"//	che equivale ad un setAutoCommit(true).");
//		mmq.m3.add(	"//	se neccesario si può selezionare il livello di isolamento tramite setTransactionLevel() ");
//		mmq.m3.add(	"//	sempre che il DB lo permetta, è verificabile tramite getMetaData())\n");
				
		mmq.m4a.add("PreparedStatement statement = conn.prepareStatement(update);");
		
		mmq.m4b.add("statement.clearParameters();");
		int index = 1;
		for(Attributo atr : db2dao.getAllAttributi())
		{
			String tmp;
			if(!atr.getTipo().equals(Generatore.DATE)) {
				tmp = db2dao.getNomeOggetto()+".get"+atr.getNomeUp()+"()";
			}else {
				mmq.m4b.add("long secs = "+db2dao.getNomeOggetto()+".get"+ atr.getNomeUp() + "().getTime();");
				mmq.m4b.add("java.sql.Date dataSQL = new java.sql.Date(secs);");
				tmp="dataSQL";
			}
			mmq.m4b.add("statement.set"+atr.getTipoUp()+"("+index+","+tmp+");");
			index++;
		}

		mmq.m4c.add("statement.executeUpdate();");
		mmq.m4c.add("conn.commit(); //esegui transazione");
		mmq.m4d.add("result = true;");
		mmq.m4f.add("statement.close();");
		
		mmq.m5.remove(0);
		mmq.m5.add("if (conn != null) {");
		mmq.m5.add("	try {");
		mmq.m5.add("		System.err.print(\"Transaction is being rolled back\");");
		mmq.m5.add("		conn.rollback();");
		mmq.m5.add("	} catch(SQLException excep) {");
		mmq.m5.add("		excep.printStackTrace();");
		mmq.m5.add("	}");
		mmq.m5.add("}");
		mmq.m5.add("e.printStackTrace();");
		
		//mmq.m6.add("conn.setAutoCommit(true); //una buona prassi e ripristinare l'auto-commit");
		mmq.m7.add("return result;");
		return mmq;
	}
	
	private static String dateJavaToSQL(Attributo atr) {
		String result="";
		result+=	"	long sec = "+ atr.getNome() + ".getTime();\n";
		result+=	"	java.sql.Date dataSQL = new java.sql.Date(sec);\n";
		return result;
	}
	
	private static String dateSQLtoJAVA(Attributo atr) {
		String result="\n";
		result+="\t\t\t\tlong secs = rs.get"+atr.getTipo()+"(\""+ atr.getNomeColumn() + "\").getTime();\n";
		result+="\t\t\t\tjava.util.Date dataJAVA = new java.util.Date(secs);";
		return result;
	}

	public static MappingMetodoQuery inizializzaMMQInsert(DB2DAO db2dao) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo("create");
		mmq.setTipoRitorno("boolean");

		String atribs = "";
		Set<Attributo> list = new LinkedHashSet<Attributo>();
		if(db2dao.getHaveUML())
			list.add(new Attributo(db2dao.getNomeBean(), db2dao.getNomeOggetto()));
		else 
			list.addAll(db2dao.getAllAttributi());
		
			for(Attributo a: list)
				atribs += a.getTipo() + " " + a.getNome() + ", ";
			atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
	
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Connection conn = null;");
		mmq.m2.add("if("+generaCheck(list)+")");
		mmq.m2.add(	"	return result;");
		
		mmq.m3.add(	"//if (conn.getMetaData().supportsTransactions()) // se si vuole verificare e gestire il supporto alle transazioni");
		mmq.m3.add(	"conn.setAutoCommit(false); // start transaction block");
//		mmq.m3.add(	"//	se non supporta le transazioni il metodo commit() non esegue alcuna operazione ");
//		mmq.m3.add(	"//	e il livello di isolamento è sempre TRANSACTION_NONE");
//		mmq.m3.add(	"//	che equivale ad un setAutoCommit(true).");
//		mmq.m3.add(	"//	se neccesario si può selezionare il livello di isolamento tramite setTransactionLevel() ");
//		mmq.m3.add(	"//	sempre che il DB lo permetta, è verificabile tramite getMetaData())\n");
		
		mmq.m4a.add("PreparedStatement statement = conn.prepareStatement(insert);");

		String name = db2dao.getNomeOggetto();
		
		List<Attributo> la = new ArrayList<Attributo>() ;
//		if(classe.isClasseUML()) {
//			la.addAll(listaPrimaryKeys) ;
//		}
//		else {
//			la.addAll(classe.getPrimaryKeys());
//				
//		}
		
		la.addAll(db2dao.getAllAttributi());
		
		int index = 1;
		for(Attributo atr : la)
		{
			if(db2dao.getHaveUML()) {
				String tmp;
				if(!atr.getTipo().equals(Generatore.DATE)) {
					tmp = name+".get"+atr.getNomeUp()+"()";
				}else {
					mmq.m4b.add("long secs = "+name+".get"+ atr.getNomeUp() + "().getTime();");
					mmq.m4b.add("java.sql.Date dataSQL = new java.sql.Date(secs);");
					tmp="dataSQL";
				}
				mmq.m4b.add("statement.set"+atr.getTipoUp()+"("+index+","+tmp+");");
			}else {
				String tmp;
				if(!atr.getTipo().equals(Generatore.DATE)) {
					tmp = atr.getNome();
				}else {
					mmq.m4b.add(dateJavaToSQL(atr));
					tmp="dataSQL";
				}
				mmq.m4b.add("statement.set"+atr.getTipoUp()+"("+index+","+tmp+");");
			}
			index++;
		}		
		mmq.m4c.add("statement.executeUpdate();");
		mmq.m4c.add("conn.commit(); //esegui transazione");
		mmq.m4d.add("result = true;");
		mmq.m4f.add("statement.close();");
		
		mmq.m5.remove(0);
		mmq.m5.add("if (conn != null) {");
		mmq.m5.add("	try {");
		mmq.m5.add("		System.err.print(\"Transaction is being rolled back\");");
		mmq.m5.add("		conn.rollback();");
		mmq.m5.add("	} catch(SQLException excep) {");
		mmq.m5.add("		excep.printStackTrace();");
		mmq.m5.add("	}");
		mmq.m5.add("}");
		mmq.m5.add("e.printStackTrace();");
		
		mmq.m7.add("return result;");
		return mmq;
	}
	
	public static MappingMetodoQuery inizializzaMMQRead(DB2DAO db2dao, String nomeQuery, String nomeMetodoQuery, Set<Attributo> attributi ) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(nomeMetodoQuery);
		mmq.setTipoRitorno(db2dao.getNomeBean());
		
		String atribs = "";
		for(Attributo a: attributi)
			atribs += a.getTipo() + " " + a.getNome() + ", ";
		atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
		
		mmq.m1.add(db2dao.getNomeBean() + " result = null;");
		mmq.m1.add("Connection conn = null;");
		mmq.m2.add("if("+generaCheck(attributi)+")\n\t\t\treturn result;");
		mmq.m4a.add("PreparedStatement statement = conn.prepareStatement("+nomeQuery+");");
		mmq.m4b.add("statement.clearParameters();");
		int index = 1;
		for(Attributo atr : attributi)
		{
			String tmp;
			if(!atr.getTipo().equals(Generatore.DATE)) {
				tmp = atr.getNome();
			}else {
				mmq.m4b.add(dateJavaToSQL(atr));
				tmp="dataSQL";
			}
			mmq.m4b.add("statement.set"+atr.getTipoUp()+"("+index+","+tmp+");");
			index++;
		}
		
		mmq.m4c.add("ResultSet rs = statement.executeQuery();");
		mmq.m4d.add("if ( rs.next() ) {");
		mmq.m4d.add(	"	result = new "+ db2dao.getNomeBean() +"();");
		
		String tmp;
		for(Attributo atr : db2dao.getAllAttributi()) {
			if(!atr.getTipo().equals(Generatore.DATE)) {
				tmp = 	"rs.get"+atr.getTipoUp()+"(\""+atr.getNomeColumn()+"\")";
			}else {
				mmq.m4d.add(	"	"+dateSQLtoJAVA(atr));
				tmp="dataJAVA";
			}
			mmq.m4d.add(	"	result.set"+atr.getNomeUp()+"("+tmp+");");
		}
		
		Set<Riferimento<? extends Classe>> refs = db2dao.getRiferimenti();
		for(Riferimento<? extends Classe> r: refs) {
			if(r.getThereIsDirectReferences() && !r.getIsLazyLoad()) {
				Attributo atri = r.getAttributo();
				Attributo pk = r.getTo().getPrimaryKey();
				tmp = "rs.get"+pk.getTipoUp()+"(\""+pk.getNomeColumn()+"\")";
				
				String t = r.getTipoRelazione();
				if(t.equals("n1")||t.equals("11")) {
					tmp = "new DB2"+r.getTo().getNome()+"DAO().readBy"+pk.getNomeUp()+"("+tmp+")";
					mmq.m4d.add(	"	result.set"+r.getTo().getNome()+"("+tmp+");");
				}else{
					//if(t.equals("nm"))
						tmp = "new DB2"+r.getTo().getNome()+"DAO().find"+r.getTo().getNomePlurale()+"By"+r.getFrom().getPrimaryKey().getNomeUp()+"("+tmp+")";
					//else
						//tmp = "new DB2"+r.getTo().getNome()+"DAO().findBy"+pk.getNomeUp()+"("+tmp+")";
					mmq.m4d.add(	"	result.set"+r.getTo().getNomePlurale()+"("+tmp+");");
				}
				
			}
		}
		
		mmq.m4d.add("}");
		
		mmq.m4e.add("rs.close();");
		mmq.m4f.add("statement.close();");
		mmq.m7.add("return result;");
		
		return mmq;
	}


	public static MappingMetodoQuery inizializzaMMQDelete(DB2DAO db2dao, String nomeQuery, String nomeMetodoQuery, Set<Attributo> attributi ) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(nomeMetodoQuery);
		mmq.setTipoRitorno("boolean");
		
		String atribs = "";
		for(Attributo a: attributi)
			atribs += a.getTipo() + " " + a.getNome() + ", ";
		atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
		
		mmq.m1.add("boolean result = false;");
		mmq.m1.add("Connection conn = null;");
		mmq.m2.add("if("+generaCheck(attributi)+")");
		mmq.m2.add("\treturn result;");

		mmq.m4a.add("PreparedStatement statement = conn.prepareStatement("+nomeQuery+");");
		mmq.m4b.add("statement.clearParameters();");
		int index = 1;
		for(Attributo atr : attributi)
		{
			String tmp;
			if(!atr.getTipo().equals(Generatore.DATE)) {
				tmp = atr.getNome();
			}else {
				mmq.m4b.add(dateJavaToSQL(atr));
				tmp="dataSQL";
			}
			mmq.m4b.add("statement.set"+atr.getTipoUp()+"("+index+","+tmp+");");
			index++;
		}
		mmq.m4c.add("statement.executeUpdate();");

		mmq.m4d.add("result = true;");
		mmq.m4f.add("statement.close();");
		mmq.m7.add("return result;");
		return mmq;
	}

	public static MappingMetodoQuery inizializzaMMQFind(DB2DAO db2dao, MetodoFind<DB2DAO> mf ,String nomeQuery, String nomeMetodoQuery, Set<Attributo> attributi ) {
		MappingMetodoQuery mmq = new MappingMetodoQuery();
		MMQinizializato(mmq);
		
		mmq.setNomeMetodo(nomeMetodoQuery);
		mmq.setTipoRitorno("Set<"+mf.getClasseTarget().getNomeBean()+">");
		
		String atribs = "";
		for(Attributo a: attributi)
			atribs += a.getTipo() + " " + a.getNome() + ", ";
		atribs = atribs.substring(0, atribs.length() - 2);
		//}
		mmq.byAttributi = atribs;
		//mmq.byAttributi = find.getStringAttributiMetodoQuery();
		
		mmq.m1.add("Set<"+mf.getClasseTarget().getNomeBean()+"> result = new HashSet<"+mf.getClasseTarget().getNomeBean()+">();");
		mmq.m1.add("Connection conn = null;");
		mmq.m2.add("if("+generaCheck(attributi)+")");
		mmq.m2.add("\treturn result;");
		//mmq.m3.add("result = new ArrayList<"+mf.getClasseTarget().getNomeBean()+">();");
		mmq.m4a.add("PreparedStatement statement = conn.prepareStatement("+nomeQuery+");");
		
		mmq.m4b.add("statement.clearParameters();");
		String tmp;
		Attributo atr = mf.getAttributo();
		if(!atr.getTipo().equals(Generatore.DATE)) {
			tmp = atr.getNome();
		}else {
			mmq.m4b.add(dateJavaToSQL(atr));
			tmp="dataSQL";
		}
		mmq.m4b.add("statement.set"+atr.getTipoUp()+"(1, "+tmp+");");
//		int index = 1;
//		for(Attributo atr : attributi)
//		{
//			m4b+="\t\t\tstatement.set"+atr.getTipoUp();
//			m4b+="("+index+","+atr.getNome()+");\n";
//			index++;
//		}

		mmq.m4c.add("ResultSet rs = statement.executeQuery();");
		
		
		mmq.m4d.add("while( rs.next() ) {");
		//if(classe.getReference(mf.getClasseTarget(),mf.getClasseSource())!=null && classe.getReference(mf.getClasseTarget(),mf.getClasseSource()).isLazyLoad())
		String res=("	"+mf.getClasseTarget().getNomeBean()+" entity = new ");
		//System.out.println("inFind: "+mf.getIsLazy()+" "+mf.getClasseTarget());
		if(mf.getIsLazy()) {
			res+=(	"DB2"+ mf.getClasseTarget().getNomeBean()+"Proxy();");     //Proxy
		}
		else
			res+=(	""+mf.getClasseTarget().getNomeBean()+"();");
		mmq.m4d.add(res);
		List<Attributo> attrs = new ArrayList<Attributo>();
		attrs.addAll(mf.getClasseTarget().getPrimaryKeys());
		attrs.addAll(mf.getClasseTarget().getAttributi());
		
		for(Attributo atri : attrs) {
			
			//String tmp;
			if(!atri.getTipo().equals(Generatore.DATE)) {
				tmp = "rs.get"+atri.getTipoUp()+"(\""+atri.getNomeColumn()+"\")";
			}else {
				mmq.m4d.add(	"	"+dateSQLtoJAVA(atri));
				tmp="dataJAVA";
			}
			mmq.m4d.add(	"	entity.set"+atri.getNomeUp()+"("+tmp+");");
			
			//m4d+="\n\t\t\t\tentity.set"+atri.getNomeUp()+"(rs.get"+atri.getTipoUp()+"(\""+atri.getNome()+"\"));";
		}
		
		Set<Riferimento<? extends Classe>> refs = mf.getClasseTarget().getRiferimenti();
		//Set<Riferimento<? extends Classe>> refs = db2dao.getRiferimenti();
		for(Riferimento<? extends Classe> r: refs) {
			if(r.getThereIsDirectReferences() && !r.getIsLazyLoad()) {
				Attributo atri = r.getAttributo();
				Attributo pk = r.getTo().getPrimaryKey();
				tmp = "rs.get"+pk.getTipoUp()+"(\""+pk.getNomeColumn()+"\")";
				
				String t = r.getTipoRelazione();
				if(t.equals("n1")||t.equals("11")) {
					tmp = "new DB2"+r.getTo().getNome()+"DAO().readBy"+pk.getNomeUp()+"("+tmp+")";
					mmq.m4d.add(	"	entity.set"+r.getTo().getNome()+"("+tmp+");");
				}else{
					//if(t.equals("nm"))
						tmp = "new DB2"+r.getTo().getNome()+"DAO().find"+r.getTo().getNomePlurale()+"By"+r.getFrom().getPrimaryKey().getNomeUp()+"("+tmp+")";
					//else
						//tmp = "new DB2"+r.getTo().getNome()+"DAO().findBy"+pk.getNomeUp()+"("+tmp+")";
					mmq.m4d.add(	"	entity.set"+r.getTo().getNomePlurale()+"("+tmp+");");
				}
				
			}
		}
		
		mmq.m4d.add("	result.add(entity);");
		mmq.m4d.add("}");
		
		mmq.m4e.add("rs.close();");
		mmq.m4f.add("statement.close();");
		mmq.m7.add("return result;");
		return mmq;
	}
	
	

}
