package com.github.calmlow.instructions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import com.github.calmlow.core.Initialization;
import com.sunopsis.tools.core.exception.SnpsSimpleMessageException;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.DefaultTransactionDefinition;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.runtime.scenario.OdiScenario;
import oracle.odi.generation.support.OdiScenarioGeneratorImpl;
import oracle.odi.domain.project.OdiPackage;

public class GenerateScenario {
	private boolean overwriteScenario = true;
	
	/**
	 * Use this main method to easily test the plugin Method may be removed in
	 * the future
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String executeClass = "com.sunopsis.dwg.tools.OdiExportObject";

		System.out.println("\nExecuting stuff \n\n\n");
		
		GenerateScenario gc = new GenerateScenario();
		gc.generate();
		
	}

	protected void generate(){
		
		Initialization i = new Initialization();
		i.setDatabaseHost("oraclelinux.lokalen");
		i.setDatabasePort("1521");
		i.setDatabaseSID("orcl");
		i.setMasterUser("DEV_ODI_REPO7");
		i.setMasterPass("odi7");
		i.setWorkRep("WORKREP7");
		i.setOdiUser("SUPERVISOR");
		i.setOdiPass("odiodi7");

		OdiInstance odi = null;
		
		try {
			odi = i.getOdiInstance();
			OdiProject project = i.getProject(odi, "DEFAULTTESTS");
			
			OdiPackage pPackage = i.getPackage(odi, "first", project);
			
			System.out.println("Packeinfo----");
			//System.out.println("desc: "+ pPackage.getDescription());
			System.out.println("Times generated: "+ pPackage.getInternalVersion());
			//System.out.println("LastUser: "+ pPackage.getLastUser());
			
			System.out.println("InternalId: "+ pPackage.getInternalId());
			
			System.out.println("\n\nOdiScenGen tryouts:\n");
			ITransactionStatus trans = i.openTransaction(odi);
			
			
			OdiScenarioGeneratorImpl generator = new OdiScenarioGeneratorImpl( odi );
			
			
			OdiScenario latestScen = i.getLastScenForPackage(odi, pPackage);
			if(latestScen != null){
				System.out.println("\n-------latestScen---------");
				System.out.println("getName: " + latestScen.getName());
				System.out.println("getVersion: " + latestScen.getVersion());
				System.out.println("getInternalId: " + latestScen.getInternalId());
				System.out.println("getInternalVersion: " + latestScen.getInternalVersion());
				System.out.println("getScenarioFolder: " + latestScen.getScenarioFolder());
				System.out.println("getFirstDate: " + latestScen.getFirstDate());
				System.out.println("getLastDate: " + latestScen.getLastDate());
				System.out.println("getTag: " + latestScen.getTag());
				System.out.println("\n----------------\n\n");
				
			}
			
			if(latestScen != null && overwriteScenario){
				System.out.println("\n\nREGENERATING LAST SCEN: " + latestScen.getTag()+ "\n");
				generator.regenerateScenario(latestScen);
				
			} else {
				String newSpecifiedVersion = "003";
				System.out.println("Bumb up version: " + latestScen.getVersion());
				OdiScenario newScenario = generator.generateScenario(pPackage, "FIRSTT", newSpecifiedVersion);
				System.out.println("Scenario INFO");
				System.out.println("getVersion: " + newScenario.getVersion());
				System.out.println("getInternalId: " + newScenario.getInternalId());
				System.out.println("getHashCode: " + newScenario.hashCode());
				System.out.println("getTag: " + newScenario.getTag());
			
			}
			
			//odi.getTransactionalEntityManager().persist(pkg);
	        
	        // Commit
			odi.getTransactionManager().commit(trans);
			
		} catch(oracle.odi.core.config.OdiConfigurationException e){
			
			System.out.println(e.getMessage());
			
		} finally {
	        if (odi != null){
	        	try {
	        		// Finally close the Instance
		        	System.out.println("\n\nClosing the ODI connection...\n\n");
		        	odi.close();
		        } catch (Exception e) {
		        	System.out.println(e.getMessage());
		        }
	        }	        	
	    } 
	}

	public static int executeProcess(String executeClass,
			ArrayList<String> paramsList) throws IOException {
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add("java");
		commandList.add("-cp");
		commandList.add(System.getProperty("java.class.path"));
		commandList.add(executeClass);
		commandList.addAll(paramsList);
		return executeJavaProcess(commandList);
	}

	public static int executeJavaProcess(ArrayList<String> commandList)
			throws IOException {
		ProcessBuilder builder = new ProcessBuilder(commandList);
		Map<String, String> environment = builder.environment();
		environment.put("path", System.getProperty("path.separator")); // Clearing
																		// the
																		// path
																		// variable;
		environment
				.put("path", System.getProperty("java.home") + File.separator
						+ "bin" + System.getProperty("path.separator"));
		builder = builder.redirectErrorStream(true);
		Process javaProcess = builder.start();
		writeProcessOutput(javaProcess);
		if (System.getProperty("os.name").equals("Linux")) {
			try {
				javaProcess.waitFor();
			} catch (InterruptedException e) {
				throw new IOException(e.getMessage());
			}
		}
		return javaProcess.exitValue();
	}

	static void writeProcessOutput(Process process) throws IOException {
		InputStreamReader tempReader = new InputStreamReader(
				new BufferedInputStream(process.getInputStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
	}
}
