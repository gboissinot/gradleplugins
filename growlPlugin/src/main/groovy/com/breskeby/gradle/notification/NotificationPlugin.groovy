package com.breskeby.gradle.notification

import org.gradle.api.*;
import org.gradle.api.internal.plugins.osgi.DefaultOsgiManifest;
import org.gradle.api.internal.plugins.osgi.OsgiHelper;
import org.gradle.api.plugins.BasePluginConvention;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;

import org.gradle.api.plugins.ProjectPluginsContainer;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.execution.*
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.breskeby.gradle.notification.growl.GrowlNotification

/**
 * <p>A {@link Plugin} which introduces notification support like growl, twitter or email</p>
 *
 * @author Rene Groeschke
 */
public class NotificationPlugin implements Plugin {
    
	public void use(Project project, ProjectPluginsContainer projectPluginsHandler) {
		final NotifyConfiguration config = new NotifyConfiguration()
		project.setProperty("notification", config);
		project.afterEvaluate{ proj -> 
        	proj.getBuild().getTaskGraph().whenReady {
		 		Task notifyableTask
				if(config.task){
					notifyableTask = proj.getBuild().getTaskGraph().getAllTasks().find { it.name == config.task };					
					if(!notifyableTask){
						org.slf4j.LoggerFactory.getLogger(NotificationPlugin.class).warn("Task ${config.task} not in TaskGraph")
					}
				}
				else{ //no task specified for notification. take the last one in the taskGraph
					notifyableTask = proj.getBuild().getTaskGraph().getAllTasks().last();					
				}
		 		
				if(notifyableTask){
					notifyableTask.doLast{
			 			config.growl.sendNotification(config.message ?:"${project.name} build done with task ${notifyableTask.name}")
					}	
				}
		 			
		 	}
		 	
    	}
	} 
}
