/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.git

import com.nowsprinting.intellij_mob.MobBundle
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

        val expected = listOf<String>()
        assertEquals(expected, notifyContents)
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

        val expected = listOf<String>()
        assertEquals(expected, notifyContents)
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

        val expected = listOf<String>()
        assertEquals(expected, notifyContents)
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

        val notifyFormat = MobBundle.message("mob.notify_content.notify")
        val expected = listOf(
            String.format(notifyFormat, "Committers after your last commit: Lindsay, Jenny, Katsuro"),
            String.format(notifyFormat, "***Lindsay*** is (probably) next.")
        )
        assertEquals(expected, notifyContents)
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

        val notifyFormat = MobBundle.message("mob.notify_content.notify")
        val expected = listOf(
            String.format(notifyFormat, "Committers after your last commit: Lindsay, Jenny, Katsuro, Katsuro"),
            String.format(notifyFormat, "***Lindsay*** is (probably) next.")
        )
        assertEquals(expected, notifyContents)
    }

    @Test
    fun getCoAuthors_nothingCommit_nothingCoAuthors() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an <%ae>", "--abbrev-commit"), repository, false)
        } returns listOf()  // nothing commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.email"), repository, false)
        } returns listOf("katsuro@example.com")

        val actual = getCoAuthors(settings, repository)
        val expected = setOf<String>()
        assertEquals(expected, actual)
    }

    @Test
    fun getCoAuthors_anyCommit_getCoAuthors() {
        val repository = DummyGitRepository()
        val settings = MobProjectSettings()
        settings.baseBranch = "base"
        settings.wipBranch = "wip"

        mockkStatic("com.nowsprinting.intellij_mob.git.GitCommandUtilKt")
        every {
            git(GitCommand.LOG, listOf("base..wip", "--pretty=format:%an <%ae>", "--abbrev-commit"), repository, false)
        } returns listOf(
            "Jenny <jenny@example.com>",
            "Lindsay <lindsay@example.com>",
            "Katsuro <katsuro@example.com>",
            "Jenny <jenny@example.com>",
            "Lindsay <lindsay@example.com>",
            "Katsuro <katsuro@example.com>"
        )  // exist past commit

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.name"), repository, false)
        } returns listOf("Katsuro")

        every {
            git(GitCommand.CONFIG, listOf("--get", "user.email"), repository, false)
        } returns listOf("katsuro@example.com")

        val actual = getCoAuthors(settings, repository)
        val expected = setOf(
            "Lindsay <lindsay@example.com>",
            "Jenny <jenny@example.com>"
        )
        assertEquals(expected, actual)
    }
}