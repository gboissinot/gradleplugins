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

package com.thalesgroup.gradle.pde.tasks.product;

class AntProductPde {

  void execute(String eclipseLauncher, String baseLocation, String equinoxLauncherPluginVersion,
               String buildDirectory, String builderDir,
               String timestamp, String pdeBuildPluginVersion, AntBuilder ant) {

    buildDirectory = buildDirectory.replace('\\', '/')
    builderDir = builderDir.replace('\\', '/')

    StringBuffer commandLine = new StringBuffer();
    commandLine.append("java -jar ${eclipseLauncher}/plugins/org.eclipse.equinox.launcher_${equinoxLauncherPluginVersion}.jar")
    commandLine.append(" -application")
    commandLine.append(" org.eclipse.ant.core.antRunner")
    commandLine.append(" -buildfile")
    commandLine.append(" ${eclipseLauncher}/plugins/org.eclipse.pde.build_${pdeBuildPluginVersion}/scripts/productBuild/productBuild.xml")
    commandLine.append(" -Dbuilder=${builderDir}")
    commandLine.append(" -DbaseLocation=${baseLocation}")
    commandLine.append(" -Dtimestamp=${timestamp}")

    ant.echo(message: "[PDE Command line] - " + commandLine)
    ant.echo(message: "Building...")
    ant.java(
            classname: "org.eclipse.equinox.launcher.Main",
            fork: "true",
            failonerror: "true") {
      arg(value: "-application")
      arg(value: "org.eclipse.ant.core.antRunner")
      arg(value: "-buildfile")
      arg(value: "${eclipseLauncher}/plugins/org.eclipse.pde.build_${pdeBuildPluginVersion}/scripts/productBuild/productBuild.xml")
      arg(value: "-Dbuilder=${builderDir}")
      arg(value: "-DbaseLocation=${baseLocation}")
      arg(value: "-Dtimestamp=${timestamp}")
      classpath {
        pathelement(location: "${eclipseLauncher}/plugins/org.eclipse.equinox.launcher_${equinoxLauncherPluginVersion}.jar")
      }
    }
  }
}