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


import org.gradle.api.Project

public class ProductPdeConvention {

  Project project
  Map customValues

  String timestamp;
  String buildDirectory
  String builderDir
  List pluginsSrcDirList
  String featuresSrcDir
  String productName
  String eclipseLauncher
  String pdeBuildPluginVersion
  String equinoxLauncherPluginVersion
  String base
  String baseLocation
  String linksSrcDirectory
  List extLocations
  String buildId
  String publishDirectory
  String jobVersion
  String envConfigs = "*, *, *"
  Boolean usePreviousLinks
  String jvmOptions = "-Xms128m -Xmx512m -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"
  String javacSource = 1.5
  String javacTarget = 1.5
  String data = "eclipsews"
  String eclipseExtensionsRoot = eclipseExtensionsRoot

  String additionalPropertiesPath

  ProductPdeConvention(Project project, Map customValues) {
    this.project = project
    this.customValues = customValues;
  }

  public String getBaseLocation() {
    if (baseLocation == null) {
      baseLocation = base + "/eclipse"
    }
    return baseLocation
  }


  public String getBuilderDir() {
    if (builderDir == null) {
      builderDir = "${buildDirectory}/pdebuild"
    }
    return builderDir
  }  

  public String toString() {

    println "-----------------------------------------------------"
    println "                 INPUTS                              "
    println "Build directory                          :" + builderDir;
    println ""
    println "Eclipse Launcher Path                    :" + eclipseLauncher;
    println "Eclipse Laucher Plugin Version           :" + equinoxLauncherPluginVersion;
    println "PDE Plugin Version                       :" + pdeBuildPluginVersion;
    println ""
    println "Eclipse Target platform   (base)         :" + base
    println "Eclipse Target platform   (baseLocation) :" + baseLocation

    println "Timestamp                                :" + timestamp;
    println "Builder directory                        :" + builderDir;

    println "Extensions Location Root                 :" + eclipseExtensionsRoot;
    println "Extensions List                          :"
    extLocations.each {
      println "--->" + it
    }

    println "Data directory                           :" + data;
    println "JVM Options                              :" + jvmOptions;
    println "Environement Configuration               :" + envConfigs;
    println "Java source version                      :" + javacSource;
    println "Java target version                      :" + javacTarget;
    println ""
    println ""
    println "Product name                             :" + productName;
    println "Plugin Source                            :"
    pluginsSrcDirList.each {
      println "--->" + it
    }
    println "Job version                              :" + jobVersion;
    println "Publish directory                        :" + publishDirectory

    //Built from the given property file
    //The properties are added at the end of the command line
    //The command line properties override the default properties from the file
    if (!customValues.values().isEmpty()) {
      println "---------- Additional parameters"
      for (Map.Entry<String, String> entry: customValues.entrySet()) {
        args << "-D" + entry.getKey() + "=" + entry.getValue()
      }
    }

  }
}
