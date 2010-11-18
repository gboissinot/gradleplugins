package com.thalesgroup.gradle.pde.tasks;

import groovy.util.AntBuilder;

import java.util.List;

public class GenerateMapFileAction {

    private String buildDirectory;
    private String pluginsSrcDir;
    private AntBuilder ant;


    public GenerateMapFileAction(String buildDirectory, String pluginsSrcDir, AntBuilder ant) {
        this.buildDirectory = buildDirectory;
        this.pluginsSrcDir = pluginsSrcDir;
        this.ant = ant;
    }






    public void generate(List<String> features) {
        
    }

}
