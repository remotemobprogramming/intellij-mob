package com.nowsprinting.intellij_mob.git

import com.nowsprinting.intellij_mob.config.MobProjectSettings
import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * Check exist uncommitted changes.
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true if exist uncommite changes
 */
fun hasUncommittedChanges(repository: GitRepository, verbose: Boolean = false): Boolean {
    val command = GitCommand.STATUS
    val options = listOf("--short")

    val status = git(command, options, repository, verbose)
    return status.isNotEmpty()
}