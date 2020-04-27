package com.nowsprinting.intellij_mob.git

import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.FakeLogger
import git4idea.repo.GitRemote
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class FetchKtTest {

    private class StubGitRepository(val remoteSet: MutableSet<GitRemote>) : DummyGitRepository() {
        override fun getRemotes(): MutableCollection<GitRemote> {
            return remoteSet
        }
    }

    @Disabled("No integration tests fixture")
    @Test
    fun fetch_success() {
        TODO("Not yet implemented")
    }

    @Test
    fun fetch_repositoryHasNotRemote_failure() {
        val emptyRemotes = mutableSetOf<GitRemote>()
        val repository = StubGitRepository(emptyRemotes)

        val actual = fetch(repository, FakeLogger())
        kotlin.test.assertTrue(actual is FetchResult.Failure)
        kotlin.test.assertEquals("Repository not has remote", actual.reason)
    }
}