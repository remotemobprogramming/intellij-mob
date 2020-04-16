package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.service.TimerService

class StartAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val timer = e.project?.let { TimerService.getInstance(it) }
        if (timer != null) {
            e.presentation.isEnabled = !timer.isRunning()
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val settings = MobProjectSettings.getInstance(e.project)
        val dialog = StartDialog()
        dialog.setTimer(settings.timer)
        dialog.setStartWithShare(settings.startWithShare)
        dialog.setNextAtExpire(settings.nextAtExpire)
        dialog.title = MobBundle.message("mob.start.dialog.title")
        dialog.pack()
        dialog.setLocationRelativeTo(null) // screen center
        dialog.isVisible = true
    }
}