/**
 * 
 */
package com.breskeby.gradle.notification


import org.gradle.api.Task
import com.breskeby.gradle.notification.growl.GrowlNotification
import com.breskeby.gradle.notification.AbstractNotificationTask
/**
 * @author Rene
 *
 */
public class NotifyConfiguration{
	AbstractNotificationTask growl = new GrowlNotification()
	String task
	String message
}
