package com.breskeby.gradle.notification;

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import com.breskeby.gradle.notification.NotifyConfiguration

import org.gradle.api.Task

public abstract class AbstractNotificationTask {
	public abstract void sendNotification(String message);
}
