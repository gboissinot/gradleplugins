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




import groovy.util.XmlSlurper;

import org.gradle.api.GradleException;
import org.gradle.api.Project

public abstract class PdeConvention {
    
    // gradle fields
    Project project;

    // mandatory plugin fields
    List<String> pluginsSrcDirList;
    List<String> featuresSrcDirList;
    String eclipseLauncher;
    String publishDirectory;
    
    // optional plugin fields
    String pdeBuildPluginVersion;
    String equinoxLauncherPluginVersion;
    Boolean usePreviousLinks = false;
    List<String> extLocations;
    String linksSrcDirectory;
    Map additionalProperties = new HashMap<String, Object>();
    String buildPropertiesFile;
    String rcpCleaner = rcpCleaner;
    String eclipseExtensionsRoot = eclipseExtensionsRoot;
    String data;
    String jvmOptions = "-Xms128m -Xmx512m -XX:MaxPermSize=256m";
    String targetFile;

    // mandatory PDE properties
    String base;
    String builderDir;
    String baseLocation;
    String buildDirectory;
    String buildId = "BUILD_ID";
    
    
    public PdeConvention(Project project) {
        this.project = project;
    }
    
    public abstract BuildType getType();
    
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
    
    public List<String> getTargetPaths() {
        List<String> paths = new ArrayList<String>();
        
        if (this.targetFile != null) {
            def content = new File(this.targetFile).text
            def target = new XmlSlurper().parseText(content)
            def locs = target.locations.location
            locs.each() {
                def path = it.@path.text()
                if (!new File(path).exists()) {
                    throw new GradleException("ERROR in ${this.targetFile}: ${path} does not exist.")
                }
                paths << normPathForAnt(path)
            }
        }
        return paths
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
    
    public String getBaseLocation() {
        if (additionalProperties.get("baseLocation")) {
            baseLocation = additionalProperties.get("baseLocation")
            additionalProperties.remove("baseLocation")
        } else if (baseLocation == null) {
            baseLocation = base + "/eclipse"
        }
        return normPathForAnt(baseLocation)
    }
    
    
    public String getBuilderDir() {
        if (additionalProperties.get("builderDir")) {
            builderDir = additionalProperties.get("builderDir");
            additionalProperties.remove("builderDir")
        } else if (builderDir == null) {
            builderDir = buildDirectory + "/builder"
        }
        return normPathForAnt(builderDir)
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
        if (data == null) {
            data = "eclipsews"
        }
        if (buildDirectory) {
            return normPathForAnt(buildDirectory + "/" + data)
        } else {
            return normPathForAnt(data)
        }
    }
    
    public String getBuildId() {
        if (additionalProperties.get("buildId")) {
            buildId = additionalProperties.get("buildId");
            additionalProperties.remove("buildId")
        }
        return buildId
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
        printBuiltElements();
        println "Build directory         : " + (getBuildDirectory() == null ? "" : getBuildDirectory());
        println "Launcher Path           : " + (getEclipseLauncher() == null ? "" : getEclipseLauncher());
        println "Launcher Plugin Version : " + (equinoxLauncherPluginVersion == null ? "" : equinoxLauncherPluginVersion);
        println "PDE Plugin Version      : " + (pdeBuildPluginVersion == null ? "" : pdeBuildPluginVersion);
        println "Eclipse workspace       : " + (getData() == null ? "" : getData());
        println "Target Platform         : " + (getBase() == null ? "" : getBase());
        
        if (linksSrcDirectory) {
            println "Link files directory    : " + getLinksSrcDirectory();
        } else {
            if (targetFile) {
                println "Target Platform File    : ${targetFile}"
                getTargetPaths().each { println " -> " + it }
            }
            if (extLocations) {
                println "Extension Locations     : "
                getExtLocations().each { println " -> " + it }
            }
        }
        
        println "JVM Options             : " + (jvmOptions == null ? "" : jvmOptions);
        println "Publish directory       : " + (getPublishDirectory() == null ? "" : getPublishDirectory());
        
        if (additionalProperties != null && !additionalProperties.values().isEmpty()) {
            println "----- Additional parameters -----"
            for (Map.Entry<String, String> entry: additionalProperties.entrySet()) {
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
