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
import com.thalesgroup.gradle.pde.tasks.AntUtil
import com.thalesgroup.gradle.pde.tasks.ReplaceElt
import static com.thalesgroup.gradle.pde.tasks.Util.normPathForAnt;

class AntProductResource {
    
    void execute(ProductPdeConvention productPdeConvention, AntBuilder ant) {
        String base = productPdeConvention.getBase()
        String buildDirectory = productPdeConvention.getBuildDirectory()
        String builderDir = productPdeConvention.getBuilderDir()
        String productName = productPdeConvention.getName()
        String buildId = productPdeConvention.getBuildId()
        String baseLocation = productPdeConvention.getBaseLocation()
        String version = productPdeConvention.getJobVersion()
        String envConfigs = productPdeConvention.getEnvConfigs()
        Boolean usePreviousLinks = productPdeConvention.getUsePreviousLinks()
        String javacSource = productPdeConvention.getJavacSource()
        String javacTarget = productPdeConvention.getJavacTarget()
        
        File fBuilderDir = new File(builderDir);
        fBuilderDir.mkdirs();
        
        buildDirectory = buildDirectory.replace('\\', '/')
        baseLocation = baseLocation.replace('\\', '/')
        productName = productName.replace('\\', '/')
        
        //build.properties
        java.io.InputStream buildPropertiesIs = this.getClass().getResourceAsStream("/product/build.properties");
        AntUtil.processResource(buildPropertiesIs, fBuilderDir, "build.properties", [
            new ReplaceElt("gradleProductName", productName),
            new ReplaceElt("gradleEclipseBase", normPathForAnt(base)),
            new ReplaceElt("gradleBuildDirectory", normPathForAnt(buildDirectory)),
            new ReplaceElt("gradleBuildId", buildId),
            new ReplaceElt("gradleConfigs", envConfigs),
            new ReplaceElt("gradleJavacSource", javacSource),
            new ReplaceElt("gradleJavacTarget", javacTarget)
        ]);
        buildPropertiesIs.close();
        
        //Links directory
        if (usePreviousLinks) {
            def tempLinkDir = new File(buildDirectory, "links")
            def destLinkDir = new File(baseLocation, "links");
            tempLinkDir.listFiles().each {File file ->
                FileInputStream fIs = new FileInputStream(file)
                AntUtil.processResource(fIs, destLinkDir, file.getName(), [
                    new ReplaceElt("\\#\\{version\\}", version)
                ]);
                fIs.close();
            }
        }
    }
}