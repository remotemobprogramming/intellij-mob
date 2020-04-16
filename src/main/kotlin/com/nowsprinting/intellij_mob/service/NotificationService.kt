package com.nowsprinting.intellij_mob.service

import com.intellij.notification.*
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

class NotificationService {
    private val NOTIFICATION_GROUP = NotificationGroup("Mob", NotificationDisplayType.BALLOON, true)

    fun information(content: String?): Notification? {
        return notify(null, content, NotificationType.INFORMATION)
    }

    fun warning(content: String?): Notification? {
        return notify(null, content, NotificationType.WARNING)
    }

    fun error(content: String?): Notification? {
        return notify(null, content, NotificationType.ERROR)
    }

    fun notify(
        project: Project?,
        content: String?,
        type: NotificationType?
    ): Notification? {
        val notification = NOTIFICATION_GROUP.createNotification(content!!, type!!)
        notification.notify(project)
        return notification
    }

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

    companion object {
        fun getInstance(project: Project): NotificationService? {
            return ServiceManager.getService(
                project,
                NotificationService::class.java
            )
        }
    }
}