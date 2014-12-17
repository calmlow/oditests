package com.github.calmlow.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiConfigurationException;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.DefaultTransactionDefinition;
import oracle.odi.core.security.Authentication;
import oracle.odi.domain.project.OdiFolder;
import oracle.odi.domain.project.OdiPackage;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiFolderFinder;
import oracle.odi.domain.project.finder.IOdiPackageFinder;
import oracle.odi.domain.project.finder.IOdiProjectFinder;
import oracle.odi.domain.runtime.scenario.OdiScenario;
import oracle.odi.domain.runtime.scenario.finder.IOdiScenarioFinder;

public class Initialization {
	
	private String databaseUrl = null;
	private String databaseHost = "";
	private String databasePort = "";
	private String databaseSID = "orcl";
	private String databaseDriver = "oracle.jdbc.OracleDriver";
	private String masterUser;
	private String masterPass;
	private String workRep;
	private String odiUser;
	private String odiPass;
	
	public void setDatabaseHost(String databaseHost) {
		this.databaseHost = databaseHost;
	}
	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}

	public void setDatabaseSID(String databaseSID) {
		this.databaseSID = databaseSID;
	}

	public void setMasterUser(String masterUser) {
		this.masterUser = masterUser;
	}

	public void setMasterPass(String masterPass) {
		this.masterPass = masterPass;
	}

	public void setWorkRep(String workRep) {
		this.workRep = workRep;
	}

	public void setOdiUser(String odiUser) {
		this.odiUser = odiUser;
	}

	public void setOdiPass(String odiPass) {
		this.odiPass = odiPass;
	}
	
	public Initialization() {
		//this.databaseUrl = "jdbc:oracle:thin:@oraclelinux.lokalen:1521:orcl";
		this.databaseUrl = "jdbc:oracle:thin:@"+databaseHost+":"+databasePort+":"+databaseSID;
    	/*this.masterUser = "DEV_ODI_REPO7";
    	this.masterPass = "odi7";
    	this.workRep = "WORKREP7";
    	this.odiUser = "SUPERVISOR";
    	this.odiPass = "odiodi7";*/
    }
	
	public OdiInstance getOdiInstance() throws OdiConfigurationException {
        // Connection
        MasterRepositoryDbInfo masterInfo =
            new MasterRepositoryDbInfo(databaseUrl, databaseDriver, masterUser, masterPass.toCharArray(), new PoolingAttributes());
        WorkRepositoryDbInfo workInfo = new WorkRepositoryDbInfo(workRep, new PoolingAttributes());
        OdiInstance odiInstance = OdiInstance.createInstance(new OdiInstanceConfig(masterInfo, workInfo));
        Authentication auth = odiInstance.getSecurityManager().createAuthentication(odiUser, odiPass.toCharArray());
        
        odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth);
        
        return odiInstance;
    }
	public ITransactionStatus openTransaction(OdiInstance odi) {
		ITransactionStatus transaction = odi.getTransactionManager().getTransaction(new DefaultTransactionDefinition());
		return transaction;
	}
	
	public OdiProject getProject(OdiInstance odi, String code){
    	OdiProject project = ((IOdiProjectFinder) odi.getTransactionalEntityManager().getFinder(OdiProject.class)).findByCode(code);
    	
        System.out.println("::: " + project + " ::: " + project.getName() + " :::");
        System.out.println("Project Code: " + project.getCode() );
        return project;
    }
	
	public OdiPackage getPackage(OdiInstance odi, String packageName, OdiProject project){
		Collection<OdiPackage> packageCollection = ((IOdiPackageFinder) odi.getTransactionalEntityManager().getFinder(OdiPackage.class)).findByName(packageName, project.getCode());
		
		OdiPackage odiPackage = null;
		for (Iterator<OdiPackage> it = packageCollection.iterator(); it.hasNext();) {
            odiPackage = (OdiPackage) it.next();
        }
		
		return odiPackage;
	}
	public OdiScenario getLastScenForPackage(OdiInstance odi, OdiPackage odiPackage){
		Collection<OdiScenario> scenCollection = ((IOdiScenarioFinder) odi.getTransactionalEntityManager().getFinder(OdiScenario.class)).findBySourcePackage(odiPackage.getPackageId());
		
		List<OdiScenario> scenList = new ArrayList<OdiScenario>( scenCollection );
		Collections.sort( scenList, new OdiScenarioComparator() );
		
		for(OdiScenario s: scenList){
			System.out.println(s.getTag());
		}
		
		if(scenList.size() > 0){
			return scenList.get(scenList.size() - 1);
		} else {
			return null;
		}
		
		/*OdiScenario odiScenario = null;
		for (Iterator<OdiScenario> it = scenCollection.iterator(); it.hasNext();) {
			odiScenario = (OdiScenario) it.next();
        }
		
		return odiScenario;*/
	}
	
	private class OdiScenarioComparator implements Comparator<OdiScenario> {
	    
		@Override
	    public int compare(OdiScenario o1, OdiScenario o2) {
			return o1.getVersion().compareTo(o2.getVersion() );
	    }
	}
}

