package com.github.calmlow.maven;

import java.util.List;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;


@Mojo( name = "verify", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class VerifyObjectsMojo extends BaseOdiMojo {
	
	@Parameter(property="project", defaultValue="${project}", required=true, readonly=true)
    protected MavenProject project;
    
    @Parameter(property="plugins", defaultValue="${plugin.artifacts}")
    private List<DefaultArtifact> pluginArtifacts;
	
    @Parameter(property="workRepository")
    private String workRepository;
    
    @Parameter(property="test2")
    private String test2;
    
    @Parameter(property="no-commit")
    private boolean noCommit;
    
    
    public VerifyObjectsMojo(){
    	super();
    }
    
    /*
	public String getWorkRepository() {
		return workRepository;
	}

	public void setWorkRepository(String workRepository) {
		this.workRepository = workRepository;
	}*/
    private boolean isComponentTypeCorrect(String componentType){
    	String validComponentTypes[] = {"Project","Procedures","Packages"};
    	boolean validType = false;
    	for(String t: validComponentTypes){
    		if(t.equalsIgnoreCase(componentType)){
    			validType = true;
    		}
    	}
    	return validType;
    }

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		String component = project.getParentArtifact().getArtifactId();
		
		if( ! isComponentTypeCorrect(component)){
			getLog().error("Component has wrong type or placement");
			throw new MojoExecutionException("Component has wrong type or placement");
		}
		
		String projectCodeUnid = project.getParent().getParent().getArtifactId();
		String projectCodeUnidFallBack = project.getParent().getParent().getName();
		
		
		getLog().info("--- Classpath ---");
		getLog().info("projectCodeUnid: " + projectCodeUnid);
		getLog().info("odiComponentType: "+ odiComponentType);
		getLog().info("noCommit: "+ noCommit);
		getLog().info("workRepository: "+ workRepository);
		getLog().info("test2: "+ test2);
		
		getLog().info("Artifacts no: " + project.getArtifacts() );
		getLog().info("getArtifactId: " + project.getArtifactId() );
		getLog().info("getArtifact.getId(): " + project.getArtifact().getId() );
		getLog().info("getParent.artifactId(): " + project.getParentArtifact().getArtifactId() );
		getLog().info("getName: " + project.getName() );
		getLog().info("getPluginArtifacts: " + project.getPluginArtifacts() );
		getLog().info("getAttachedArtifacts: " + project.getAttachedArtifacts() );
		getLog().info("ParentFile: " + project.getParentFile() );
		getLog().info("getPackaging: " + project.getPackaging() );
		getLog().info("getModules: " + project.getModules() );
		getLog().info("----------------------------------------" );
		getLog().info("getProperties: " + project.getProperties() );
		
		
		if (pluginArtifacts != null){
	        for(int i=0; i<pluginArtifacts.size(); i++){
	            if ( pluginArtifacts.get(i) instanceof org.apache.maven.artifact.DefaultArtifact){
	                DefaultArtifact mavenPluginArtifact = (DefaultArtifact) pluginArtifacts.get(i);
	                //getLog().info( mavenPluginArtifact.getFile().getAbsolutePath() );
	                //if (classpath != null && !classpath.equals(""))
	                  //  classpath += System.getProperty("path.separator");
	                //classpath += mavenPluginArtifact.getFile().getAbsolutePath();
	            }
	        }
	    }
	}
	
	
}

