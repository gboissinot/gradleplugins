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

package com.thalesgroup.gradle.pde.tasks.product

import com.thalesgroup.gradle.pde.ProductPdeConvention
import org.gradle.api.GradleException

class AntProductPde {
    
    void execute(ProductPdeConvention productPdeConvention, 
                 Map<String, Object> customValues,
                 AntBuilder ant) 
    {
        String eclipseLauncher = productPdeConvention.getEclipseLauncher().replace('\\', '/')
        String equinoxLauncherPluginVersion = productPdeConvention.getEquinoxLauncherPluginVersion()
        String buildDirectory = productPdeConvention.getBuildDirectory().replace('\\', '/')
        String builderDir = productPdeConvention.getBuilderDir().replace('\\', '/')
        String timestamp = productPdeConvention.getTimestamp()
        String pdeBuildPluginVersion = productPdeConvention.getPdeBuildPluginVersion()
        List extLocations = productPdeConvention.getExtLocations()
        String jvmOptions = productPdeConvention.getJvmOptions()
        String data = productPdeConvention.getData()
        
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
        
        args << "-Dbuilder=${builderDir}"
        args << "-Dtimestamp=${timestamp}"
        
        //----------  Build the pluginPath
        if (extLocations) {
            String pluginPath = extLocations.join(File.pathSeparator)
            args << "-DpluginPath=${pluginPath}"
        }
        
        //Built from the given property file
        //The properties are added at the end of the command line
        //The command line properties override the default properties from the file
        if (!customValues.values().isEmpty()) {
            for (Map.Entry<String, String> entry: customValues.entrySet()){
                args << "-D" + entry.getKey() + "=" + entry.getValue()
            }
        }
        
        args << "-data \"${data}\""
        
        
        String eclipseCommand = args.join(" ")
        
        println "[PDE Command line] java $eclipseCommand"
        println "Building in ${buildDirectory} ..."
        ant.exec(executable: "java", dir: buildDirectory, failonerror: true) { 
            arg(line: eclipseCommand) 
        }
    }
}