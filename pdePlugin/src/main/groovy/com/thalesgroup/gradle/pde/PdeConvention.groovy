/** *****************************************************************************
 * Copyright (c) 2009 Thales Corporate Services SAS                             *
 * Author : Gregory Boissinot                                                   *
 *                                                                              *
 * Permission is hereby granted, free of charge, to any person obtaining a copy *
 * of this software and associated documentation files (the "Software"), to deal*
 * in the Software without restriction, including without limitation the rights *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
 * copies of the Software, and to permit persons to whom the Software is        *
 * furnished to do so, subject to the following conditions:                     *
 *                                                                              *
 * The above copyright notice and this permission notice shall be included in   *
 * all copies or substantial portions of the Software.                          *
 *                                                                              *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
 * THE SOFTWARE.                                                                *
 ****************************************************************************** */

package com.thalesgroup.gradle.pde;


import org.gradle.api.GradleException;
import org.gradle.api.Project

public abstract class PdeConvention {
    Project project;
    Map customValues;
    
    Boolean javacDebugInfo;
    String buildDirectory;
    String builderDir;
    List<String> pluginsSrcDirList;
    List<String> featuresSrcDirList;
    String eclipseLauncher;
    String pdeBuildPluginVersion;
    String equinoxLauncherPluginVersion;
    String base;
    String baseLocation;
    String linksSrcDirectory;
    List<String> extLocations;
    String buildId;
    String publishDirectory;
    String jobVersion;
    String envConfigs = "*, *, *";
    Boolean usePreviousLinks;
    String jvmOptions = "-Xms128m -Xmx512m -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled";
    String javacSource = "1.5";
    String javacTarget = "1.5";
    String data = "eclipsews";
    String eclipseExtensionsRoot = eclipseExtensionsRoot;
    String rcpCleaner = rcpCleaner;
    String buildPropertiesFile;
    
    public PdeConvention(Project project, Map customValues) {
        this.project = project;
        this.customValues = customValues;
    }
    
    public abstract BuildType getType();
    
    public String getBaseLocation() {
        if (baseLocation == null) {
            baseLocation = "${base}/eclipse"
        }
        return normPathForAnt(baseLocation)
    }
    
    public String getBuilderDir() {
        if (builderDir == null) {
            builderDir = "${buildDirectory}/builder"
        }
        return normPathForAnt(builderDir)
    }
    
    public List<String> getExtLocations() {
        List<String> locations = new ArrayList<String>();
        if (this.extLocations != null) {
            for (String loc : this.extLocations) {
                loc = normPathForAnt(loc)
                
                
                if (!usePreviousLinks && !loc.endsWith("/eclipse")) {
                    loc += "/eclipse"
                }
                
                if (!(new File(loc).exists())) {
                    File locFile = new File(eclipseExtensionsRoot, loc)
                        if (!locFile.exists()) {
                            throw new GradleException("${loc} does not exist.")
                        }
                    loc = locFile.toString()
                }
                
                locations << normPathForAnt(loc)
            }
        }
        
        return locations
    }
    
    public String getRcpCleaner() {
        if (rcpCleaner == null) {
            rcpCleaner = "R:/extloc/platform-3.3/rcpcleaner";
        }
        return rcpCleaner;
    }
    
    
    public String getBuildPropertiesFile() {
        return buildPropertiesFile
    }
    
    public String getBase() {
        return normPathForAnt(base)
    }
    
    public String getBuildDirectory() {
        return normPathForAnt(buildDirectory)
    }
    
    public String getEclipseLauncher() {
        return normPathForAnt(eclipseLauncher)
    }
    
    public String getEclipseExtensionsRoot() {
        return normPathForAnt(eclipseExtensionsRoot)
    }
    
    public String getData() {
        if (buildDirectory) {
            return normPathForAnt(buildDirectory + "/" + data)
        } else {
            return normPathForAnt(data)
        }
    }
    
    public String getLinksSrcDirectory() {
        return normPathForAnt(linksSrcDirectory)
    }
    
    public String getPublishDirectory() {
        return normPathForAnt(publishDirectory)
    }
    

    public abstract void printBuiltElements();
    
    
    public void print() {
        println "===================================================="
        println "*                PDE PARAMETERS                    *"
        println "===================================================="
        println "Job version              : " + (jobVersion == null ? "" : jobVersion);
        println "BuildId                  : " + (buildId == null ? "" : buildId);
        printBuiltElements();
        println ""
        println "Build directory          : " + (getBuildDirectory() == null ? "" : getBuildDirectory());
        println "Launcher Path            : " + (getEclipseLauncher() == null ? "" : getEclipseLauncher());
        println "Launcher Plugin Version  : " + (equinoxLauncherPluginVersion == null ? "" : equinoxLauncherPluginVersion);
        println "PDE Plugin Version       : " + (pdeBuildPluginVersion == null ? "" : pdeBuildPluginVersion);
        println "Eclipse workspace (data) : " + (getData() == null ? "" : getData());
        println ""
        println "Builder directory        : " + (getBuilderDir() == null ? "" : getBuilderDir());
        println "Target (base)            : " + (getBase() == null ? "" : getBase());
        println "Target (baseLocation)    : " + (getBaseLocation() == null ? "" : getBaseLocation());
        
        if (linksSrcDirectory) {
            println "Link files directory     : " + getLinksSrcDirectory();
        } else {
            if (extLocations) {
                println "Extension Locations      : "
                getExtLocations().each { println " -> " + it }
            }
        }
        
        println ""
        println "JVM Options              : " + (jvmOptions == null ? "" : jvmOptions);
        println "Environement Config      : " + (envConfigs == null ? "" : envConfigs);
        println "Java source version      : " + (javacSource == null ? "" : javacSource);
        println "Java target version      : " + (javacTarget == null ? "" : javacTarget);
        println ""
        println "Publish directory        : " + (getPublishDirectory() == null ? "" : getPublishDirectory());
        
        if (!customValues.values().isEmpty()) {
            println "----- Additional parameters -----"
            for (Map.Entry<String, String> entry: customValues.entrySet()) {
                println " -> " + entry.getKey() + " = " + entry.getValue();
            }
        }
        println "===================================================="
    }
    
    
    public static String normPathForAnt(String path) {
        if (path == null) {
            return null;
        } else {
            return path.replace('\\', '/');
        }
    }
    
}
