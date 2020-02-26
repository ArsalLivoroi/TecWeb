package it.unibo.tw.dao.db2;

import java.util.*;
import it.unibo.tw.dao.*;

public class DB2WorkPackageDTOProxy extends WorkPackageDTO {

	private static final long serialVersionUID = 1L;

	public DB2WorkPackageDTOProxy() {
		super();
	}
	
	@Override
	public Set<PartnerDTO> getPartners(){
		if(!listaPartnersIsAlreadyLoaded()){
			WorkPackagePartnerMappingDAO workPackagePartnerMapping = new DB2WorkPackagePartnerMappingDAO();
			listaPartnersIsAlreadyLoaded(true);
			super.setPartners(workPackagePartnerMapping.findPartnersByIdWorkPackage(this.getIdWorkPackage()));
		}			
		return super.getPartners();
	}
	
}