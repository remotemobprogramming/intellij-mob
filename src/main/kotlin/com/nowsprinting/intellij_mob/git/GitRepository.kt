package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.GitRepositoryKt")

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
        val result = GitRepositoryResult.Failure("Repository not found in this project")
        log.warn(result.reason)
        return result
    }

    if (repositories.count() > 1) {
        val result = GitRepositoryResult.Failure("Has a multiple repositories in this project")
        // TODO: support multiple repositories
        var message = result.reason
        for (repo in repositories) {
            message += "\nFind repository: " + repo.root.path
        }
        log.warn(message)
        return result
    }

    log.debug("Find repository: $repositories[0].root.path")
    return GitRepositoryResult.Success(repositories[0])
}