/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.done

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.validateForDoneTask
import com.nowsprinting.intellij_mob.git.*
import com.nowsprinting.intellij_mob.timer.TimerService
import com.nowsprinting.intellij_mob.util.notify
import git4idea.repo.GitRepository

class DoneTask(val settings: MobProjectSettings, val e: AnActionEvent, project: Project, title: String) :
    Backgroundable(project, title) {
    private val logger = Logger.getInstance(javaClass)
    private val notifyContents = mutableListOf<String>()
    private var completed = false
    private var doNotRun = false
    private var needOpenCommitDialog = false
    private lateinit var repository: GitRepository
    private lateinit var coAuthors: Set<String>

    override fun run(indicator: ProgressIndicator) {
        val fractionPerCommandSection = 1.0 / 10
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

        val (validSettings, reasonInvalidSettings) = settings.validateForDoneTask()
        if (!validSettings) {
            val format = MobBundle.message("mob.done.error.precondition")
            val message = String.format(format, reasonInvalidSettings)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }

        val (validRepository, reasonInvalidRepository) = repository.validateForDone(settings)
        if (!validRepository) {
            val format = MobBundle.message("mob.done.error.precondition")
            val message = String.format(format, reasonInvalidRepository)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }
        indicator.fraction += fractionPerCommandSection

        stopTimer()

        val hasUncommittedChanges = hasUncommittedChanges(repository)
        val hasChangesForDone = hasChangesForDone(settings, repository)
        if (!hasUncommittedChanges && !hasChangesForDone) {
            val format = MobBundle.message("mob.done.error.precondition")
            val message = String.format(format, MobBundle.message("mob.done.error.reason.nothing_changes_to_squash"))
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.warning"), message))
            doNotRun = true
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (!fetch(repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (repository.hasMobProgrammingBranchOrigin(settings)) {
            coAuthors = getCoAuthors(settings, repository)  // create before squash
            if (!mergeRemoteWipBranchToBaseBranch(indicator)) {
                return
            }
            needOpenCommitDialog = true
        } else {
            if (!noRemoteWipBranch()) {
                return
            }
        }

        indicator.fraction = 1.0
        completed = true
    }

    override fun onFinished() {
        if (completed) {
            logger.debug(String.format(MobBundle.message("mob.notify_content.success"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.done.task_successful"),
                contents = notifyContents,
                type = NotificationType.INFORMATION
            )
        } else if (doNotRun) {
            logger.debug(String.format(MobBundle.message("mob.notify_content.warning"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.done.task_not_run"),
                contents = notifyContents,
                type = NotificationType.WARNING
            )
        } else {
            logger.debug(String.format(MobBundle.message("mob.notify_content.failure"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.done.task_failure"),
                contents = notifyContents,
                type = NotificationType.ERROR
            )
        }
        VirtualFileManager.getInstance().asyncRefresh {
            logger.debug(MobBundle.message("mob.logging.refresh"))
            if (needOpenCommitDialog) {
                openGitCommitAndPushDialog(project, coAuthors)
            }
        }
    }

    private fun mergeRemoteWipBranchToBaseBranch(indicator: ProgressIndicator): Boolean {
        val fractionPerCommand = 1.0 / 10

        if (hasUncommittedChanges(repository)) {
            if (!add(repository, notifyContents)) {
                return false
            }
            if (!commit(settings.wipCommitMessage, repository, notifyContents)) {
                return false
            }
        }
        indicator.fraction += fractionPerCommand

        if (!push(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return false
        }
        indicator.fraction += fractionPerCommand

        if (!checkout(settings.baseBranch, repository, notifyContents)) {
            return false
        }
        indicator.fraction += fractionPerCommand

        if (!mergeFastForward(settings.remoteName, settings.baseBranch, repository, notifyContents)) {
            return false
        }
        indicator.fraction += fractionPerCommand

        if (!mergeWithSquash(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        indicator.fraction += fractionPerCommand

        if (!deleteBranch(settings.wipBranch, repository, notifyContents)) {    // TODO: check if unmerged commits
            return false
        }
        indicator.fraction += fractionPerCommand

        if (!deleteRemoteBranch(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return false
        }
        indicator.fraction += fractionPerCommand

        val changes = StringBuilder()
        for (v in diffCached(repository)) {
            changes.append("%n| ").append(v)
        }
        if (changes.isNotEmpty()) {
            notifyContents.add(changes.substring(2))
        }
        return true
    }

    private fun noRemoteWipBranch(): Boolean {
        if (!checkout(settings.baseBranch, repository, notifyContents)) {
            return false
        }
        if (!deleteBranch(settings.wipBranch, repository, notifyContents)) {    // TODO: check if unmerged commits
            return false
        }
        notifyContents.add(
            String.format(
                MobBundle.message("mob.notify_content.notify"),
                MobBundle.message("mob.done.already_ended")
            )
        )
        return true
    }

    private fun stopTimer() {
        val timer = TimerService.getInstance(project)
        timer?.stop()
    }
}