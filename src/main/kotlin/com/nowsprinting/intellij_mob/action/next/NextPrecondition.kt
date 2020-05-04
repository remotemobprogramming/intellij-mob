package com.nowsprinting.intellij_mob.action.next

import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.*
import git4idea.repo.GitRepository

/**
 * Check precondition for mob next command
 *
 * @return success/failure, with error message
 */
fun checkNextPrecondition(settings: MobProjectSettings, project: Project): Pair<Boolean, String?> {
    return when (val result = getGitRepository(project)) {
        is GitRepositoryResult.Success -> {
            result.repository
            checkNextPrecondition(settings, result.repository)
        }
        is GitRepositoryResult.Failure -> {
            Pair(false, result.reason)
        }
    }
}

internal fun checkNextPrecondition(settings: MobProjectSettings, repository: GitRepository): Pair<Boolean, String?> {
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
    if (!repository.stayBranch(settings.wipBranch)) {
        val message = MobBundle.message("mob.next.error.reason.not_stay_wip_branch")
        return Pair(false, String.format(message, settings.wipBranch))
    }
    repository.currentBranch?.let {
        if (!it.hasValidUpstream(repository)) {
            return Pair(false, MobBundle.message("mob.start.error.reason.current_branch_has_not_valid_upstream"))
        }
    }

    // Do not check uncommitted changes, because can not detect unpushed commits here.

    return Pair(true, null)
}