/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import com.intellij.vcs.log.Hash
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.DummyHash
import git4idea.GitLocalBranch
import git4idea.GitRemoteBranch
import git4idea.GitStandardRemoteBranch
import git4idea.branch.GitBranchesCollection
import git4idea.repo.GitBranchTrackInfo
import git4idea.repo.GitRemote
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GitLocalBranchExtensionKtTest {

    private class StubGitRepository(
        val remoteBranches: MutableCollection<GitRemoteBranch>?,
        val trackedRemoteBranch: GitRemoteBranch?
    ) : DummyGitRepository() {

        override fun getBranches(): GitBranchesCollection {
            val localBranchesMap = hashMapOf<GitLocalBranch, Hash>()
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
    }

    @Test
    fun hasValidUpstream_hasNotTrackedBranch_false() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            mutableSetOf(remoteBranch),
            null
        )
        val sut = GitLocalBranch("has_not_tracked_remote_branch")

        val actual = sut.hasValidUpstream(repository)
        assertFalse(actual)
    }

    @Test
    fun hasValidUpstream_hasNotValidTrackedBranch_false() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "master")
        val trackedRemoteBranch = GitStandardRemoteBranch(origin, "tracked")
        val repository = StubGitRepository(
            mutableSetOf(remoteBranch),
            trackedRemoteBranch
        )
        val sut = GitLocalBranch("has_not_valid_tracked_remote_branch")

        val actual = sut.hasValidUpstream(repository)
        assertFalse(actual)
    }

    @Test
    fun hasValidUpstream_hasValidTrackedBranch_true() {
        val origin = GitRemote("origin", listOf<String>(), listOf<String>(), listOf<String>(), listOf<String>())
        val remoteBranch = GitStandardRemoteBranch(origin, "master")
        val repository = StubGitRepository(
            mutableSetOf(remoteBranch),
            remoteBranch
        )
        val sut = GitLocalBranch("master")

        val actual = sut.hasValidUpstream(repository)
        assertTrue(actual)
    }
}