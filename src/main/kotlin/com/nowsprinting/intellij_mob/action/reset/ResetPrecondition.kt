/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.reset

import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.validateForResetPrecondition
import com.nowsprinting.intellij_mob.git.GitRepositoryResult
import com.nowsprinting.intellij_mob.git.getGitRepository
import com.nowsprinting.intellij_mob.git.validateForReset
import git4idea.repo.GitRepository

/**
 * Check precondition for mob reset command
 *
 * @return success/failure, with error message
 */
internal fun checkResetPrecondition(settings: MobProjectSettings, project: Project): Pair<Boolean, String?> {
    return when (val result = getGitRepository(project)) {
        is GitRepositoryResult.Success -> {
            result.repository
            checkResetPrecondition(settings, result.repository)
        }
        is GitRepositoryResult.Failure -> {
            Pair(false, result.reason)
        }
    }
}

internal fun checkResetPrecondition(settings: MobProjectSettings, repository: GitRepository): Pair<Boolean, String?> {
    val (validSettings, reasonInvalidSettings) = settings.validateForResetPrecondition()
    if (!validSettings) {
        return Pair(validSettings, reasonInvalidSettings)
    }
    val (validRepository, reasonInvalidRepository) = repository.validateForReset(settings)
    if (!validRepository) {
        return Pair(validRepository, reasonInvalidRepository)
    }
    return Pair(true, null)
}