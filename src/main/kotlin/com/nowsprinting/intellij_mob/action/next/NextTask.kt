package com.nowsprinting.intellij_mob.action.next

import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.*
import com.nowsprinting.intellij_mob.service.TimerService
import com.nowsprinting.intellij_mob.util.notify
import git4idea.repo.GitRepository

class NextTask(val settings: MobProjectSettings, project: Project, title: String) : Backgroundable(project, title) {
    private val logger = Logger.getInstance(javaClass)
    private val notifyContents = mutableListOf<String>()
    private var completed = false
    private var doNotRun = false
    private lateinit var repository: GitRepository

    override fun run(indicator: ProgressIndicator) {
        val fractionPerCommandSection = 1.0 / 8
        indicator.isIndeterminate = false
        indicator.fraction = 0.0
        logger.debug(String.format(MobBundle.message("mob.notify_content.begin"), title))

        repository = when (val result = getGitRepository(project)) {
            is GitRepositoryResult.Success -> {
                result.repository
            }
            is GitRepositoryResult.Failure -> {
                logger.warn(result.reason)
                notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), result.reason))
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        val (canExecute, errorReason) = checkNextPrecondition(settings, repository)  // recheck precondition
        if (!canExecute) {
            val format = MobBundle.message("mob.next.error.cant_do_next")
            val message = String.format(format, errorReason)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (settings.wipCommitMessage.isEmpty()) {
            val format = MobBundle.message("mob.next.error.cant_do_next")
            val message = String.format(format, errorReason)
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), message))
            return
        }
        indicator.fraction += fractionPerCommandSection

        val hasUncommittedChanges = hasUncommittedChanges(repository)
        val hasUnpushedCommit = hasUnpushedCommit(settings, repository)
        if (!hasUncommittedChanges && !hasUnpushedCommit) {
            val message = MobBundle.message("mob.next.error.reason.has_not_uncommitted_changes")
            logger.warn(message)
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.warning"), message))
            doNotRun = true
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (hasUncommittedChanges) {
            if (!add(repository, notifyContents)) {
                return
            }
            if (!commit(settings.wipCommitMessage, repository, notifyContents)) {
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        val pushCommits = StringBuilder()
        for (v in diffFrom(settings.remoteName, settings.wipBranch, repository)) {
            pushCommits.append("%n| ").append(v)
        }
        indicator.fraction += fractionPerCommandSection

        if (!push(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        notifyContents.add(pushCommits.substring(2))

        showNextTypist(settings, repository, notifyContents)
        indicator.fraction += fractionPerCommandSection

        stopTimer()

        if (!settings.nextStay) {
            checkout(settings.baseBranch, repository, notifyContents)
            // If it fails, it does not cause an error
        }

        indicator.fraction = 1.0
        completed = true
    }

    override fun onFinished() {
        if (completed) {
            logger.debug(String.format(MobBundle.message("mob.notify_content.success"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.next.task_successful"),
                contents = notifyContents,
                type = NotificationType.INFORMATION
            )
        } else if (doNotRun) {
            logger.debug(String.format(MobBundle.message("mob.notify_content.warning"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.next.task_do_not_run"),
                contents = notifyContents,
                type = NotificationType.WARNING
            )
        } else {
            logger.debug(String.format(MobBundle.message("mob.notify_content.failure"), title))
            notify(
                project = project,
                title = MobBundle.message("mob.next.task_failure"),
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