package com.github.calmlow.maven;

public class ScenarioObject {
	private String name;
    private String version;
    private String internalId;
    
    
	public String toString(){
        String formatString = "Name: %s\tVersion: %s";
        return String.format(formatString, name, version);
    }
}
