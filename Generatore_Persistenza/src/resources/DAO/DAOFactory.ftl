package it.unibo.tw.dao;

import it.unibo.tw.dao.db2.DB2DAOFactory;

public abstract class DAOFactory {

	// --- List of supported DAO types ---

	/**
	 * Numeric constant '0' corresponds to explicit DB2 choice
	 */
	public static final int DB2 = 0;
	
	/**
	 * Numeric constant '1' corresponds to explicit Hsqldb choice
	 */
	public static final int HSQLDB = 1;
	
	/**
	 * Numeric constant '2' corresponds to explicit MySQL choice
	 */
	public static final int MYSQL = 2;
	
	
	// --- Actual factory method ---
	
	/**
	 * Depending on the input parameter
	 * this method returns one out of several possible 
	 * implementations of this factory spec 
	 */
	public static DAOFactory getDAOFactory(int whichFactory) {
		switch ( whichFactory ) {
		case DB2:
			return new DB2DAOFactory();
//		case HSQLDB:
//			return; //new HsqldbDAOFactory();
//		case MYSQL:
//			 return new MySqlDAOFactory();
		default:
			return null;
		}
	}
	
	
	
	// --- Factory specification: concrete factories implementing this spec must provide this methods! ---

<#list daos as dao>	
	/**
	 * Method to obtain a DATA ACCESS OBJECT
	 * for the datatype ${dao.nome}
	 */
	public abstract ${dao.nomeDAO} get${dao.nomeDAO}();
	
</#list>

}