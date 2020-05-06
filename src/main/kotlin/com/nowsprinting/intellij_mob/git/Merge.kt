/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import git4idea.commands.GitCommand
import git4idea.repo.GitRepository

/**
 * git merge $remote/$branch --ff-only
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
fun mergeFastForward(
    remote: String,
    branch: String,
    repository: GitRepository,
    notifyContents: MutableList<String>? = null,
    verbose: Boolean = false
): Boolean {
    val command = GitCommand.MERGE
    val options = listOf("$remote/$branch", "--ff-only")

    return git(command, options, repository, notifyContents, verbose)
}

/**
 * git merge --squash --ff $branch
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @param   branch          Target branch name
 * @param   repository      Git repository
 * @param   notifyContents  Notification content when executing as a series of commands. Add results with this function.
 * @param   verbose         Add `--verbose` option (default: false)
 * @return  true: Git command successful
 */
fun mergeWithSquash(
    branch: String,
    repository: GitRepository,
    notifyContents: MutableList<String>? = null,
    verbose: Boolean = false
): Boolean {
    val command = GitCommand.MERGE
    val options = listOf("--squash", "--ff", branch)

    return git(command, options, repository, notifyContents, verbose)
}