package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.GitRepositoryResult
import com.nowsprinting.intellij_mob.git.getGitRepository
import com.nowsprinting.intellij_mob.git.isNothingToCommit
import git4idea.repo.GitRepository

/**
 * Check precondition for mob start command
 *
 * @return success/failure, error message
 */
fun checkStartPrecondition(settings: MobProjectSettings, project: Project): Pair<Boolean, String?> {
    val repository = when (val result = getGitRepository(project)) {
        is GitRepositoryResult.Success -> {
            result.repository
        }
        is GitRepositoryResult.Failure -> {
            val errorMessage = MobBundle.message("mob.start.error.reason.cant_get_git_repository")
            return Pair(false, errorMessage)
        }
    }
    return checkStartPrecondition(settings, repository)
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
    if (!isExistRemote(settings.remoteName, repository)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.not_exist_remote_name"))
    }
    if (!isExistBaseBranch(settings.baseBranch, repository)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.not_exist_base_branch"))
    }
    if (!isNothingToCommit(repository)) {
        return Pair(false, MobBundle.message("mob.start.error.reason.has_uncommitted_changes"))
    }
    return Pair(true, null)
}

private fun isExistRemote(remoteName: String, repository: GitRepository): Boolean {
    for (remote in repository.remotes) {
        if (remoteName == remote.name) {
            return true
        }
    }
    return false
}

private fun isExistBaseBranch(branchName: String, repository: GitRepository): Boolean {
    for (localBranch in repository.branches.localBranches) {
        if (branchName == localBranch.name) {
            return true
        }
    }
    for (remoteBranch in repository.branches.remoteBranches) {
        if (remoteBranch.name.endsWith("/$branchName")) {
            return true
        }
    }
    return false
}