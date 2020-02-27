package it.unibo.tw.dao.db2;

import java.util.*;
import it.unibo.tw.dao.*;

public class DB2PartnerDTOProxy extends PartnerDTO {

	private static final long serialVersionUID = 1L;

	public DB2PartnerDTOProxy() {
		super();
	}
	
	@Override
	public Set<WorkPackageDTO> getWorkPackages(){
		if(!listaWorkPackagesIsAlreadyLoaded()){
			WorkPackagePartnerMappingDAO workPackagePartnerMapping = new DB2WorkPackagePartnerMappingDAO();
			listaWorkPackagesIsAlreadyLoaded(true);
			super.setWorkPackages(workPackagePartnerMapping.findWorkPackagesByIdPartner(this.getIdPartner()));
		}			
		return super.getWorkPackages();
	}
	
}