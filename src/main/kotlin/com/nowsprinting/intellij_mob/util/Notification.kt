/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.util

import com.intellij.notification.*
import com.intellij.openapi.project.Project

private val NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup("Mob")

/**
 * Notify information level message
 */
fun notifyInformation(content: String): Notification? {
    return notify(content = content, type = NotificationType.INFORMATION)
}

/**
 * Notify warning level message
 */
fun notifyWarning(content: String): Notification? {
    return notify(content = content, type = NotificationType.WARNING)
}

/**
 * Notify error level message
 */
fun notifyError(content: String): Notification? {
    return notify(content = content, type = NotificationType.ERROR)
}

/**
 * Notify messages with title
 */
fun notify(
    project: Project? = null,
    title: String = "",
    contents: List<String>,
    type: NotificationType
): Notification? {
    var content = StringBuilder()
    for (v in contents) {
        content.append("%n").append(v)
    }
    return notify(project = project, title = title, content = content.toString().format(), type = type)
}

/**
 * Notify message with title
 */
fun notify(
    project: Project? = null,
    title: String = "",
    content: String,
    type: NotificationType
): Notification? {
    val notification = NOTIFICATION_GROUP.createNotification(title = title, content = content, type = type)
    notification.notify(project)
    return notification
}

/**
 * Open `EventLog` window
 */
fun openEventLog(project: Project) {
    val eventLog = EventLog.getEventLog(project)
    if (eventLog != null && !eventLog.isVisible) {
        eventLog.activate(Runnable {
            val contentName = getContentName(NOTIFICATION_GROUP.displayId)
            val content = eventLog.contentManager.findContent(contentName)
            if (content != null) {
                eventLog.contentManager.setSelectedContent(content)
            }
        }, true)
    }
}

private fun getContentName(groupId: String): String {
    for (category in EventLogCategory.EP_NAME.extensionList) {
        if (category.acceptsNotification(groupId)) {
            return category.displayName
        }
    }
    return ""
}