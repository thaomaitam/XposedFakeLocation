package com.noobexon.xposedfakelocation.xposed.arch

import com.noobexon.xposedfakelocation.xposed.bridge.Xposed

abstract class IHook {
    protected val classLoader: ClassLoader get() = currentClassLoader!!
    
    companion object {
        var currentClassLoader: ClassLoader? = null
    }

    abstract fun onHook()

    protected fun logI(msg: String) = Xposed.log("[INFO] $msg")
    protected fun logE(msg: String, t: Throwable? = null) = Xposed.log("[ERROR] $msg").also { t?.let { Xposed.log(it.stackTraceToString()) } }

    protected fun findClass(name: String): Class<*> = classLoader.loadClass(name)

    protected fun subHook(hook: IHook) {
        hook.onHook()
    }
}
