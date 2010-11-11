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


package com.thalesgroup.gradle.pde.tasks.feature

import com.thalesgroup.gradle.pde.FeaturePdeConvention
import org.gradle.api.GradleException

class AntFeaturePde {

  void execute(FeaturePdeConvention featurePdeConvention,
               Map<String, Object> customValues,
               AntBuilder ant) {

    String eclipseLauncher = featurePdeConvention.getEclipseLauncher()
    String equinoxLauncherPluginVersion = featurePdeConvention.getEquinoxLauncherPluginVersion()
    String buildDirectory = featurePdeConvention.getBuildDirectory()
    String builderDir = featurePdeConvention.getBuilderDir()
    String timestamp = featurePdeConvention.getTimestamp()
    String pdeBuildPluginVersion = featurePdeConvention.getPdeBuildPluginVersion()
    List extLocations = featurePdeConvention.getExtLocations()
    String jvmOptions = featurePdeConvention.getJvmOptions()
    String data = featurePdeConvention.getData()
    String eclipseExtensionsRoot = featurePdeConvention.getEclipseExtensionsRoot()

    buildDirectory = buildDirectory.replace('\\', '/')
    builderDir = builderDir.replace('\\', '/')

    def dataDirectory = buildDirectory + "/" + data
    ant.mkdir(dir: dataDirectory)

    List args = new ArrayList()
    args << "${jvmOptions}"
    if (equinoxLauncherPluginVersion) {
      args << "-jar \"${eclipseLauncher}/plugins/org.eclipse.equinox.launcher_${equinoxLauncherPluginVersion}.jar\""
    } else {
      args << "-jar \"${eclipseLauncher}/plugins/org.eclipse.equinox.launcher.jar\""
    }

    args << "-application org.eclipse.ant.core.antRunner"
    if (pdeBuildPluginVersion) {
      args << "-buildfile \"${eclipseLauncher}/plugins/org.eclipse.pde.build_${pdeBuildPluginVersion}/scripts/build.xml\""
    } else {
      args << "-buildfile \"${eclipseLauncher}/plugins/org.eclipse.pde.build/scripts/build.xml\""
    }
    args << "-Dtimestamp=${timestamp}"
    args << "-Dbuilder=\"${builderDir}\""

    //----------  Build the pluginPath
    if (extLocations) {
      List pluginPathArgs = new ArrayList();
      extLocations.each {String pluginPath ->
        if (!(new File(pluginPath).exists())) {
          pluginPath = eclipseExtensionsRoot + "/" + pluginPath
          if (!(new File(pluginPath).exists())) {
            throw new GradleException(pluginPath + " do not exist.")
          }
        }
        pluginPathArgs << pluginPath + "/eclipse"
      }
      String pluginPath = pluginPathArgs.join(File.pathSeparator)
      args << "-DpluginPath=${pluginPath}"
    }

    //Built from the given property file
    //The properties are added at the end of the command line
    //The command line properties override the default properties from the file
    if (!customValues.values().isEmpty()) {
      println "---------- Additional parameters"
      for (Map.Entry<String, String> entry: customValues.entrySet()){
        args << "-D" + entry.getKey() + "=" + entry.getValue()
      }
    }

    //-- Data directory
    args << "-data \"${dataDirectory}\""


    String eclipseCommand = args.join(" ")

    ant.echo(message: "[PDE Command line] - java " + eclipseCommand)
    ant.echo(message: "Building in ${buildDirectory} ...")
    ant.exec(executable: "java", dir: buildDirectory, failonerror: true) {
      arg(line: eclipseCommand)
    }
  }

}