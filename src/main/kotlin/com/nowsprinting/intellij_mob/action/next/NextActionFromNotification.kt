/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.next

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.nowsprinting.intellij_mob.MobBundle

class NextActionFromNotification(text: String? = MobBundle.message("mob.timer.expired.next")) :
    NotificationAction(text) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        notification.expire()
        NextAction().actionPerformed(e)
    }
}