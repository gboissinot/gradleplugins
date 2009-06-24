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

package com.thalesgroup.gradle.pde.tasks.product;


import org.gradle.api.*;
import org.gradle.api.tasks.util.ExistingDirsFilter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.gradle.pde.tasks.CommonTask;
import com.thalesgroup.gradle.pde.ProductPdeConvention;


public class PdeProductTask extends CommonTask {

  private static Logger logger = LoggerFactory.getLogger(PdeProductTask.class);

  public PdeProductTask(final Project project, String name) {
	super(project, name);
        setActions(new ArrayList<TaskAction>());
        doFirst(new TaskAction() {
           public void execute(Task task) {
              generate(project, task);
           }
        });
  }

  protected void generate(Project project, Task task) {

     ProductPdeConvention productPdeConvention = productPde(project.getConvention());

     new AntProductPde().execute( 
				productPdeConvention.getEclipseLocation(),
				productPdeConvention.getBaseLocation(),
				productPdeConvention.getEquinoxLauncherPluginVersion(),
				productPdeConvention.getBuildDirectory(),
				productPdeConvention.getBuilderDir(),
				productPdeConvention.getTimestamp(),
				productPdeConvention.getPdeBuildPluginVersion(),                                                                 
				getAnt());
  }

}