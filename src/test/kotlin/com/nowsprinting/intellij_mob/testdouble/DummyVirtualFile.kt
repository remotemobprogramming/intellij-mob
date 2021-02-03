/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.testdouble

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.InputStream
import java.io.OutputStream

internal open class DummyVirtualFile : VirtualFile() {
    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) {
        throw Exception("Not yet implemented")
    }

    override fun getLength(): Long {
        throw Exception("Not yet implemented")
    }

    override fun getFileSystem(): VirtualFileSystem {
        throw Exception("Not yet implemented")
    }

    override fun getPath(): String {
        throw Exception("Not yet implemented")
    }

    override fun isDirectory(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getTimeStamp(): Long {
        throw Exception("Not yet implemented")
    }

    override fun getName(): String {
        throw Exception("Not yet implemented")
    }

    override fun contentsToByteArray(): ByteArray {
        throw Exception("Not yet implemented")
    }

    override fun isValid(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getInputStream(): InputStream {
        throw Exception("Not yet implemented")
    }

    override fun getParent(): VirtualFile {
        throw Exception("Not yet implemented")
    }

    override fun getChildren(): Array<VirtualFile> {
        throw Exception("Not yet implemented")
    }

    override fun isWritable(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream {
        throw Exception("Not yet implemented")
    }
}