/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

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