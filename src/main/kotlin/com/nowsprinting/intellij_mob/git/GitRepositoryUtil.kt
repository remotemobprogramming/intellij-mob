/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.GitRepositoryUtilKt")

sealed class GitRepositoryResult {
    class Failure(val reason: String) : GitRepositoryResult()
    class Success(val repository: GitRepository) : GitRepositoryResult()
}

/**
 * Get git repository
 */
fun getGitRepository(project: Project): GitRepositoryResult {
    val manager = GitRepositoryManager.getInstance(project)
    return getGitRepository(manager, logger)
}

internal fun getGitRepository(manager: GitRepositoryManager, log: Logger): GitRepositoryResult {
    val repositories = manager.repositories
    if (repositories.count() == 0) {
        val result = GitRepositoryResult.Failure(MobBundle.message("mob.start.error.reason.repository_not_found"))
        log.error(result.reason)
        return result
    }

    if (repositories.count() > 1) {
        val result = GitRepositoryResult.Failure(MobBundle.message("mob.start.error.reason.has_multiple_repositories"))
        // TODO: support multiple repositories
        val logMessage = StringBuilder(result.reason)
        for (repo in repositories) {
            logMessage.append("%nFound repository: $repo.root.path")
        }
        log.error(logMessage.toString().format())
        return result
    }

    log.debug("Found repository: $repositories[0].root.path")
    return GitRepositoryResult.Success(repositories[0])
}