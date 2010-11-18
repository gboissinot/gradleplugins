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
import java.io.FileNotFoundException;

import com.thalesgroup.gradle.pde.PdeConvention;

class AntPdeClean {
    
    void execute(PdeConvention conv, AntBuilder ant) {
        println "Deleting the build directory..."
        //delete the working directory
        ant.delete(dir: conv.getBuildDirectory());
        
        println "Creating the build directory..."
        ant.mkdir(dir: conv.getBuildDirectory())
        
        println "Creating the pde eclipse workspace..."
        ant.mkdir(dir: conv.getData())
        
        if (conv.getUsePreviousLinks()) {
            def destLinkDir = new File(conv.getBaseLocation(), "links");
            
            println "Deleting old link files..."
            ant.delete(dir: destLinkDir);
            ant.mkdir(dir: destLinkDir);
            
            def rcpcleaner = "R:/extloc/platform-3.3/rcpcleaner";
            ant.echo(message: "path=${rcpcleaner}", file: "${destLinkDir}/org.thalesgroup.rcpcleaner.link");
            
            try {
                new CleanTargetPlatformAction(ant, conv.getBaseLocation(), conv.getBuildDirectory(), 
                        conv.getData()).clean();
            } catch (FileNotFoundException e) {
                println "WARNING! Target Platform could not be cleaned. " + e.toString();
            }
        }
    }
}