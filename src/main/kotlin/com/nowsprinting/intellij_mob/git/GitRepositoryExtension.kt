package com.nowsprinting.intellij_mob.git

import com.intellij.dvcs.repo.Repository
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import git4idea.GitLocalBranch
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.GitRepositoryExtensionKt")

fun GitRepository.hasRemote(remoteName: String): Boolean {
    for (remote in this.remotes) {
        if (remoteName == remote.name) {
            return true
        }
    }
    return false
}

fun GitRepository.hasMobProgrammingBranch(settings: MobProjectSettings): Boolean {
    return hasLocalBranch(settings.wipBranch)
}

fun GitRepository.hasLocalBranch(branchName: String): Boolean {
    if (this.getLocalBranch(branchName) != null) {
        return true
    }
    return false
}

fun GitRepository.getLocalBranch(branchName: String): GitLocalBranch? {
    for (branch in this.branches.localBranches) {
        if (branch.name == branchName) {
            return branch
        }
    }
    return null
}

fun GitRepository.hasMobProgrammingBranchOrigin(settings: MobProjectSettings): Boolean {
    return hasRemoteBranch(settings.remoteName, settings.wipBranch)
}

fun GitRepository.hasRemoteBranch(remoteName: String, branchName: String): Boolean {
    val remoteBranchName = "${remoteName}/${branchName}"
    return hasRemoteBranch(remoteBranchName)
}

fun GitRepository.hasRemoteBranch(remoteBranchName: String): Boolean {
    for (branch in this.branches.remoteBranches) {
        if (branch.name == remoteBranchName) {
            return true
        }
    }
    return false
}

fun GitRepository.isMobProgramming(settings: MobProjectSettings): Boolean {
    return stayBranch(settings.wipBranch)
}

fun GitRepository.stayBranch(branchName: String): Boolean {
    if (this.currentBranchName == branchName) {
        return true
    }
    return false
}

/**
 * Check uncommitted changes with outside `Task.Backgroundable#run()`.
 *
 * Note: Untracked files are not detected.
 */
fun GitRepository.isNothingToCommit(): Boolean {
    if (state != Repository.State.NORMAL) {
        logger.info("Repository state is ${state.toString()}")
        return false
    }

    val changes = ChangeListManager.getInstance(project).allChanges
    if (changes.isNotEmpty()) {
        logger.info("Has uncommitted changes")
        for (v in changes) {
            logger.debug("  ${v.type.toString()}: ${v.virtualFile?.path}")
        }
        return false
    }
    return true
}

private fun GitRepository.validateCommonPrecondition(settings: MobProjectSettings): Pair<Boolean, String?> {
    if (!hasRemote(remoteName = settings.remoteName)) {
        return Pair(false, MobBundle.message("mob.validate_reason.not_exist_remote_name"))
    }
    if (!hasRemoteBranch(remoteName = settings.remoteName, branchName = settings.baseBranch)) {
        return Pair(false, MobBundle.message("mob.validate_reason.not_exist_base_branch_on_remote"))
    }
    currentBranch?.let {
        if (!it.hasValidUpstream(this)) {
            return Pair(false, MobBundle.message("mob.validate_reason.current_branch_has_not_valid_upstream"))
        }
    }
    return Pair(true, null)
}

/**
 * Validate repository for start precondition check and start task.
 */
fun GitRepository.validateForStartPrecondition(settings: MobProjectSettings): Pair<Boolean, String?> {
    val (valid, reason) = validateCommonPrecondition(settings)
    if (!valid) {
        return Pair(valid, reason)
    }
    getLocalBranch(settings.baseBranch)?.let {
        if (!it.hasValidUpstream(this)) {
            return Pair(false, MobBundle.message("mob.validate_reason.base_branch_has_not_valid_upstream"))
        }
    }
    if (!isNothingToCommit()) {
        return Pair(false, MobBundle.message("mob.validate_reason.has_uncommitted_changes"))
    }
    return Pair(true, null)
}

/**
 * Validate repository for next precondition check and next task.
 *
 * Note: Do not check nothing uncommitted changes, because can not detect unpushed commits here.
 */
fun GitRepository.validateForNextPrecondition(settings: MobProjectSettings): Pair<Boolean, String?> {
    val (valid, reason) = validateCommonPrecondition(settings)
    if (!valid) {
        return Pair(valid, reason)
    }
    if (!stayBranch(settings.wipBranch)) {
        val message = MobBundle.message("mob.validate_reason.not_stay_wip_branch")
        return Pair(false, String.format(message, settings.wipBranch))
    }
    // Validate about upstream is passed inside validateCommonPrecondition()

    return Pair(true, null)
}