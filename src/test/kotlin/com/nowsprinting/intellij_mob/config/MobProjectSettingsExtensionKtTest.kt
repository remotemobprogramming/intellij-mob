/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.config

import com.nowsprinting.intellij_mob.MobBundle
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class MobProjectSettingsExtensionKtTest {

    private fun createSettings(): MobProjectSettings {
        val settings = MobProjectSettings()
        settings.remoteName = "remote"
        settings.baseBranch = "master"
        settings.wipBranch = "mob-session"
        settings.wipCommitMessage = "mob next [ci-skip]"
        return settings
    }

    @Test
    fun validateForStartTask_remoteNameIsNull_failure() {
        val settings = createSettings()
        settings.remoteName = null
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_remote_name"), reason)
    }

    @Test
    fun validateForStartTask_remoteNameIsEmpty_failure() {
        val settings = createSettings()
        settings.remoteName = ""
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_remote_name"), reason)
    }

    @Test
    fun validateForStartTask_baseBranchIsNull_failure() {
        val settings = createSettings()
        settings.baseBranch = null
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_base_branch"), reason)
    }

    @Test
    fun validateForStartTask_baseBranchIsEmpty_failure() {
        val settings = createSettings()
        settings.baseBranch = ""
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_base_branch"), reason)
    }

    @Test
    fun validateForStartTask_wipBranchIsNull_failure() {
        val settings = createSettings()
        settings.wipBranch = null
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_wip_branch"), reason)
    }

    @Test
    fun validateForStartTask_wipBranchIsEmpty_failure() {
        val settings = createSettings()
        settings.wipBranch = ""
        val (valid, reason) = settings.validateForStartTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_wip_branch"), reason)
    }

    @Test
    fun validateForStartTask_valid_success() {
        val settings = createSettings()
        settings.wipCommitMessage = null  // empty but not validate in start task
        val (valid, reason) = settings.validateForStartTask()
        Assertions.assertTrue(valid)
        Assertions.assertNull(reason)
    }

    @Test
    fun validateForNextPrecondition_valid_success() {
        val settings = createSettings()
        settings.wipCommitMessage = null  // empty but not validate in next precondition
        val (valid, reason) = settings.validateForNextPrecondition()
        Assertions.assertTrue(valid)
        Assertions.assertNull(reason)
    }

    @Test
    fun validateForNextTask_wipCommitMessageIsNull_failure() {
        val settings = createSettings()
        settings.wipCommitMessage = null
        val (valid, reason) = settings.validateForNextTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_wip_commit_message"), reason)
    }

    @Test
    fun validateForNextTask_wipCommitMessageIsEmpty_failure() {
        val settings = createSettings()
        settings.wipCommitMessage = ""
        val (valid, reason) = settings.validateForNextTask()
        assertFalse(valid)
        assertEquals(MobBundle.message("mob.validate_reason.unset_wip_commit_message"), reason)
    }

    @Test
    fun validateForNextTask_valid_success() {
        val settings = createSettings()
        val (valid, reason) = settings.validateForNextTask()
        Assertions.assertTrue(valid)
        Assertions.assertNull(reason)
    }
}