package com.nowsprinting.intellij_mob.action.start

import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import git4idea.repo.GitRepository

/**
 * Check precondition for mob start command
 *
 * @return success/failure, error message
 */
fun checkStartPrecondition(settings: MobProjectSettings, repository: GitRepository): Pair<Boolean, String?> {
    if (settings.wipBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.unset_wip_branch"))
    }
    if (settings.baseBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.unset_base_branch"))
    }
    if (settings.remoteName.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.start.error.unset_remote_name"))
    }
    if (!isExistRemote(settings.remoteName, repository)) {
        return Pair(false, MobBundle.message("mob.start.error.not_exist_remote_name"))
    }
    if (!isExistBaseBranch(settings.baseBranch, repository)) {
        return Pair(false, MobBundle.message("mob.start.error.not_exist_base_branch"))
    }
    if (!isNothingToCommit()) {
        return Pair(false, MobBundle.message("mob.start.error.has_uncommitted_changes"))
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

private fun isNothingToCommit(): Boolean {
//    output : = silentgit("status", "--short")
//    var isMobbing: = len(strings.TrimSpace(output)) == 0
//    return isMobbing
    return true // TODO:
}