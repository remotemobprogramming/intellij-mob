package com.nowsprinting.intellij_mob.action.start

import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.*
import com.nowsprinting.intellij_mob.service.TimerService
import com.nowsprinting.intellij_mob.util.notify
import com.nowsprinting.intellij_mob.util.screenShareWithZoom
import com.nowsprinting.intellij_mob.util.status
import git4idea.repo.GitRepository

class StartTask(val settings: MobProjectSettings, project: Project, title: String) : Backgroundable(project, title) {
    private val notifyContents = mutableListOf<String>()
    private var completed = false
    private lateinit var repository: GitRepository

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true
        indicator.fraction = 0.0
        val fractionPerCommandSection = 1.0 / 4

        repository = when (val result = getGitRepository(project)) {
            is GitRepositoryResult.Success -> {
                result.repository
            }
            is GitRepositoryResult.Failure -> {
                notifyContents.add(String.format(MobBundle.message("mob.notify_content.failure"), result.reason))
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        if (!fetch(repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        if (!pull(repository, notifyContents)) {
            return
        }
        indicator.fraction += fractionPerCommandSection

        val hasMobProgrammingBranch = repository.hasMobProgrammingBranch(settings)
        val hasMobProgrammingBranchOrigin = repository.hasMobProgrammingBranchOrigin(settings)
        if (hasMobProgrammingBranch && hasMobProgrammingBranchOrigin) {
            if (!rejoiningMobSession()) {
                return
            }
        } else if (!hasMobProgrammingBranch && !hasMobProgrammingBranchOrigin) {
            if (!createWipBranchFromBaseBranch()) {
                return
            }
        } else if (!hasMobProgrammingBranch && hasMobProgrammingBranchOrigin) {
            if (!joiningMobSession()) {
                return
            }
        } else {
            if (!purgingLocalBranchAndStartNewWipBranchFromBaseBranch()) {
                return
            }
        }
        indicator.fraction += fractionPerCommandSection

        val timer = TimerService.getInstance(project)
        if (timer != null && !timer.isRunning()) {
            timer.start(settings.timerMinutes)
            val message = MobBundle.message("mob.timer.start_successful")
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))
        } else {
            val message = MobBundle.message("mob.timer.start_failure")
            notifyContents.add(String.format(MobBundle.message("mob.notify_content.warning"), message))
        }

        if (settings.startWithShare) {
            val (success, message) = screenShareWithZoom()
            if (success) {
                notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))
            } else {
                notifyContents.add(String.format(MobBundle.message("mob.notify_content.warning"), message))
            }
        }

        notifyContents.add(status(repository, settings))

        indicator.fraction = 1.0
        completed = true
    }

    override fun onFinished() {
        if (completed) {
            notify(
                project = project,
                title = MobBundle.message("mob.start.task_successful"),
                contents = notifyContents,
                type = NotificationType.INFORMATION
            )
        } else {
            notify(
                project = project,
                title = MobBundle.message("mob.start.task_failure"),
                contents = notifyContents,
                type = NotificationType.ERROR
            )
        }
    }

    private fun rejoiningMobSession(): Boolean {
        val message = MobBundle.message("mob.start.rejoining_mob_session")
        notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))

        if (!repository.isMobProgramming(settings)) {
            if (!deleteBranch(settings.wipBranch, repository, notifyContents)) {    // TODO: check if unmerged commits
                return false
            }
            if (!checkout(settings.wipBranch, repository, notifyContents)) {
                return false
            }
            if (!setUpstreamToRemoteBranch(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
                return false
            }
        }
        return true
    }

    private fun createWipBranchFromBaseBranch(): Boolean {
        val messageFormat = MobBundle.message("mob.start.create_wip_branch_from_base_branch")
        val message = String.format(messageFormat, settings.wipBranch, settings.baseBranch)
        notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))

        if (!checkout(settings.baseBranch, repository, notifyContents)) {
            return false
        }
        if (!merge(settings.remoteName, settings.baseBranch, repository, notifyContents)) {
            return false
        }
        if (!createBranch(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        if (!checkout(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        if (!push(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return false
        }
        return true
    }

    private fun joiningMobSession(): Boolean {
        val message = MobBundle.message("mob.start.joining_mob_session")
        notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))

        if (!checkout(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        if (!setUpstreamToRemoteBranch(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return false
        }
        return true
    }

    private fun purgingLocalBranchAndStartNewWipBranchFromBaseBranch(): Boolean {
        val messageFormat = MobBundle.message("mob.start.purging_local_branch_and_start_new_wip_branch_from_base")
        val message = String.format(messageFormat, settings.wipBranch, settings.baseBranch)
        notifyContents.add(String.format(MobBundle.message("mob.notify_content.notify"), message))

        if (!deleteBranch(settings.wipBranch, repository, notifyContents)) {    // TODO: check if unmerged commits
            return false
        }
        if (!checkout(settings.baseBranch, repository, notifyContents)) {
            return false
        }
        if (!merge(settings.remoteName, settings.baseBranch, repository, notifyContents)) {
            return false
        }
        if (!createBranch(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        if (!checkout(settings.wipBranch, repository, notifyContents)) {
            return false
        }
        if (!push(settings.remoteName, settings.wipBranch, repository, notifyContents)) {
            return false
        }
        return true
    }
}