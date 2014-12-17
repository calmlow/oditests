package com.github.calmlow.maven;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Test
 * 
 * @requiresDependencyResolution
 */
@Mojo( name = "genscen", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class GenerateScenMojo extends AbstractMojo {
	
	@Parameter(property="project", defaultValue="${project}", required=true, readonly=true)
    protected MavenProject project;
    
    @Parameter(property="plugins", defaultValue="${plugin.artifacts}")
    private List<DefaultArtifact> pluginArtifacts;
	
    @Parameter(property="workRepository")
    private String workRepository;
    
    @Parameter(property="test2")
    private String test2;
    
    
    
    /*
	public String getWorkRepository() {
		return workRepository;
	}

	public void setWorkRepository(String workRepository) {
		this.workRepository = workRepository;
	}*/

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		System.out.println("fdssfd");
		
		getLog().info("--- Classpath ---");
		
		getLog().info("workRepository: "+ workRepository);
		getLog().info("test2: "+ test2);
		
		getLog().info("Artifacts no: " + project.getArtifacts() );
		getLog().info("getPluginArtifacts: " + project.getPluginArtifacts() );
		getLog().info("getAttachedArtifacts: " + project.getAttachedArtifacts() );
		getLog().info("ParentFile: " + project.getParentFile() );
		
		
		if (pluginArtifacts != null){
	        for(int i=0; i<pluginArtifacts.size(); i++){
	            if ( pluginArtifacts.get(i) instanceof org.apache.maven.artifact.DefaultArtifact){
	                DefaultArtifact mavenPluginArtifact = (DefaultArtifact) pluginArtifacts.get(i);
	                getLog().info( mavenPluginArtifact.getFile().getAbsolutePath() );
	                //if (classpath != null && !classpath.equals(""))
	                  //  classpath += System.getProperty("path.separator");
	                //classpath += mavenPluginArtifact.getFile().getAbsolutePath();
	            }
	        }
	    }
	}
	
	
}
