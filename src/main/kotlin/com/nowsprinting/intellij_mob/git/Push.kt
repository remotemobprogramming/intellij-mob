package com.nowsprinting.intellij_mob.git

import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git push --set-upstream $remote $branch
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   remote          Target remote name
 * @param   branch          Target branch name
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true: Git command successful
 */
fun push(
    remote: String,
    branch: String,
    repository: GitRepository,
    notifyContents: MutableList<String>? = null,
    verbose: Boolean = false
): Boolean {
    val command = GitCommand.PUSH
    val options = listOf("--set-upstream", remote, branch)

    return git(command, options, repository, notifyContents, verbose)
}