/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.done

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.nowsprinting.intellij_mob.MobBundle

class DoneNotificationAction(text: String? = MobBundle.message("mob.timer.expired.done")) :
    NotificationAction(text) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        notification.expire()
        DoneAction().actionPerformed(e)
    }
}