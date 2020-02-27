package it.unibo.tw.dao.db2;

import java.util.*;
import it.unibo.tw.dao.*;

public class DB2ProgettoDTOProxy extends ProgettoDTO {

	private static final long serialVersionUID = 1L;

	public DB2ProgettoDTOProxy() {
		super();
	}
	
	@Override
	public Set<WorkPackageDTO> getWorkPackages(){
		if(!listaWorkPackagesIsAlreadyLoaded()){
			WorkPackageDAO workPackage = new DB2WorkPackageDAO();
			listaWorkPackagesIsAlreadyLoaded(true);
			super.setWorkPackages(workPackage.findWorkPackagesByIdProgetto(this.getIdProgetto()));
		}			
		return super.getWorkPackages();
	}
	
}