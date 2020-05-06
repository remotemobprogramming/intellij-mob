/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.testdouble

import com.intellij.openapi.vcs.LocalFilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision
import com.intellij.openapi.vcs.changes.FakeRevision
import com.intellij.openapi.vfs.VirtualFile

fun createFakeChange(): FakeChange {
    val beforeRevision = FakeRevision(LocalFilePath("/path/to/before", false))
    val afterRevision = FakeRevision(LocalFilePath("/path/to/after", false))
    return FakeChange(beforeRevision, afterRevision)
}

class FakeChange(beforeRevision: ContentRevision, afterRevision: ContentRevision) :
    Change(beforeRevision, afterRevision) {

    override fun getType(): Type {
        return Type.NEW
    }

    override fun getVirtualFile(): VirtualFile? {
        return null
    }
}