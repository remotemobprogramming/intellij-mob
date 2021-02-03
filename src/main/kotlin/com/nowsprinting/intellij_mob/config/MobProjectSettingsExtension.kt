/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.config

import com.nowsprinting.intellij_mob.MobBundle

private fun MobProjectSettings.validateCommonPrecondition(): Pair<Boolean, String?> {
    if (remoteName.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.validate_reason.unset_remote_name"))
    }
    if (baseBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.validate_reason.unset_base_branch"))
    }
    if (wipBranch.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.validate_reason.unset_wip_branch"))
    }
    return Pair(true, null)
}

fun MobProjectSettings.validateForStartPrecondition(): Pair<Boolean, String?> {
    return validateCommonPrecondition()
}

fun MobProjectSettings.validateForStartTask(): Pair<Boolean, String?> {
    return validateForStartPrecondition()
}

fun MobProjectSettings.validateForNextPrecondition(): Pair<Boolean, String?> {
    return validateCommonPrecondition()
}

fun MobProjectSettings.validateForNextTask(): Pair<Boolean, String?> {
    val (valid, reason) = validateForNextPrecondition()
    if (!valid) {
        return Pair(valid, reason)
    }
    if (wipCommitMessage.isNullOrEmpty()) {
        return Pair(false, MobBundle.message("mob.validate_reason.unset_wip_commit_message"))
    }
    return Pair(true, null)
}

fun MobProjectSettings.validateForDonePrecondition(): Pair<Boolean, String?> {
    return validateCommonPrecondition()
}

fun MobProjectSettings.validateForDoneTask(): Pair<Boolean, String?> {
    return validateForDonePrecondition()
}

fun MobProjectSettings.validateForResetPrecondition(): Pair<Boolean, String?> {
    return validateCommonPrecondition()
}

fun MobProjectSettings.validateForResetTask(): Pair<Boolean, String?> {
    return validateForResetPrecondition()
}