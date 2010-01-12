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

import com.thalesgroup.gradle.pde.tasks.AntUtil
import com.thalesgroup.gradle.pde.tasks.ReplaceElt;

class AntFeatureResource {

  void execute(String base,
               String buildDirectory,
               String builderDir,
               String featureName,
               String buildId,
               String baseLocation,
               String version,
               String envConfigs,
               AntBuilder ant) {


    println("execute AntFeatureResource ")
    File fBuilderDir = new File(builderDir);
    fBuilderDir.mkdirs();

    buildDirectory = buildDirectory.replace('\\', '/')
    baseLocation = baseLocation.replace('\\', '/')

    //allElements.xml
    java.io.InputStream allElementsIs = this.getClass().getResourceAsStream("/feature/allElements.xml");
    AntUtil.processResource(allElementsIs, fBuilderDir, "allElements.xml", [new ReplaceElt("gradleFeatureName", featureName)]);
    allElementsIs.close();

    //build.properties
    java.io.InputStream buildPropertiesIs = this.getClass().getResourceAsStream("/feature/build.properties");
    AntUtil.processResource(buildPropertiesIs, fBuilderDir, "build.properties", [new ReplaceElt("gradleFeatureName", featureName),
            new ReplaceElt("gradleBase", base), new ReplaceElt("gradleBuildDirectory", buildDirectory),
            new ReplaceElt("gradleConfigs", envConfigs), new ReplaceElt("gradleBuildId", buildId)]);
    buildPropertiesIs.close();

    //Links directory
    def tempLinkDir = new File(buildDirectory + "/dropins")
    def destLinkDir = new File(baseLocation, "dropins");
    tempLinkDir.listFiles().each {File file ->
      FileInputStream fIs = new FileInputStream(file)
      AntUtil.processResource(fIs, destLinkDir, file.getName(), [new ReplaceElt("\\#\\{version\\}", version)]);
      fIs.close();
    }

    //customTargets.xml
    java.io.InputStream customTargetsIs = this.getClass().getResourceAsStream("/feature/customTargets.xml");
    AntUtil.copyFile(customTargetsIs, fBuilderDir, "customTargets.xml")
    customTargetsIs.close();
  }
}