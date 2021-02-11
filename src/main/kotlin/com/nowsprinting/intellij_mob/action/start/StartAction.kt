/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.MobSettingsConfigurable
import com.nowsprinting.intellij_mob.timer.TimerService

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

        FileDocumentManager.getInstance().saveAllDocuments()
        logger.debug(MobBundle.message("mob.logging.save_all_documents"))
        val (canExecute, reason) = checkStartPrecondition(settings, project)

        val dialog = StartDialog()
        dialog.title = e.presentation.text.removeSuffix("...")
        dialog.timerMinutes = settings.timerMinutes
        dialog.isStartWithShare = settings.startWithShare
        dialog.setPreconditionResult(canExecute, reason)
        dialog.pack()
        dialog.setLocationRelativeTo(null) // set on screen center
        dialog.isVisible = true

        if (dialog.isOpenSettings) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, MobSettingsConfigurable::class.java)
        }

        if (dialog.isOk) {
            settings.timerMinutes = dialog.timerMinutes
            settings.startWithShare = dialog.isStartWithShare
            // Do not call saveAllDocuments() before run start task, Because there may be changes in mob.xml
            StartTask(settings, e, project, dialog.title).queue()
        }
    }
}