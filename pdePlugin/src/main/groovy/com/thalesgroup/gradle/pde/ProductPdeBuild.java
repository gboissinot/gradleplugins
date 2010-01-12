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

import com.thalesgroup.gradle.pde.tasks.product.CleanProductTask;
import com.thalesgroup.gradle.pde.tasks.product.DeployProductTask;
import com.thalesgroup.gradle.pde.tasks.product.InitProductTask;
import com.thalesgroup.gradle.pde.tasks.product.PdeProductTask;
import com.thalesgroup.gradle.pde.tasks.product.ResourceProductTask;



public class ProductPdeBuild implements Plugin<Project> {

    public static final String CLEAN_TASK_NAME             = "pdeClean";
    public static final String INIT_TASK_NAME              = "initPde";
    public static final String PROCESS_RESOURCES_TASK_NAME = "processPdeResources";
    public static final String PDE_BUILD_TASK_NAME         = "pdeBuild";
    public static final String UPLOAD_TASK_NAME            = "uploadPde";

    public void use(final Project project) {
	   	HashMap<String, String> customValues = new HashMap<String,String>();
	   	//project.setProperty("notification", config);
		
	   	ProductPdeConvention productPdeConvention = new ProductPdeConvention(project, customValues);
        Convention convention = project.getConvention();
        convention.getPlugins().put("productPde", productPdeConvention);

        project.setProperty("productPde", productPdeConvention);
        configureClean(project, customValues);
        configureInit(project,customValues);
        configureProcessResources(project,customValues);
        configurePdeBuild(project,customValues);
        configureDeploy(project,customValues);
	
   }

   private void configureClean(Project project, final Map<String, ?> customValues) {
	
    	project.getTasks().withType(CleanProductTask.class).allTasks(new Action<CleanProductTask>() {
    			public void execute(CleanProductTask task) {
   					task.setCustomValues(customValues);
    			}
    	});
        project.getTasks().add(CLEAN_TASK_NAME, CleanProductTask.class).setDescription("Cleanning...");
    }


	private void configureInit(Project project,
			final Map<String, ?> customValues) {

		project.getTasks().withType(InitProductTask.class).allTasks(
				new Action<InitProductTask>() {
					public void execute(InitProductTask task) {
						task.setCustomValues(customValues);
					}
				});
		project.getTasks().add(INIT_TASK_NAME, InitProductTask.class).setDescription("Initialization...");
	}


    private void configureProcessResources(Project project, final Map<String, ?> customValues) {


	project.getTasks().withType(ResourceProductTask.class).allTasks(new Action<ResourceProductTask>() {
            public void execute(ResourceProductTask task) {
		task.dependsOn(INIT_TASK_NAME);
		task.setCustomValues(customValues);
            }
        });
        project.getTasks().add(PROCESS_RESOURCES_TASK_NAME, ResourceProductTask.class).setDescription("Process resources.");
    }



   private void configurePdeBuild(final Project project, final Map<String, ?> customValues) {
	project.getTasks().withType(PdeProductTask.class).allTasks(new Action<PdeProductTask>() {
            public void execute(PdeProductTask pdeTask) {
		pdeTask.dependsOn(PROCESS_RESOURCES_TASK_NAME);
		pdeTask.setCustomValues(customValues);
            }
        });
        project.getTasks().add(PDE_BUILD_TASK_NAME, PdeProductTask.class).setDescription("Launch PDE.");	
   }


   private void configureDeploy(Project project, final Map<String, ?> customValues) {
	project.getTasks().withType(DeployProductTask.class).allTasks(new Action<DeployProductTask>() {
            public void execute(DeployProductTask task) {
		task.setCustomValues(customValues);
		task.dependsOn(PDE_BUILD_TASK_NAME);
            }
        });
        project.getTasks().add(UPLOAD_TASK_NAME, DeployProductTask.class).setDescription("Deploying...");
   }

  
}

