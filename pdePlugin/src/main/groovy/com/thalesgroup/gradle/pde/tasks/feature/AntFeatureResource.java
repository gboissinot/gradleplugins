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

package com.thalesgroup.gradle.pde.tasks.feature;

import static com.thalesgroup.gradle.pde.tasks.Util.normPathForAnt;
import groovy.util.AntBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.thalesgroup.gradle.pde.FeaturePdeConvention;
import com.thalesgroup.gradle.pde.tasks.AntUtil;
import com.thalesgroup.gradle.pde.tasks.CleanTargetPlatformAction;
import com.thalesgroup.gradle.pde.tasks.GenerateAllElementsAction;
import com.thalesgroup.gradle.pde.tasks.ReplaceElt;

class AntFeatureResource {

    void execute(FeaturePdeConvention conv, AntBuilder ant) throws IOException {
        String base = conv.getBase();
        String baseLocation = conv.getBaseLocation();
        String buildDirectory = conv.getBuildDirectory();
        String builderDir = conv.getBuilderDir();
        String featureName = conv.getName();
        String buildId = conv.getBuildId();
        String version = conv.getJobVersion();
        String envConfigs = conv.getEnvConfigs();
        Boolean usePreviousLinks = conv.getUsePreviousLinks();
        String javacSource = conv.getJavacSource();
        String javacTarget = conv.getJavacTarget();
        String data = conv.getData();
        
        File fBuilderDir = new File(builderDir);
        fBuilderDir.mkdirs();
        
        
        new GenerateAllElementsAction().generate(builderDir, new String[]{featureName});
        
        //build.properties
        java.io.InputStream buildPropertiesIs = this.getClass().getResourceAsStream("/feature/build.properties");
        AntUtil.processResource(buildPropertiesIs, fBuilderDir, "build.properties", new ReplaceElt[]{
            new ReplaceElt("gradleFeatureName", featureName),
            new ReplaceElt("gradleBase", normPathForAnt(base)),
            new ReplaceElt("gradleBuildDirectory", normPathForAnt(buildDirectory)),
            new ReplaceElt("gradleConfigs", envConfigs),
            new ReplaceElt("gradleBuildId", buildId),
            new ReplaceElt("gradleJavacSource", javacSource),
            new ReplaceElt("gradleJavacTarget", javacTarget)
        });
        buildPropertiesIs.close();
        
        //Links directory
        if (usePreviousLinks) {
            File tempLinkDir = new File(buildDirectory, "links");
            File destLinkDir = new File(baseLocation, "links");
            for (File file : tempLinkDir.listFiles()) {
                FileInputStream fIs = new FileInputStream(file);
                AntUtil.processResource(fIs, destLinkDir, file.getName(), new ReplaceElt[]{
                     new ReplaceElt("\\#\\{version\\}", version)
                });
                fIs.close();
            }
            
            try {
                new CleanTargetPlatformAction(ant, baseLocation, buildDirectory, data).clean();
            } catch (FileNotFoundException e) {
                System.out.println("WARNING! Target Platform could not be initialized. " + e.toString());
            }
        }
        
        //customTargets.xml
        InputStream customTargetsIs = this.getClass().getResourceAsStream("/feature/customTargets.xml");
        AntUtil.copyFile(customTargetsIs, fBuilderDir, "customTargets.xml");
        customTargetsIs.close();
    }
}