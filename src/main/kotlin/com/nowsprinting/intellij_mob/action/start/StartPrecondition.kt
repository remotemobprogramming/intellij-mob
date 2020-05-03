package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.*
import git4idea.repo.GitRepository

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
    if (!isNothingToCommit(repository)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.has_uncommitted_changes"))
    }
    return Pair(true, null)
}