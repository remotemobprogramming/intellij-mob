package com.nowsprinting.intellij_mob.action.start

import com.intellij.dvcs.repo.Repository
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.*
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")

/**
 * Check precondition for mob start command
 *
 * @return success/failure, with error message
 */
fun checkStartPrecondition(settings: MobProjectSettings, project: Project): Pair<Boolean, String?> {
    return when (val result = getGitRepository(project)) {
        is GitRepositoryResult.Success -> {
            result.repository
            checkStartPrecondition(settings, result.repository)
        }
        is GitRepositoryResult.Failure -> {
            Pair(false, result.reason)
        }
    }
}

/**
 * Check precondition for mob start command
 *
 * @return success/failure, error message
 */
fun checkStartPrecondition(settings: MobProjectSettings, repository: GitRepository): Pair<Boolean, String?> {
    if (settings.wipBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.reason.unset_wip_branch"))
    }
    if (settings.baseBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.reason.unset_base_branch"))
    }
    if (settings.remoteName.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.reason.unset_remote_name"))
    }
    if (!repository.hasRemote(remoteName = settings.remoteName)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.not_exist_remote_name"))
    }
    if (!repository.hasRemoteBranch(remoteName = settings.remoteName, branchName = settings.baseBranch)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.not_exist_base_branch_on_remote"))
    }
    repository.getLocalBranch(settings.baseBranch)?.let {
        if (!it.hasValidUpstream(repository)) {
            return Pair(false, MobBundle.message("mob.start.error.reason.base_branch_has_not_valid_upstream"))
        }
    }
    repository.currentBranch?.let {
        if (!it.hasValidUpstream(repository)) {
            return Pair(false, MobBundle.message("mob.start.error.reason.current_branch_has_not_valid_upstream"))
        }
    }
    if (!isNothingToCommit(repository)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.has_uncommitted_changes"))
    }
    return Pair(true, null)
}

fun isNothingToCommit(repository: Repository): Boolean {
    if (repository.state != Repository.State.NORMAL) {
        logger.info("Repository state is ${repository.state.toString()}")
        return false
    }

    FileDocumentManager.getInstance().saveAllDocuments()

    val changes = ChangeListManager.getInstance(repository.project).allChanges
    if (changes.isNotEmpty()) {
        logger.info("Has uncommitted changes")
        for (v in changes) {
            logger.debug("  ${v.type.toString()}: ${v.virtualFile?.path}")
        }
        return false
    }
    // Note: Untracked files are not detected

    return true
}