/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.reset

import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.validateForResetTask
import com.nowsprinting.intellij_mob.git.*
import com.nowsprinting.intellij_mob.timer.TimerService
import com.nowsprinting.intellij_mob.util.notify
import git4idea.repo.GitRepository

class ResetTask(val settings: MobProjectSettings, project: Project, title: String) : Backgroundable(project, title) {
    private val logger = Logger.getInstance(javaClass)
    private val notifyContents = mutableListOf<String>()
    private var completed = false
    private lateinit var repository: GitRepository

    override fun run(indicator: ProgressIndicator) {
        val fractionPerCommandSection = 1.0 / 5
        indicator.isIndeterminate = false
        indicator.fraction = 0.0
        logger.debug(String.format(MobBundle.message("mob.notify_content.begin"), title))

        repository = when (val result = getGitRepository(project)) {
            is GitRepositoryResult.Success -> {
                result.repository
            }
            is GitRepositoryResult.Failure -> {
                notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), result.reason))
                return
            }
        }

        val (validSettings, reasonInvalidSettings) = settings.validateForResetTask()
        if (!validSettings) {
            val format = MobBundle.message("mob.reset.error.precondition")
            val message = String.format(format, reasonInvalidSettings)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }

        val (validRepository, reasonInvalidRepository) = repository.validateForResetPrecondition(settings)
        if (!validRepository) {
            val format = MobBundle.message("mob.reset.error.precondition")
            val message = String.format(format, reasonInvalidRepository)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }
        indicator.fraction += fractionPerCommandSection

        stopTimer()

        if (!fetch(repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (!checkout(settings.baseBranch, repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (repository.hasMobProgrammingBranch(settings)) {
            if (!deleteBranch(settings.wipBranch, repository, notifyContents)) {
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        if (repository.hasMobProgrammingBranchOrigin(settings)) {
            if (!deleteRemoteBranch(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        indicator.fraction = 1.0
        completed = true
    }

    override fun onFinished() {
        if (completed) {
            logger.debug(String.format(MobBundle.message("mob.notify_content.success"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.reset.task_successful"),
                contents = notifyContents,
                type = NotificationType.INFORMATION
            )
        } else {
            logger.debug(String.format(MobBundle.message("mob.notify_content.failure"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.reset.task_failure"),
                contents = notifyContents,
                type = NotificationType.ERROR
            )
        }
    }

    private fun stopTimer() {
        val timer = TimerService.getInstance(project)
        timer?.stop()
    }
}