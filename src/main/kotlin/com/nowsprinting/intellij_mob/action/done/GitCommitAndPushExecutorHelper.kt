/*
 * Copyright 2020-2022 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.done

import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitChangeListDialog
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.util.notify

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.action.done.GitCommitAndPushExecutorHelperKt")

/**
 * Open git commit & push dialog.
 *
 * TODO: select modal dialog or non-modal tool window
 * Settings/Preferences | Version Control | Commit, select "Use non-modal commit interface"
 */
fun openGitCommitAndPushDialog(project: Project, coAuthors: Set<String>) {
    ApplicationManager.getApplication().invokeLater({
        try {
            val changes = ChangeListManager.getInstance(project).allChanges
            val initialSelection = LocalChangeList.createEmptyChangeList(project, LocalChangeList.getDefaultName())
            val initialCommitMessage = StringBuilder(MobBundle.message("mob.done.commit_dialog.initial_commit_message"))
            for (author in coAuthors) {
                initialCommitMessage.append("%nCo-authored-by: $author")
            }

            logger.debug(MobBundle.message("mob.done.commit_dialog.open_start"))
            CommitChangeListDialog.commitChanges(
                project,
                changes,
                initialSelection,
                null,
                String.format(initialCommitMessage.toString())
            )
            logger.debug(MobBundle.message("mob.done.commit_dialog.closed"))

        } catch (t: Throwable) {
            val message = String.format(MobBundle.message("mob.done.commit_dialog.open_failure"), t.toString())
            logger.error(message, t)
            notify(
                project = project,
                title = MobBundle.message("mob.done.please_commit_and_push"),
                content = String.format("%n%s", message),
                type = NotificationType.WARNING
            )
        }
    }, ModalityState.defaultModalityState())
}