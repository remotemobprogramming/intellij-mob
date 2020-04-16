package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.MobSettingsConfigurable
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

        if (dialog.isOpenSettings) {
            ShowSettingsUtil.getInstance().showSettingsDialog(e.project, MobSettingsConfigurable::class.java)
        }

        if (dialog.isOk) {
            settings.timer = dialog.timer
            settings.startWithShare = dialog.isStartWithShare
            settings.nextAtExpire = dialog.isNextAtExpire
            // TODO: run start task
        }
    }
}