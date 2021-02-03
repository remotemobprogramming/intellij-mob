/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git commit --message $message --no-verify
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   message         commit message
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true: Git command successful
 */
fun commit(
    message: String, repository: GitRepository, notifyContents: MutableList<String>? = null, verbose: Boolean = false
): Boolean {
    val command = GitCommand.COMMIT
    val options = listOf("--message", "\"$message\"", "--no-verify")

    return git(command, options, repository, notifyContents, verbose)
}