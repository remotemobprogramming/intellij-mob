package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.options.ShowSettingsUtil
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.MobSettingsConfigurable
import com.nowsprinting.intellij_mob.service.TimerService

class StartAction : AnAction() {
    private val logger = Logger.getInstance(javaClass)

    override fun update(e: AnActionEvent) {
        super.update(e)

        val timer = e.project?.let { TimerService.getInstance(it) }
        val enabled = timer?.let { !it.isRunning() } ?: false
        e.presentation.isEnabled = enabled
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: throw NullPointerException("AnActionEvent#getProject() was return null")
        val settings = MobProjectSettings.getInstance(project)
        val (canExecute, errorMessage) = checkStartPrecondition(settings, project)

        val dialog = StartDialog()
        dialog.title = e.presentation.text.removeSuffix("...")
        dialog.setTimer(settings.timer)
        dialog.setStartWithShare(settings.startWithShare)
        dialog.setNextAtExpire(settings.nextAtExpire)
        dialog.setPreconditionResult(canExecute, errorMessage)
        dialog.pack()
        dialog.setLocationRelativeTo(null) // set on screen center
        dialog.isVisible = true

        if (dialog.isOpenSettings) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, MobSettingsConfigurable::class.java)
        }

        if (dialog.isOk) {
            settings.timer = dialog.timer
            settings.startWithShare = dialog.isStartWithShare
            settings.nextAtExpire = dialog.isNextAtExpire
            start(settings, project)
        }
    }
}