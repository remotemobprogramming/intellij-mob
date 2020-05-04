package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git log $from..$to --pretty="format:%h %cr <%an>" --abbrev-commit
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   from            From branch name
 * @param   to              To branch name (current)
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  changes list
 */
fun logFrom(from: String, to: String, repository: GitRepository, verbose: Boolean = false): List<String> {
    val command = GitCommand.LOG
    val options = listOf("$from..$to", "--pretty=format:%h %cr <%an>", "--abbrev-commit")

    return git(command, options, repository, verbose)
}