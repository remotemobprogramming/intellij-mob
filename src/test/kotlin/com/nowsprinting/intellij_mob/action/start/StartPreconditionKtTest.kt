package com.nowsprinting.intellij_mob.action.start

import com.intellij.dvcs.repo.Repository
import com.intellij.vcs.log.Hash
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.DummyHash
import git4idea.GitLocalBranch
import git4idea.GitRemoteBranch
import git4idea.GitStandardRemoteBranch
import git4idea.branch.GitBranchesCollection
import git4idea.repo.GitBranchTrackInfo
import git4idea.repo.GitRemote
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StartPreconditionKtTest {

    private class StubGitRepository(
        val remoteSet: MutableCollection<GitRemote>?,
        val localBranches: MutableCollection<GitLocalBranch>?,
        val remoteBranches: MutableCollection<GitRemoteBranch>?,
        val trackedRemoteBranch: GitRemoteBranch?,
        val current: GitLocalBranch?,
        val repositoryState: Repository.State = Repository.State.NORMAL
    ) : DummyGitRepository() {

        override fun getRemotes(): MutableCollection<GitRemote> {
            return remoteSet!!
        }

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

        override fun getBranchTrackInfo(localBranchName: String): GitBranchTrackInfo? {
            trackedRemoteBranch?.let {
                return GitBranchTrackInfo(
                    GitLocalBranch(localBranchName),
                    it,
                    false
                )
            }
            return null
        }

        override fun getCurrentBranch(): GitLocalBranch? {
            return current
        }

        override fun getState(): Repository.State {
            return repositoryState
        }
    }

    private fun createSettings(): MobProjectSettings {
        val settings = MobProjectSettings()
        settings.noStateLoaded()
        return settings
    }

    @Test
    fun checkStartPrecondition_wipBranchIsNull_failure() {
        val settings = createSettings()
        settings.wipBranch = null
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_wip_branch"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_wipBranchIsEmpty_failure() {
        val settings = createSettings()
        settings.wipBranch = ""
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_wip_branch"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseBranchIsNull_failure() {
        val settings = createSettings()
        settings.baseBranch = null
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_base_branch"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseBranchIsEmpty_failure() {
        val settings = createSettings()
        settings.baseBranch = ""
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_base_branch"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_remoteNameIsNull_failure() {
        val settings = createSettings()
        settings.remoteName = null
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_remoteNameIsEmpty_failure() {
        val settings = createSettings()
        settings.remoteName = ""
        val (canExecute, errorMessage) = checkStartPrecondition(settings, DummyGitRepository())
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.unset_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_noRemoteRepository_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(), // not set origin
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = null,
            current = null
        )
        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.not_exist_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_remoteNameIsNotExist_failure() {
        val settings = createSettings()
        val notOrigin = GitRemote("not_origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(notOrigin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(notOrigin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = null,
            current = null
        )
        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.not_exist_remote_name"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseBranchIsNotExist_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localNotMaster = GitLocalBranch("not_master")
        val remoteNotMaster = GitStandardRemoteBranch(origin, "not_master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localNotMaster),
            remoteBranches = mutableSetOf(remoteNotMaster),
            trackedRemoteBranch = null,
            current = null
        )
        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_noBranches_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(), // not set
            remoteBranches = mutableSetOf(),  // not set
            trackedRemoteBranch = null,
            current = null
        )
        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_existLocalBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(),  // not set
            trackedRemoteBranch = null,
            current = null
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns true

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.not_exist_base_branch_on_remote"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_repositoryStateIsNotNormal_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = remoteMaster,
            current = null,
            repositoryState = Repository.State.MERGING
        )

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.has_uncommitted_changes"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_hasUncommittedChanges_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = remoteMaster,
            current = null
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns false

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.has_uncommitted_changes"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_existRemoteBranch_success() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(), // not set
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = null,
            current = null
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns true

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertTrue(canExecute)
        assertNull(errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseAndCurrentHasValidTrackedBranch_success() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = remoteMaster,
            current = localMaster
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns true

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertTrue(canExecute)
        assertNull(errorMessage)
    }

    @Test
    fun checkStartPrecondition_baseHasNotValidTrackedBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(localMaster),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = null,
            current = localMaster
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns true

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.base_branch_has_not_valid_upstream"), errorMessage)
    }

    @Test
    fun checkStartPrecondition_currentHasNotValidTrackedBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val localMaster = GitLocalBranch("master")
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            localBranches = mutableSetOf(),
            remoteBranches = mutableSetOf(remoteMaster),
            trackedRemoteBranch = null,
            current = localMaster
        )

        mockkStatic("com.nowsprinting.intellij_mob.action.start.StartPreconditionKt")
        every {
            isNothingToCommit(repository)
        } returns true

        val (canExecute, errorMessage) = checkStartPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals(MobBundle.message("mob.start.error.reason.current_branch_has_not_valid_upstream"), errorMessage)
    }
}