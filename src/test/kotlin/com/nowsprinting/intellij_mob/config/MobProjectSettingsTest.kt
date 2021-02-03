/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MobProjectSettingsTest {

    @Test
    fun noStateLoaded_setDefault() {
        val sut = MobProjectSettings()
        sut.noStateLoaded()
        Assertions.assertEquals(sut.wipBranch, "mob-session")
        Assertions.assertEquals(sut.baseBranch, "master")
        Assertions.assertEquals(sut.remoteName, "origin")
        Assertions.assertEquals(sut.timerMinutes, 10)
        Assertions.assertFalse(sut.startWithShare)
        Assertions.assertEquals(sut.wipCommitMessage, "mob next [ci-skip]")
        Assertions.assertTrue(sut.nextStay)
    }
}