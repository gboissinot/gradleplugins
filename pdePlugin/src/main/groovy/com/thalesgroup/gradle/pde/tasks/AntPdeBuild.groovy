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

package com.thalesgroup.gradle.pde.tasks

import groovy.util.AntBuilder;

import java.util.Map;

import com.thalesgroup.gradle.pde.BuildType;
import com.thalesgroup.gradle.pde.PdeConvention;
import com.thalesgroup.gradle.pde.ProductPdeConvention;

class AntPdeBuild {

    void execute(PdeConvention conv, AntBuilder ant) {
        
        String launcher = conv.getEclipseLauncher()

        List args = new ArrayList();
        
        args << conv.getJvmOptions();
        
        if (conv.getEquinoxLauncherPluginVersion()) {
            args << "-jar \"${launcher}/plugins/org.eclipse.equinox.launcher_${conv.getEquinoxLauncherPluginVersion()}.jar\"";
        } else {
            args << "-jar \"${launcher}/plugins/org.eclipse.equinox.launcher.jar\"";
        }
        
        args << "-application org.eclipse.ant.core.antRunner"
        
        def scriptsDir;
        
        if (conv.getPdeBuildPluginVersion()) {
            scriptsDir = "${launcher}/plugins/org.eclipse.pde.build_${conv.getPdeBuildPluginVersion()}/scripts"
        } else {
            scriptsDir = "${launcher}/plugins/org.eclipse.pde.build/scripts"
        }
        
        if (conv.getType() == BuildType.product) {
            args << "-buildfile \"${scriptsDir}/productBuild/productBuild.xml\""
        } else {
            args << "-buildfile \"${scriptsDir}/build.xml\""
        }
        
        args << "-DbuildDirectory=\"${conv.getBuildDirectory()}\""
        args << "-Dbuilder=\"${conv.getBuilderDir()}\""

        args << "-Dbase=\"${conv.getBase()}\""
        args << "-DbaseLocation=\"${conv.getBaseLocation()}\""
        
        args << "-DbuildId=\"${conv.getBuildId()}\""
        
        
        if (conv.getType() == BuildType.product) {
            def productFile = ((ProductPdeConvention) conv).getProductFile()
            args << "-Dproduct=\"${productFile}\""
        }
        
        //----------  Build the pluginPath
        if (conv.getExtLocations() && !conv.getUsePreviousLinks()) {
            String pluginPath = conv.getExtLocations().join(File.pathSeparator)
            args << "-DpluginPath=\"${pluginPath}\""
        }
        
        //Built from the given property file
        //The properties are added at the end of the command line
        //The command line properties override the default properties from the file
        if (conv.getAdditionalProperties() != null && !conv.getAdditionalProperties().values().isEmpty()) {
            for (Map.Entry<String, Object> entry: conv.getAdditionalProperties().entrySet()){
                args << "-D${entry.getKey()}=\"${entry.getValue()}\"";
            }
        }
        
        //-- Data directory
        args << "-data \"${conv.getData()}\""
        
        String eclipseCommand = args.join(" ")
        
        println "[PDE Command line] java $eclipseCommand"
        println "Building in ${conv.getBuildDirectory()} ..."
        ant.exec(executable: "java", dir: conv.getBuildDirectory(), failonerror: true) {
            arg(line: eclipseCommand)
        }
    }
}
