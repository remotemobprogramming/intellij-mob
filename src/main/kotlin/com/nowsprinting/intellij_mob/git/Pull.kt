package com.nowsprinting.intellij_mob.git

import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git pull --ff-only
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true: Git command successful
 */
fun pull(repository: GitRepository, notifyContents: MutableList<String>? = null, verbose: Boolean = false): Boolean {
    val command = GitCommand.PULL
    val options = listOf("--ff-only")

    return git(command, options, repository, notifyContents, verbose)
}