package com.nowsprinting.intellij_mob.git

import com.intellij.openapi.vfs.VirtualFile
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import com.nowsprinting.intellij_mob.testdouble.DummyVirtualFile
import com.nowsprinting.intellij_mob.testdouble.FakeLogger
import git4idea.repo.GitRepositoryManager
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GitRepositoryKtTest {

    private class StubGitRepository(val repoPath: String) : DummyGitRepository() {
        private class StubVirtualFile(val repoPath: String) : DummyVirtualFile() {
            override fun getPath(): String {
                return repoPath
            }
        }

        override fun getRoot(): VirtualFile {
            return StubVirtualFile(repoPath)
        }
    }

    @Test
    fun getRepository_success() {
        val mockRepositoryManager = mockk<GitRepositoryManager>()
        every { mockRepositoryManager.repositories } returns listOf(
            StubGitRepository("/path/to/repository")
        )

        val actual = getGitRepository(
            mockRepositoryManager,
            FakeLogger()
        )
        Assertions.assertTrue(actual is GitRepositoryResult.Success)
    }

    @Test
    fun getRepository_noRepository_failure() {
        val mockRepositoryManager = mockk<GitRepositoryManager>()
        every { mockRepositoryManager.repositories } returns listOf()

        val actual = getGitRepository(
            mockRepositoryManager,
            FakeLogger()
        )
        kotlin.test.assertTrue(actual is GitRepositoryResult.Failure)
        kotlin.test.assertEquals("Repository not found in this project", actual.reason)
    }

    @Test
    fun getRepository_manyRepository_failure() {
        val mockRepositoryManager = mockk<GitRepositoryManager>()
        every { mockRepositoryManager.repositories } returns listOf(
            StubGitRepository("/path/to/repository-1"),
            StubGitRepository("/path/to/repository-2")
        )

        val actual = getGitRepository(
            mockRepositoryManager,
            FakeLogger()
        )
        kotlin.test.assertTrue(actual is GitRepositoryResult.Failure)
        kotlin.test.assertEquals("Has a multiple repositories in this project", actual.reason)
    }
}