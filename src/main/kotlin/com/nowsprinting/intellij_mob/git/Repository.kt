package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.RepositoryKt")

sealed class RepositoryResult {
    class Failure(val reason: String) : RepositoryResult()
    class Success(val repository: GitRepository) : RepositoryResult()
}

/**
 * Get git repository
 */
fun getRepository(project: Project): RepositoryResult {
    val manager = GitRepositoryManager.getInstance(project)
    return getRepository(manager, logger)
}

internal fun getRepository(manager: GitRepositoryManager, log: Logger): RepositoryResult {
    val repositories = manager.repositories
    if (repositories.count() == 0) {
        val result = RepositoryResult.Failure("Repository not found in this project")
        log.warn(result.reason)
        return result
    }

    if (repositories.count() > 1) {
        val result = RepositoryResult.Failure("Has a multiple repositories in this project")
        // TODO: support multiple repositories
        var message = result.reason
        for (repo in repositories) {
            message += "\nFind repository: " + repo.root.path
        }
        log.warn(message)
        return result
    }

    log.debug("Find repository: $repositories[0].root.path")
    return RepositoryResult.Success(repositories[0])
}