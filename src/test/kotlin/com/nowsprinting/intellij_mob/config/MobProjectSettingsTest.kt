package com.nowsprinting.intellij_mob.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MobProjectSettingsTest {

    @Test
    fun noStateLoaded_setDEfault() {
        val sut = MobProjectSettings()
        sut.noStateLoaded()
        Assertions.assertEquals(sut.wipBranch, "mob-session")
        Assertions.assertEquals(sut.baseBranch, "master")
        Assertions.assertEquals(sut.remoteName, "origin")
        Assertions.assertEquals(sut.timerMinutes, 10)
        Assertions.assertFalse(sut.startWithShare)
        Assertions.assertTrue(sut.nextAtExpire)
        Assertions.assertEquals(sut.wipCommitMessage, "mob next [ci-skip]")
        Assertions.assertFalse(sut.nextStay)
    }
}