/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import com.intellij.vcs.log.Hash
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.DummyHash
import git4idea.GitLocalBranch
import git4idea.GitRemoteBranch
import git4idea.GitStandardRemoteBranch
import git4idea.branch.GitBranchesCollection
import git4idea.repo.GitRemote
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GitRepositoryExtensionKtTest {

    private class StubGitRepository(
        val localBranches: MutableCollection<GitLocalBranch>?,
        val remoteBranches: MutableCollection<GitRemoteBranch>?,
        val current: GitLocalBranch? = null
    ) : DummyGitRepository() {

        override fun getBranches(): GitBranchesCollection {
            val localBranchesMap = hashMapOf<GitLocalBranch, Hash>()
            localBranches?.let {
                for (branch in it) {
                    localBranchesMap[branch] = DummyHash()
                }
            }
            val remoteBranchesMap = hashMapOf<GitRemoteBranch, Hash>()
            remoteBranches?.let {
                for (branch in it) {
                    remoteBranchesMap[branch] = DummyHash()
                }
            }
            return GitBranchesCollection(localBranchesMap, remoteBranchesMap)
        }

        override fun getCurrentBranch(): GitLocalBranch? {
            return current
        }

        override fun getCurrentBranchName(): String? {
            return current?.name
        }
    }

    @Test
    fun hasMobProgrammingBranch_found_true() {
        val localBranch = GitLocalBranch("mob-session")
        val repository = StubGitRepository(
            mutableSetOf(localBranch),
            mutableSetOf()
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        val actual = repository.hasMobProgrammingBranch(settings)
        assertTrue(actual)
    }

    @Test
    fun hasMobProgrammingBranch_notFound_false() {
        val localBranch = GitLocalBranch("mob-session")
        val repository = StubGitRepository(
            mutableSetOf(localBranch),
            mutableSetOf()
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "not-exist-branch"
        val actual = repository.hasMobProgrammingBranch(settings)
        assertFalse(actual)
    }

    @Test
    fun hasMobProgrammingBranch_noLocalBranch_false() {
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf()
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        val actual = repository.hasMobProgrammingBranch(settings)
        assertFalse(actual)
    }

    @Test
    fun hasMobProgrammingBranchOrigin_found_true() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "mob-session")
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(remoteBranch)
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        settings.remoteName = "origin"
        val actual = repository.hasMobProgrammingBranchOrigin(settings)
        assertTrue(actual)
    }

    @Test
    fun hasMobProgrammingBranchOrigin_remoteNotFound_false() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "mob-session")
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(remoteBranch)
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        settings.remoteName = "not-exist-origin"
        val actual = repository.hasMobProgrammingBranchOrigin(settings)
        assertFalse(actual)
    }

    @Test
    fun hasMobProgrammingBranchOrigin_branchNotFound_false() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "mob-session")
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(remoteBranch)
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "not-exist-branch"
        settings.remoteName = "origin"
        val actual = repository.hasMobProgrammingBranchOrigin(settings)
        assertFalse(actual)
    }

    @Test
    fun hasMobProgrammingBranchOrigin_noRemoteBranch_false() {
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf()
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        settings.remoteName = "origin"
        val actual = repository.hasMobProgrammingBranchOrigin(settings)
        assertFalse(actual)
    }

    @Test
    fun isMobProgramming_inWipBranch_true() {
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(),
            GitLocalBranch("mob-session")
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        val actual = repository.isMobProgramming(settings)
        assertTrue(actual)
    }

    @Test
    fun isMobProgramming_notInWipBranch_false() {
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(),
            GitLocalBranch("master")
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        val actual = repository.isMobProgramming(settings)
        assertFalse(actual)
    }

    @Test
    fun isMobProgramming_currentBranchIsNull_false() {
        val repository = StubGitRepository(
            mutableSetOf(),
            mutableSetOf(),
            null
        )
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        val actual = repository.isMobProgramming(settings)
        assertFalse(actual)
    }

    // Cover validateForStartPrecondition() tests, in StartPreconditionKtTest class
    // Cover validateForNextPrecondition() tests, in NextPreconditionKtTest class
}