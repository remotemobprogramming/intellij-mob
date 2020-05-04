package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.diagnostic.Logger
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.util.notifyError
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.GitCommandUtilKt")

/**
 * Execute git command.
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   command         Execute git command
 * @param   options         Git command options
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true: Git command successful
 */
fun git(
    command: GitCommand,
    options: List<String> = listOf(),
    repository: GitRepository,
    notifyContents: MutableList<String>? = null,
    verbose: Boolean = false
): Boolean {
    val commandFull = StringBuilder("git $command")
    for (v in options) {
        commandFull.append(" ").append(v)
    }
    logger.debug(String.format(MobBundle.message("mob.notify_content.begin"), commandFull))

    val handler = GitLineHandler(repository.project, repository.root, command)
    handler.addParameters(options)
    if (verbose) {
        handler.addParameters("--verbose")
    }

    val result = Git.getInstance().runCommand(handler)
    if (result.output.isNotEmpty()) {
        logger.debug(result.outputAsJoinedString)
    }

    if (!result.success()) {
        val gitErrorMessage = result.errorOutputAsJoinedString
        notifyError(gitErrorMessage)

        val message = String.format(MobBundle.message("mob.notify_content.failure"), commandFull)
        notifyContents?.add(message)
        logger.error("$message%n$gitErrorMessage".format())
        return false
    }

    val message = String.format(MobBundle.message("mob.notify_content.success"), commandFull)
    notifyContents?.add(message)
    logger.info(message)
    return true
}

/**
 * Execute git command and returns output.
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   command         Execute git command
 * @param   options         Git command options
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  output
 */
fun git(
    command: GitCommand,
    options: List<String> = listOf(),
    repository: GitRepository,
    verbose: Boolean = false
): List<String> {
    val commandFull = StringBuilder("git $command")
    for (v in options) {
        commandFull.append(" ").append(v)
    }
    logger.debug(String.format(MobBundle.message("mob.notify_content.begin"), commandFull))

    val handler = GitLineHandler(repository.project, repository.root, command)  // maybe with "--no-pager"
    handler.addParameters(options)
    if (verbose) {
        handler.addParameters("--verbose")
    }

    val result = Git.getInstance().runCommand(handler)
    if (result.output.isNotEmpty()) {
        logger.debug(result.outputAsJoinedString)
    }

    if (!result.success()) {
        val gitErrorMessage = result.errorOutputAsJoinedString
        notifyError(gitErrorMessage)

        val message = String.format(MobBundle.message("mob.notify_content.failure"), commandFull)
        logger.error("$message%n$gitErrorMessage".format())
        return emptyList()
    }

    val message = String.format(MobBundle.message("mob.notify_content.success"), commandFull)
    logger.info(message)
    return result.output
}