/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.util

import com.intellij.openapi.diagnostic.Logger
import javazoom.jl.player.Player
import java.io.BufferedInputStream

private val log = Logger.getInstance(Sound::class.java)

class Sound(private val name: String) {

    var player: Player? = null

    fun play() {
        val filename = "/$name.mp3"
        try {
            val buffer = BufferedInputStream(javaClass.getResourceAsStream(filename))
            player = Player(buffer)
            object : Thread() {
                override fun run() {
                    try {
                        player?.play()
                    } catch (e: Exception) {
                        log.error("Problem playing file $filename", e)
                    }
                }
            }.start()
        } catch (e: Exception) {
            log.error("Problem playing file $filename", e)
        }
    }

    fun stop() {
        player?.close()
    }

}