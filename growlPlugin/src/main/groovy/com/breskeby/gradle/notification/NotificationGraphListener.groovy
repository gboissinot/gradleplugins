/**
 * 
 */
package com.breskeby.gradle.notification

import org.gradle.api.execution.*

/**
 * @author Rene
 *
 */
public class NotificationGraphListener implements TaskExecutionGraphListener{
	
	NotificationPlugin plugin;
	
	public NotificationGraphListener(NotificationPlugin notifyPlugin){
		plugin = notifyPlugin;
	}
	
	public void graphPopulated(TaskExecutionGraph graph) {
		graph.getAllTasks()
	}
}
