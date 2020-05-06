/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import git4idea.GitLocalBranch
import git4idea.repo.GitRepository

fun GitLocalBranch.hasValidUpstream(repository: GitRepository): Boolean {
    val trackedBranch = this.findTrackedBranch(repository) ?: return false
    return repository.hasRemoteBranch(trackedBranch.name)
}