package com.nowsprinting.intellij_mob.testdouble

import com.intellij.vcs.log.Hash

internal open class DummyHash : Hash {
    override fun toShortString(): String {
        throw Exception("Not yet implemented")
    }

    override fun asString(): String {
        throw Exception("Not yet implemented")
    }
}