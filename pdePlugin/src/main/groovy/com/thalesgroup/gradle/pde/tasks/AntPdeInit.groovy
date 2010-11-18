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

import static com.thalesgroup.gradle.pde.tasks.Util.convertPathForOs;
import static com.thalesgroup.gradle.pde.tasks.Util.normPathForAnt;

class AntPdeInit {
    
    void execute(String buildDirectory,
                 String builderDir,
                 List pluginsSrcDirList,
                 String featuresSrcDir,
                 String publishDirectory,
                 String baseLocation,
                 String linksSrcDirectory,
                 List extensionLocations,
                 Boolean usePreviousLinks,
                 String data,
                 AntBuilder ant) 
    {

        if (usePreviousLinks) {
            //Create the destination dropins directory
            def destLinkDir = baseLocation + "/links"
            ant.delete(dir: destLinkDir, failonerror: false)
            ant.mkdir(dir: destLinkDir)
            
            //Create the temporary links directory
            def tempLinkDir = buildDirectory + "/links"
            ant.mkdir(dir: tempLinkDir)
            
            if (linksSrcDirectory) {
                println "Fetching link files from ${linksSrcDirectory}..."
                // Copy the temp links
                ant.copy(todir: tempLinkDir) {
                    fileset(dir: linksSrcDirectory) { include(name: '*.link') }
                }
            } else if (extensionLocations) {
                println "Generating link files..."
                for (String extLoc : extensionLocations) {
                    def linkFileName = extLoc.replaceAll("[\\\\/:]", "_")
                    linkFileName = tempLinkDir + "/${linkFileName}.link"
                    ant.echo(message: "path=${extLoc}", file: linkFileName)
                    println " -> generated " + linkFileName
                }
            }
        }
        // Create the features directory and fill in
        def featuresDir = buildDirectory + "/features"
        println "Fetching features..."
        ant.mkdir(dir: featuresDir)
        if (featuresSrcDir) {
            ant.copy(todir: featuresDir) { fileset(dir: featuresSrcDir) }
        }
        
        // Create the plugins directory and fill in
        def pluginsDir = buildDirectory + "/plugins"
        println "Fetching plugins..."
        ant.mkdir(dir: pluginsDir)
        if (!pluginsSrcDirList.isEmpty()) {
            ant.copy(todir: pluginsDir) {
                pluginsSrcDirList.each { fileset(dir: it) }
            }
        }
        
        //Create the publish directory
        if (publishDirectory) {
            println "Creating the publication directory..."
            ant.mkdir(dir: publishDirectory)
        }
    }

    
    
}