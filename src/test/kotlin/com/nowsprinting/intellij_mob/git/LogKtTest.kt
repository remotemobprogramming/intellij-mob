package com.nowsprinting.intellij_mob.git

import com.nowsprinting.intellij_mob.config.MobProjectSettings
import com.nowsprinting.intellij_mob.testdouble.DummyGitRepository
import git4idea.commands.GitCommand
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LogKtTest {

    @Test
    fun showNextTypist_nothingCommit_notFoundNextTypist() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an", "--abbrev-commit"), repository, false)
        } returns listOf()  // nothing commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        val notifyContents = mutableListOf<String>()
        showNextTypist(settings, repository, notifyContents)

        assertEquals(0, notifyContents.size)
    }

    @Test
    fun showNextTypist_firstCommit_notFoundNextTypist() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an", "--abbrev-commit"), repository, false)
        } returns listOf("Katsuro") // first commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        val notifyContents = mutableListOf<String>()
        showNextTypist(settings, repository, notifyContents)

        assertEquals(0, notifyContents.size)
    }

    @Test
    fun showNextTypist_notExistPastCommit_notFoundNextTypist() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an", "--abbrev-commit"), repository, false)
        } returns listOf("Katsuro", "Lindsay", "Jenny") // not exist past commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        val notifyContents = mutableListOf<String>()
        showNextTypist(settings, repository, notifyContents)

        assertEquals(0, notifyContents.size)
    }

    @Test
    fun showNextTypist_existPastCommit_foundNextTypist() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an", "--abbrev-commit"), repository, false)
        } returns listOf("Katsuro", "Jenny", "Lindsay", "Katsuro")  // exist past commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        val notifyContents = mutableListOf<String>()
        showNextTypist(settings, repository, notifyContents)

        assertEquals(2, notifyContents.size)
        assertEquals("\uD83D\uDCE2 Committers after your last commit: Lindsay, Jenny, Katsuro", notifyContents[0])
        assertEquals("\uD83D\uDCE2 ***Lindsay*** is (probably) next.", notifyContents[1])
    }


    @Test
    fun showNextTypist_continuousCommit_ignoreContinuousTypist() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an", "--abbrev-commit"), repository, false)
        } returns listOf("Katsuro", "Katsuro", "Jenny", "Lindsay", "Katsuro")  // continuous commits

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        val notifyContents = mutableListOf<String>()
        showNextTypist(settings, repository, notifyContents)

        assertEquals(2, notifyContents.size)
        assertEquals(
            "\uD83D\uDCE2 Committers after your last commit: Lindsay, Jenny, Katsuro, Katsuro",
            notifyContents[0]
        )
        assertEquals("\uD83D\uDCE2 ***Lindsay*** is (probably) next.", notifyContents[1])
    }
}