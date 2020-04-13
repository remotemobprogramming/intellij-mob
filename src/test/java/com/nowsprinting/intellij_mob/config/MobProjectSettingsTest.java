package com.nowsprinting.intellij_mob.config;

import org.jdom.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MobProjectSettingsTest {

    @Test
    void loadState_setValues() {
        Element element = new Element(MobProjectSettings.MOB_SETTINGS);
        element.setAttribute(MobProjectSettings.KEY_WIP_BRANCH, "wip");
        element.setAttribute(MobProjectSettings.KEY_BASE_BRANCH, "base");
        element.setAttribute(MobProjectSettings.KEY_REMOTE_NAME, "remote");
        element.setAttribute(MobProjectSettings.KEY_TIMER, "100");
        element.setAttribute(MobProjectSettings.KEY_WIP_COMMIT_MESSAGE, "commit");

        MobProjectSettings sut = new MobProjectSettings();
        sut.loadState(element);

        assertEquals(sut.wipBranch, "wip");
        assertEquals(sut.baseBranch, "base");
        assertEquals(sut.remoteName, "remote");
        assertEquals(sut.timer, 100);
        assertEquals(sut.wipCommitMessage, "commit");
    }

    @Test
    void loadState_setDebug() {
        Element element = new Element(MobProjectSettings.MOB_SETTINGS);
        element.setAttribute(MobProjectSettings.KEY_DEBUG, "true");

        MobProjectSettings sut = new MobProjectSettings();
        sut.loadState(element);

        assertTrue(sut.debug);
    }

    @Test
    void loadState_setStartWithShare() {
        Element element = new Element(MobProjectSettings.MOB_SETTINGS);
        element.setAttribute(MobProjectSettings.KEY_START_WITH_SHARE, "true");

        MobProjectSettings sut = new MobProjectSettings();
        sut.loadState(element);

        assertTrue(sut.startWithShare);
    }

    @Test
    void loadState_setNextAtExpire() {
        Element element = new Element(MobProjectSettings.MOB_SETTINGS);
        element.setAttribute(MobProjectSettings.KEY_NEXT_AT_EXPIRE, "true");

        MobProjectSettings sut = new MobProjectSettings();
        sut.loadState(element);

        assertTrue(sut.nextAtExpire);
    }

    @Test
    void loadState_setNextStay() {
        Element element = new Element(MobProjectSettings.MOB_SETTINGS);
        element.setAttribute(MobProjectSettings.KEY_NEXT_STAY, "true");

        MobProjectSettings sut = new MobProjectSettings();
        sut.loadState(element);

        assertTrue(sut.nextStay);
    }

    @Test
    void getState_getValues() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();
        sut.wipBranch = "wip";
        sut.baseBranch = "base";
        sut.remoteName = "remote";
        sut.timer = 100;
        sut.wipCommitMessage = "commit";

        Element element = sut.getState();
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_WIP_BRANCH), "wip");
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_BASE_BRANCH), "base");
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_REMOTE_NAME), "remote");
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_TIMER), "100");
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_WIP_COMMIT_MESSAGE), "commit");
    }

    @Test
    void getState_getDebug() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();
        sut.debug = true;

        Element element = sut.getState();
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_DEBUG), "true");
    }

    @Test
    void getState_getStartWithShare() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();
        sut.startWithShare = true;

        Element element = sut.getState();
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_START_WITH_SHARE), "true");
    }

    @Test
    void getState_getNextAtExpire() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();
        sut.nextAtExpire = true;

        Element element = sut.getState();
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_NEXT_AT_EXPIRE), "true");
    }

    @Test
    void getState_getNextStay() {
        MobProjectSettings sut = new MobProjectSettings();
        sut.noStateLoaded();
        sut.nextStay = true;

        Element element = sut.getState();
        assertEquals(element.getAttributeValue(MobProjectSettings.KEY_NEXT_STAY), "true");
    }

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