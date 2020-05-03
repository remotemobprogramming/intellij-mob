package com.nowsprinting.intellij_mob.git

import com.intellij.dvcs.repo.Repository
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vcs.changes.ChangeListManager

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.git.StatusKt")

/**
 * Check nothing to commit changes
 *
 * @return true: nothing to commit changes
 */
fun isNothingToCommit(repository: Repository): Boolean {
    if (repository.state != Repository.State.NORMAL) {
        logger.info("Repository state is ${repository.state.toString()}")
        return false
    }

    FileDocumentManager.getInstance().saveAllDocuments()

    val changes = ChangeListManager.getInstance(repository.project).allChanges
    if (changes.isNotEmpty()) {
        logger.info("Has uncommitted changes")
        for (v in changes) {
            logger.debug("  ${v.type.toString()}: ${v.virtualFile?.path}")
        }
        return false
    }
    return true
}