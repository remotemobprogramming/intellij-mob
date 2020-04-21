package com.nowsprinting.intellij_mob.testdouble

import com.intellij.openapi.diagnostic.Logger
import org.apache.log4j.Level

internal open class FakeLogger : Logger() {
    override fun warn(message: String?, t: Throwable?) {
        println(message)
        println(t?.stackTrace)
    }

    override fun setLevel(level: Level?) {
        throw Exception("Not yet implemented")
    }

    override fun info(message: String?) {
        println(message)
    }

    override fun info(message: String?, t: Throwable?) {
        println(message)
        println(t?.stackTrace)
    }

    override fun error(message: String?, t: Throwable?, vararg details: String?) {
        println(message)
        println(t?.stackTrace)
    }

    override fun isDebugEnabled(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun debug(message: String?) {
        println(message)
    }

    override fun debug(t: Throwable?) {
        println(t?.stackTrace)
    }

    override fun debug(message: String?, t: Throwable?) {
        println(message)
        println(t?.stackTrace)
    }
}