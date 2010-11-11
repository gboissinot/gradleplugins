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

class AntPdeInit {

  void execute(String buildDirectory,
               String builderDir,
               List pluginsSrcDirList,
               String featuresSrcDir,
               String publishDirectory,
               String baseLocation,
               String linksSrcDirectory,
               Boolean usePreviousLinks,
               AntBuilder ant) {


    //Create the build directory
    ant.echo(message: 'Create the build directory')
    ant.mkdir(dir: buildDirectory)

    if (linksSrcDirectory) {

      def linkDirName = usePreviousLinks ? "links" : "dropins"

      //Create the destination dropins directory
      def destLinkDir = baseLocation + "/" + linkDirName
      ant.delete(dir: destLinkDir, failonerror: 'false')
      ant.mkdir(dir: destLinkDir)

      ant.echo(message: 'Copy the "+ linkDirName +" directory')

      //Create the temporary links directory
      def tempLinkDir = buildDirectory + "/" + linkDirName
      ant.mkdir(dir: tempLinkDir)

      // Copy the temp links
      ant.copy(todir: tempLinkDir) {
        fileset(dir: linksSrcDirectory) {
          include(name: '*.link')
        }
      }
    }

    // Create the features directory and fill in
    def featuresDir = buildDirectory + "/features"
    ant.echo(message: 'Create the features directory')
    ant.mkdir(dir: featuresDir)
    if (featuresSrcDir) {
      ant.copy(todir: featuresDir) {
        fileset(dir: featuresSrcDir)
      }
    }

    // Create the plugins directory and fill in
    def pluginsDir = buildDirectory + "/plugins"
    ant.echo(message: 'Create the plugins directory')
    ant.mkdir(dir: pluginsDir)
    if (!pluginsSrcDirList.isEmpty()) {
      ant.copy(todir: pluginsDir) {
        pluginsSrcDirList.each {
          fileset(dir: it)
        }
      }
    }

    //Create the publish directory
    if (publishDirectory) {
      ant.echo(message: 'Create the publication directory')
      ant.mkdir(dir: publishDirectory)
    }
  }
}