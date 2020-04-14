package com.nowsprinting.intellij_mob.service;

import com.intellij.notification.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

public class NotificationService {
    private final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Mob", NotificationDisplayType.BALLOON, true);

    public static NotificationService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, NotificationService.class);
    }

    public Notification information(String content) {
        return notify(null, content, NotificationType.INFORMATION);
    }

    public Notification warning(String content) {
        return notify(null, content, NotificationType.WARNING);
    }

    public Notification error(String content) {
        return notify(null, content, NotificationType.ERROR);
    }

    public Notification notify(Project project, String content, NotificationType type) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, type);
        notification.notify(project);
        return notification;
    }

    public void openEventLog(@NotNull Project project) {
        ToolWindow eventLog = EventLog.getEventLog(project);
        if (eventLog != null && !eventLog.isVisible()) {
            eventLog.activate(() -> {
                String contentName = getContentName(NOTIFICATION_GROUP.getDisplayId());
                Content content = eventLog.getContentManager().findContent(contentName);
                if (content != null) {
                    eventLog.getContentManager().setSelectedContent(content);
                }
            }, true);
        }
    }

    @NotNull
    private static String getContentName(@NotNull String groupId) {
        for (EventLogCategory category : EventLogCategory.EP_NAME.getExtensionList()) {
            if (category.acceptsNotification(groupId)) {
                return category.getDisplayName();
            }
        }
        return "";
    }
}