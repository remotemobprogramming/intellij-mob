/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git config --get user.name
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  git user name
 */
fun gitUserName(repository: GitRepository, verbose: Boolean = false): String {
    val command = GitCommand.CONFIG
    val options = listOf("--get", "user.name")

    val output = git(command, options, repository, verbose)
    if (output.isNotEmpty()) {
        return output[0].trim()
    } else {
        return String()
    }
}

/**
 * git config --get user.email
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   repository      Git repository
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  git user name
 */
fun gitUserEmail(repository: GitRepository, verbose: Boolean = false): String {
    val command = GitCommand.CONFIG
    val options = listOf("--get", "user.email")

    val output = git(command, options, repository, verbose)
    if (output.isNotEmpty()) {
        return output[0].trim()
    } else {
        return String()
    }
}