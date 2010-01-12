/*******************************************************************************
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
*******************************************************************************/

package com.thalesgroup.gradle.pde;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.Convention;
import com.thalesgroup.gradle.pde.tasks.feature.CleanFeatureTask;
import com.thalesgroup.gradle.pde.tasks.feature.DeployFeatureTask;
import com.thalesgroup.gradle.pde.tasks.feature.InitFeatureTask;
import com.thalesgroup.gradle.pde.tasks.feature.PdeFeatureTask;
import com.thalesgroup.gradle.pde.tasks.feature.ResourceFeatureTask;



public class FeaturePdeBuild implements Plugin<Project>{

    public static final String CLEAN_TASK_NAME             = "pdeClean";
    public static final String INIT_TASK_NAME              = "initPde";
    public static final String PROCESS_RESOURCES_TASK_NAME = "processPdeResources";
    public static final String PDE_BUILD_TASK_NAME         = "pdeBuild";
    public static final String UPLOAD_TASK_NAME            = "uploadPde";


    public void use(final Project project) {
	   	HashMap<String, ?> customValues = new HashMap<String,String>();
	   	FeaturePdeConvention featurePdeConvention = new FeaturePdeConvention(project, customValues);
        Convention convention = project.getConvention();
        convention.getPlugins().put("featurePde", featurePdeConvention);

        project.setProperty("featurePde", featurePdeConvention);
        configureClean(project,customValues);
        configureInit(project,customValues);
        configureProcessResources(project,customValues);
        configurePdeBuild(project,customValues);
        configureDeploy(project,customValues);
	}

    private void configureClean(Project project, final Map<String, ?> customValues) {
	
	project.getTasks().withType(CleanFeatureTask.class).allTasks(new Action<CleanFeatureTask>() {
            public void execute(CleanFeatureTask task) {
		task.setCustomValues(customValues);
            }
        });
        project.getTasks().add(CLEAN_TASK_NAME, CleanFeatureTask.class).setDescription("Cleanning...");
    }


    private void configureInit(Project project, final Map<String, ?> customValues) {
	
	project.getTasks().withType(InitFeatureTask.class).allTasks(new Action<InitFeatureTask>() {
            public void execute(InitFeatureTask task) {
		task.setCustomValues(customValues);
            }
        });
        project.getTasks().add(INIT_TASK_NAME, InitFeatureTask.class).setDescription("Initialization...");
    }


    private void configureProcessResources(Project project, final Map<String, ?> customValues) {


	project.getTasks().withType(ResourceFeatureTask.class).allTasks(new Action<ResourceFeatureTask>() {
            public void execute(ResourceFeatureTask task) {
		task.dependsOn(INIT_TASK_NAME);
		task.setCustomValues(customValues);
            }
        });
        project.getTasks().add(PROCESS_RESOURCES_TASK_NAME, ResourceFeatureTask.class).setDescription("Process resources.");
    }



   private void configurePdeBuild(final Project project, final Map<String, ?> customValues) {
	project.getTasks().withType(PdeFeatureTask.class).allTasks(new Action<PdeFeatureTask>() {
            public void execute(PdeFeatureTask pdeTask) {
		pdeTask.dependsOn(PROCESS_RESOURCES_TASK_NAME);
		pdeTask.setCustomValues(customValues);
            }
        });
        project.getTasks().add(PDE_BUILD_TASK_NAME, PdeFeatureTask.class).setDescription("Launch PDE.");	
   }


   private void configureDeploy(Project project, final Map<String, ?> customValues) {
	project.getTasks().withType(DeployFeatureTask.class).allTasks(new Action<DeployFeatureTask>() {
            public void execute(DeployFeatureTask task) {
		task.setCustomValues(customValues);
		task.dependsOn(PDE_BUILD_TASK_NAME);
            }
        });
        project.getTasks().add(UPLOAD_TASK_NAME, DeployFeatureTask.class).setDescription("Deploying...");
   }
}

