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

import java.io.File;

import com.thalesgroup.gradle.pde.PdeConvention;

class AntPdeInit {
    
    void execute(PdeConvention conv, AntBuilder ant) 
    {
        if (conv.getUsePreviousLinks()) {
            //Create the destination links directory
            def destLinkDir = conv.getBaseLocation() + "/links"
            ant.delete(dir: destLinkDir, failonerror: false)
            ant.mkdir(dir: destLinkDir)
            
            if (conv.getLinksSrcDirectory()) {
                println "Fetching link files from ${conv.getLinksSrcDirectory()}..."
                // Copy the temp links
                ant.copy(todir: destLinkDir) {
                    fileset(dir: conv.getLinksSrcDirectory()) { include(name: '*.link') }
                }
            } else if (conv.getExtLocations()) {
                println "Generating link files..."
                for (String extLoc : conv.getExtLocations()) {
                    def linkFileName = extLoc.replaceAll("[\\\\/:]", "_")
                    linkFileName = destLinkDir + "/${linkFileName}.link"
                    ant.echo(message: "path=${extLoc}", file: linkFileName)
                    println " -> generated " + linkFileName
                }
            }
        }
        // Create the features directory and fill in
        def featuresDir = conv.getBuildDirectory() + "/features"
        println "Fetching features..."
        ant.mkdir(dir: featuresDir)
        if (conv.getFeaturesSrcDir()) {
            ant.copy(todir: featuresDir) { fileset(dir: conv.getFeaturesSrcDir()) }
        }
        
        // Create the plugins directory and fill in
        def pluginsDir = conv.getBuildDirectory() + "/plugins"
        println "Fetching plugins..."
        ant.mkdir(dir: pluginsDir)
        if (conv.getPluginsSrcDirList()) {
            ant.copy(todir: pluginsDir) {
                conv.getPluginsSrcDirList().each { fileset(dir: it) }
            }
        }
        
        //Create the publish directory
        if (conv.getPublishDirectory()) {
            println "Creating the publication directory..."
            ant.mkdir(dir: conv.getPublishDirectory())
        }
    }

    
    
}