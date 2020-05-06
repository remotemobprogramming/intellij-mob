package com.nowsprinting.intellij_mob.action.done

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.nowsprinting.intellij_mob.MobBundle

class DoneActionFromNotification(text: String? = MobBundle.message("mob.timer.expired.done")) :
    NotificationAction(text) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        notification.expire()
        DoneAction().actionPerformed(e)
    }
}