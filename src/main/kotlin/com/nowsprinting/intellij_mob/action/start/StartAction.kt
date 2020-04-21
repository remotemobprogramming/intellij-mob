package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.MobSettingsConfigurable
import com.nowsprinting.intellij_mob.git.RepositoryResult
import com.nowsprinting.intellij_mob.git.getRepository
import com.nowsprinting.intellij_mob.service.TimerService
import git4idea.repo.GitRepository

class StartAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val timer = e.project?.let { TimerService.getInstance(it) }
        timer?.let { e.presentation.isEnabled = it.isRunning() }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val dialog = StartDialog()
        dialog.title = MobBundle.message("mob.start.dialog.title")

        val settings = MobProjectSettings.getInstance(e.project)
        dialog.setTimer(settings.timer)
        dialog.setStartWithShare(settings.startWithShare)
        dialog.setNextAtExpire(settings.nextAtExpire)

        val repository: GitRepository? = e.project?.let {
            when (val result = getRepository(it)) {
                is RepositoryResult.Success -> {
                    result.repository
                }
                is RepositoryResult.Failure -> {
                    val errorMessage = MobBundle.message("mob.start.error.cant_get_git_repository")
                    dialog.setPreconditionResult(false, errorMessage)
                    null
                }
            }
        }
        repository?.let {
            val (canExecute, errorMessage) = checkStartPrecondition(settings, it);
            dialog.setPreconditionResult(canExecute, errorMessage)
        }

        dialog.pack()
        dialog.setLocationRelativeTo(null) // set on screen center
        dialog.isVisible = true

        if (dialog.isOpenSettings) {
            ShowSettingsUtil.getInstance().showSettingsDialog(e.project, MobSettingsConfigurable::class.java)
        }

        if (dialog.isOk) {
            settings.timer = dialog.timer
            settings.startWithShare = dialog.isStartWithShare
            settings.nextAtExpire = dialog.isNextAtExpire
            start(settings, repository!!)   // if click ok, repository exists
        }
    }
}