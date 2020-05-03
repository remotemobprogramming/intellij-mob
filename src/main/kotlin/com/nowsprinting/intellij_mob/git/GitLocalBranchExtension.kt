package com.nowsprinting.intellij_mob.git

import git4idea.GitLocalBranch
import git4idea.repo.GitRepository

fun GitLocalBranch.hasValidUpstream(repository: GitRepository): Boolean {
    val trackedBranch = this.findTrackedBranch(repository) ?: return false
    return repository.hasRemoteBranch(trackedBranch.name)
}