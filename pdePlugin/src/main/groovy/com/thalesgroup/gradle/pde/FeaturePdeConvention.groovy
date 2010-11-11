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

public class FeaturePdeConvention {

  Project project
  Map customValues

  String timestamp
  String buildDirectory
  String builderDir
  List pluginsSrcDirList
  String featuresSrcDir
  String featureName
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

  FeaturePdeConvention(Project project, Map customValues) {
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

  public void print() {
    println "===================================================="
    println "*                PDE PARAMETERS                    *"
    println "===================================================="
    println "Feature name             : " + (featureName == null ? "" : featureName);
    println "Job version              : " + (jobVersion == null ? "" : jobVersion);
    println "Timestamp                : " + (timestamp == null ? "" : timestamp);
    println ""
    println "Build directory          : " + (buildDirectory == null ? "" : buildDirectory);
    println "Launcher Path            : " + (eclipseLauncher == null ? "" : eclipseLauncher);
    println "Launcher Plugin Version  : " + (equinoxLauncherPluginVersion == null ? "" : equinoxLauncherPluginVersion);
    println "PDE Plugin Version       : " + (pdeBuildPluginVersion == null ? "" : pdeBuildPluginVersion);
    println "Eclipse workspace (data) : " + (data == null ? "" : data);
    println ""
    println "Builder directory        : " + (getBuilderDir() == null ? "" : getBuilderDir());
    println "Target (base)            : " + (base == null ? "" : base);
    println "Target (baseLocation)    : " + (getBaseLocation() == null ? "" : getBaseLocation());

    if (linksSrcDirectory) {
      println "Link files directory     : " + linksSrcDirectory;
    } else {
      if (extLocations) {
        println "Extension Locations      : "
        extLocations.each {
          println " -> " + it
        }
      }
    }

    println ""
    println "JVM Options              : " + (jvmOptions == null ? "" : jvmOptions);
    println "Environement Config      : " + (envConfigs == null ? "" : envConfigs);
    println "Java source version      : " + (javacSource == null ? "" : javacSource);
    println "Java target version      : " + (javacTarget == null ? "" : javacTarget);
    println ""
    println "Publish directory        : " + (publishDirectory == null ? "" : publishDirectory);
    println "===================================================="

    if (!customValues.values().isEmpty()) {
      println "---------- Additional parameters"
      for (Map.Entry<String, String> entry: customValues.entrySet()) {
        println "Paraneter " + entry.getKey() + "=" + entry.getValue();
      }
    }
  }


}
