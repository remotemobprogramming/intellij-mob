package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.LogKt")

/**
 * git log $baseBranch..$wipBranch --pretty="format:%h %cr <%an>" --abbrev-commit
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   settings        Use base and wip branch
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  commit list
 */
fun logInWip(settings: MobProjectSettings, repository: GitRepository, verbose: Boolean = false): List<String> {
    val command = GitCommand.LOG
    val from = settings.baseBranch
    val to = settings.wipBranch
    val options = listOf("$from..$to", "--pretty=format:%h %cr <%an>", "--abbrev-commit")

    return git(command, options, repository, verbose)
}

/**
 * Add (probably) next typist to notification content, if can guess.
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   settings        Use base and wip branch
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 */
fun showNextTypist(
    settings: MobProjectSettings,
    repository: GitRepository,
    notifyContents: MutableList<String>,
    verbose: Boolean = false
) {
    val command = GitCommand.LOG
    val from = settings.baseBranch
    val to = settings.wipBranch
    val options = listOf("$from..$to", "--pretty=format:%an", "--abbrev-commit")

    val authors = git(command, options, repository, verbose)
    logger.debug("there have been ${authors.size} changes")

    val gitUserName = gitGitUserName(repository)
    logger.debug("current git user.name is '$gitUserName'")

    var foundAnotherAuthor = false
    for (i in 1 until authors.size) {
        if (authors[i].trim().equals(gitUserName)) {
            if (!foundAnotherAuthor) {
                continue
            }
            val history = StringBuilder()
            for (j in (i - 1) downTo 0) {
                history.append(authors[j].trim())
                if (j != 0) {
                    history.append(", ")
                }
            }
            val notifyFormat = MobBundle.message("mob.notify_content.notify")
            notifyContents.add(String.format(notifyFormat, "Committers after your last commit: $history"))
            notifyContents.add(String.format(notifyFormat, "***${authors[i - 1].trim()}*** is (probably) next."))
            return
        } else {
            foundAnotherAuthor = true
        }
    }
}