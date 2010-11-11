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

class AntFeatureDeploy {

  void execute(FeaturePdeConvention featurePdeConvention,
               AntBuilder ant) {

    String featureName = featurePdeConvention.getFeatureName()
    String buildId = featurePdeConvention.getBuildId()
    String buildDirectory = featurePdeConvention.getBuildDirectory()
    String publishDirectory = featurePdeConvention.getPublishDirectory()

    //delete the publish directory
    ant.delete(dir: publishDirectory)

    def zipFileName = buildDirectory + "/I.$buildId" + "/$featureName-$buildId" + ".zip"
    println "Deleting ${zipFileName} file..."
    ant.unzip(dest: publishDirectory, src: zipFileName)

  }


}