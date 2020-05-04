package com.nowsprinting.intellij_mob.util

import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.git.isMobProgramming
import git4idea.repo.GitRepository

/**
 * Response `mob status` command
 *
 * Must be called from `Task.Backgroundable#run()`.
 * If an error occurs, show a notification within this function.
 *
 * @return message for notification content
 */
fun status(repository: GitRepository, settings: MobProjectSettings): String {
    val notifyFormat = MobBundle.message("mob.notify_content.notify")
    val message = StringBuilder()

    if (repository.isMobProgramming(settings)) {
        message.append(String.format(notifyFormat, MobBundle.message("mob.status.is_mob_programming")))
        // TODO: say(silentgit("--no-pager", "log", baseBranch+".."+wipBranch, "--pretty=format:%h %cr <%an>", "--abbrev-commit"))
    } else {
        message.append(String.format(notifyFormat, MobBundle.message("mob.status.is_not_mob_programming")))
    }
    return message.toString()
}