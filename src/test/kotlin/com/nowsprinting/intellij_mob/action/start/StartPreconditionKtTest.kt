/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.start

import com.intellij.dvcs.repo.Repository
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.vcs.log.Hash
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.testdouble.*
import git4idea.GitLocalBranch
import git4idea.GitRemoteBranch
import git4idea.GitStandardRemoteBranch
import git4idea.branch.GitBranchesCollection
import git4idea.repo.GitBranchTrackInfo
import git4idea.repo.GitRemote
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class StartPreconditionKtTest {

    private class StubGitRepository(
        val remoteSet: MutableCollection<GitRemote>?,
        val remoteBranches: Collection<GitRemoteBranch>?,
        val localBranches: Collection<GitLocalBranch>?,
        val trackedRemoteBranches: Collection<GitRemoteBranch>?,
        val current: GitLocalBranch?,
        val repositoryState: Repository.State = Repository.State.NORMAL
    ) : DummyGitRepository() {
        val dummyProject = DummyProject()

        override fun getRemotes(): MutableCollection<GitRemote> {
            return remoteSet!!
        }

        override fun getBranches(): GitBranchesCollection {
            val remoteBranchesMap = hashMapOf<GitRemoteBranch, Hash>()
            remoteBranches?.let {
                for (branch in it) {
                    remoteBranchesMap[branch] = DummyHash()
                }
            }
            val localBranchesMap = hashMapOf<GitLocalBranch, Hash>()
            localBranches?.let {
                for (branch in it) {
                    localBranchesMap[branch] = DummyHash()
                }
            }
            return GitBranchesCollection(localBranchesMap, remoteBranchesMap)
        }

        override fun getBranchTrackInfo(localBranchName: String): GitBranchTrackInfo? {
            trackedRemoteBranches?.let {
                for (remoteBranch in it) {
                    if (remoteBranch.name.endsWith(localBranchName)) {
                        return GitBranchTrackInfo(
                            GitLocalBranch(localBranchName),
                            remoteBranch,
                            false
                        )
                    }
                }
            }
            return null
        }

        override fun getCurrentBranch(): GitLocalBranch? {
            return current
        }

        override fun getState(): Repository.State {
            return repositoryState
        }

        override fun getProject(): Project {
            return dummyProject
        }
    }

    private class StubChangeListManager(val changes: MutableCollection<Change>) : DummyChangeListManager() {
        override fun getAllChanges(): MutableCollection<Change> {
            return changes
        }
    }

    private fun createSettings(): MobProjectSettings {
        val settings = MobProjectSettings()
        settings.noStateLoaded()
        return settings
    }

    @Test
    fun checkStartPrecondition_noRemoteRepository_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(), // not set origin
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = null,
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.not_exist_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_remoteNameIsNotExist_failure() {
        val settings = createSettings()
        val notOrigin = GitRemote("not_origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(notOrigin, "master")
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(notOrigin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = null,
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.not_exist_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseBranchIsNotExist_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteNotMaster = GitStandardRemoteBranch(origin, "not_master")
        val localNotMaster = GitLocalBranch("not_master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteNotMaster),
            localBranches = setOf(localNotMaster),
            trackedRemoteBranches = null,
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_noBranches_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(),  // not set
            localBranches = setOf(), // not set
            trackedRemoteBranches = null,
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_existLocalBranchButNotExistRemote_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(),  // not set
            localBranches = setOf(localMaster),
            trackedRemoteBranches = null,
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseHasNotValidTrackedBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = null,   // has not tracked remote branch
            current = null
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.base_branch_has_not_valid_upstream"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_currentHasNotValidTrackedBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = setOf(),
            remoteBranches = setOf(remoteMaster),
            trackedRemoteBranches = null,
            current = localMaster
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.current_branch_has_not_valid_upstream"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_repositoryStateIsNotNormal_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = setOf(remoteMaster),
            current = null,
            repositoryState = Repository.State.MERGING
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.has_uncommitted_changes"), errorMessage)
    }

    @Disabled("ApplicationManager.getApplication() returns null in tests. Since 2020.3")
    @Test
    fun checkStartPrecondition_hasUncommittedChanges_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = setOf(remoteMaster),
            current = null,
            repositoryState = Repository.State.NORMAL
        )

        mockkStatic(ChangeListManager::class)
        every {
            ChangeListManager.getInstance(any())
        } returns StubChangeListManager(mutableListOf(createFakeChange()))  // has changes

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.validate_reason.has_uncommitted_changes"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_nothingLocalBranchBranch_success() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(), // not set
            trackedRemoteBranches = null,
            current = null,
            repositoryState = Repository.State.NORMAL
        )

        mockkStatic(ChangeListManager::class)
        every {
            ChangeListManager.getInstance(any())
        } returns StubChangeListManager(mutableListOf())    // no changes

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertTrue(canExecute)
        assertNull(errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseAndCurrentHasValidTrackedBranch_success() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster),
            localBranches = setOf(localMaster),
            trackedRemoteBranches = setOf(remoteMaster),
            current = localMaster,
            repositoryState = Repository.State.NORMAL
        )

        mockkStatic(ChangeListManager::class)
        every {
            ChangeListManager.getInstance(any())
        } returns StubChangeListManager(mutableListOf())    // no changes

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertTrue(canExecute)
        assertNull(errorMessage)
    }
}