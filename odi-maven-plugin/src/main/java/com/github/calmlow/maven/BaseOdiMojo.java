package com.github.calmlow.maven;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;


public class BaseOdiMojo extends AbstractMojo {
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		System.out.println("\nIm in BaseOdiMojo \n\n");
	}
	
	@Parameter(property="component-type")
    protected String odiComponentType;
	

}
