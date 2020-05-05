package com.nowsprinting.intellij_mob.action.done

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.nowsprinting.intellij_mob.action.done.ui.DoneDialog
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.MobSettingsConfigurable
import com.nowsprinting.intellij_mob.git.GitRepositoryResult
import com.nowsprinting.intellij_mob.git.getGitRepository
import com.nowsprinting.intellij_mob.git.stayBranch
import com.nowsprinting.intellij_mob.util.notifyError

class DoneAction : AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)

        val project = e.project ?: throw NullPointerException("AnActionEvent#getProject() was return null")
        val settings = MobProjectSettings.getInstance(project)
        val repository = when (val result = getGitRepository(project)) {
            is GitRepositoryResult.Success -> {
                result.repository
            }
            is GitRepositoryResult.Failure -> {
                notifyError(result.reason)
                return
            }
        }
        val enabled = repository.stayBranch(settings.wipBranch)
        e.presentation.isEnabled = enabled
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: throw NullPointerException("AnActionEvent#getProject() was return null")
        val settings = MobProjectSettings.getInstance(project)
        FileDocumentManager.getInstance().saveAllDocuments()
        val (canExecute, reason) = checkDonePrecondition(settings, project)

        val dialog = DoneDialog()
        dialog.title = e.presentation.text.removeSuffix("...")
        dialog.setPreconditionResult(canExecute, reason)
        dialog.pack()
        dialog.setLocationRelativeTo(null) // set on screen center
        dialog.isVisible = true

        if (dialog.isOpenSettings) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, MobSettingsConfigurable::class.java)
        }

        if (dialog.isOk) {
            DoneTask(settings, project, dialog.title).queue()
        }
    }
}