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

package com.thalesgroup.gradle.pde.tasks;

import groovy.util.AntBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thalesgroup.gradle.pde.BuildType;
import com.thalesgroup.gradle.pde.FeaturePdeConvention;
import com.thalesgroup.gradle.pde.PdeConvention;

public class AntPdeResources {
    
    public void execute(PdeConvention conv, AntBuilder ant) throws IOException {
        String buildDirectory = conv.getBuildDirectory();

        File fBuilderDir = new File(conv.getBuilderDir());
        fBuilderDir.mkdirs();

        if (conv.getType() == BuildType.feature) {
            String[] features = ((FeaturePdeConvention) conv).getFeatures();
            new GenerateAllElementsAction().generate(conv.getBuilderDir(), features);
        }

        // build.properties
        InputStream buildPropertiesIs;
        if (conv.getBuildPropertiesFile() != null) {
            // use the user's file
            buildPropertiesIs = new FileInputStream(conv.getBuildPropertiesFile());
        } else {
            // use the default embedded file
            buildPropertiesIs = this.getClass().getResourceAsStream("/build.properties");
        }
        AntUtil.copyFile(buildPropertiesIs, fBuilderDir, "build.properties");
        buildPropertiesIs.close();

        // Init target platform
        if (conv.getUsePreviousLinks()) {
            try {
                new CleanTargetPlatformAction(ant, conv.getBaseLocation(), buildDirectory, conv
                        .getData()).clean();
            } catch (Exception e) {
                System.out.println("WARNING! Target Platform could not be initialized. "
                        + e.toString());
            }
        }

        // customTargets.xml
        InputStream customTargetsIs = this.getClass().getResourceAsStream(
                "/customTargets.xml");
        AntUtil.copyFile(customTargetsIs, fBuilderDir, "customTargets.xml");
        customTargetsIs.close();
    }
}
