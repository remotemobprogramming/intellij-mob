package com.nowsprinting.intellij_mob.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MobProjectSettingsTest {

    @Test
    void noStateLoaded_setDEfault() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();

        assertEquals(sut.wipBranch, "mob-session");
        assertEquals(sut.baseBranch, "master");
        assertEquals(sut.remoteName, "origin");
        assertFalse(sut.debug);
        assertEquals(sut.timer, 10);
        assertFalse(sut.startWithShare);
        assertFalse(sut.nextAtExpire);
        assertEquals(sut.wipCommitMessage, "mob next [ci-skip]");
        assertFalse(sut.nextStay);
    }
}