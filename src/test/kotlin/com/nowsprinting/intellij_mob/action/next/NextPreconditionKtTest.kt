/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.next

import com.intellij.dvcs.repo.Repository
import com.intellij.vcs.log.Hash
import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.DummyHash
import git4idea.GitLocalBranch
import git4idea.GitRemoteBranch
import git4idea.GitStandardRemoteBranch
import git4idea.branch.GitBranchesCollection
import git4idea.repo.GitBranchTrackInfo
import git4idea.repo.GitRemote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class NextPreconditionKtTest {

    private class StubGitRepository(
        val remoteSet: MutableCollection<GitRemote>?,
        val remoteBranches: Collection<GitRemoteBranch>?,
        val localBranches: Collection<GitLocalBranch>?,
        val trackedRemoteBranches: Collection<GitRemoteBranch>?,
        val current: GitLocalBranch?,
        val repositoryState: Repository.State = Repository.State.NORMAL
    ) : DummyGitRepository() {

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

        override fun getCurrentBranchName(): String? {
            return current?.name
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
    fun checkNextPrecondition_notStayWipBranch_failure() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val remoteWip = GitStandardRemoteBranch(origin, "mob-session")
        val localMaster = GitLocalBranch("master")
        val localWip = GitLocalBranch("mob-session")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster, remoteWip),
            localBranches = setOf(localMaster, localWip),
            trackedRemoteBranches = setOf(remoteMaster, remoteWip),
            current = localMaster   // not stay wip branch
        )

        val (canExecute, errorMessage) = checkNextPrecondition(settings, repository)
        assertFalse(canExecute)
        assertEquals("you aren't mob programming, current branch is not mob-session", errorMessage)
    }

    @Test
    fun checkNextPrecondition_stayWipBranch_success() {
        val settings = createSettings()
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteMaster = GitStandardRemoteBranch(origin, "master")
        val remoteWip = GitStandardRemoteBranch(origin, "mob-session")
        val localMaster = GitLocalBranch("master")
        val localWip = GitLocalBranch("mob-session")
        val repository = StubGitRepository(
            remoteSet = mutableSetOf(origin),
            remoteBranches = setOf(remoteMaster, remoteWip),
            localBranches = setOf(localMaster, localWip),
            trackedRemoteBranches = setOf(remoteWip),
            current = localWip
        )

        val (canExecute, errorMessage) = checkNextPrecondition(settings, repository)
        assertTrue(canExecute)
        assertNull(errorMessage)
    }
}