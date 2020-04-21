package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import git4idea.fetch.GitFetchSupport
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.FetchKt")

sealed class FetchResult {
    class Failure(val reason: String) : FetchResult()
    class Success() : FetchResult()
}

/**
 * git fetch origin --recurse-submodules=no --progress --prune
 */
fun fetch(repository: GitRepository): FetchResult {
    return fetch(repository, logger)
}

internal fun fetch(repository: GitRepository, log: Logger): FetchResult {
    if (repository.remotes.isEmpty()) {
        val result = FetchResult.Failure("Repository not has remote")
        log.warn(result.reason)
        return result
    }

    val project = repository.project
    val fetchSupport = GitFetchSupport.fetchSupport(project)
    val fetchResult = fetchSupport.fetchAllRemotes(setOf(repository))
    if (fetchResult.showNotificationIfFailed()) { // TODO: can show title?
        val result = FetchResult.Failure("git fetch failure")
        log.error(result.reason)
        return result
    }

    log.info("git fetch successful")
    return FetchResult.Success()
}