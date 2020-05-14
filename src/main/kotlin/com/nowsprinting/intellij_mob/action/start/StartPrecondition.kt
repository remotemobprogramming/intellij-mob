/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.start

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.config.validateForStartPrecondition
import com.nowsprinting.intellij_mob.git.GitRepositoryResult
import com.nowsprinting.intellij_mob.git.getGitRepository
import com.nowsprinting.intellij_mob.git.validateForStart
import git4idea.repo.GitRepository

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")

/**
 * Check precondition for mob start command
 *
 * @return success/failure, with error message
 */
internal fun checkStartPrecondition(settings: MobProjectSettings, project: Project): Pair<Boolean, String?> {
    return when (val result = getGitRepository(project)) {
        is GitRepositoryResult.Success -> {
            result.repository
            checkStartPrecondition(settings, result.repository)
        }
        is GitRepositoryResult.Failure -> {
            Pair(false, result.reason)
        }
    }
}

internal fun checkStartPrecondition(settings: MobProjectSettings, repository: GitRepository): Pair<Boolean, String?> {
    val (validSettings, reasonInvalidSettings) = settings.validateForStartPrecondition()
    if (!validSettings) {
        return Pair(validSettings, reasonInvalidSettings)
    }
    val (validRepository, reasonInvalidRepository) = repository.validateForStart(settings)
    if (!validRepository) {
        return Pair(validRepository, reasonInvalidRepository)
    }
    return Pair(true, null)
}