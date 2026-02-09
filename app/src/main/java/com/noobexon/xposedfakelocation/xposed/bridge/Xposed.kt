package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedInterface.Hooker
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import java.lang.reflect.Constructor
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

object Xposed {
    private lateinit var base: XposedInterface
    private lateinit var module: XposedModule

    fun init(base: XposedInterface, module: XposedModule) {
        this.base = base
        this.module = module
    }

    fun log(msg: String) = base.log(msg)
    fun log(msg: String, t: Throwable) = base.log(msg, t)

    fun hookMethod(method: Method, callback: MethodHookCallback): Unhook {
        val priority = callback.priority
        val unhook = base.hook(method, priority, DynamicHooker::class.java)
        CallbackRegistry.register(method, callback)
        return Unhook(unhook)
    }

    fun hookConstructor(constructor: Constructor<*>, callback: MethodHookCallback): Unhook {
        val priority = callback.priority
        val unhook = base.hook(constructor, priority, DynamicHooker::class.java)
        CallbackRegistry.register(constructor, callback)
        return Unhook(unhook)
    }

    fun invokeOriginal(method: Method, receiver: Any?, vararg args: Any?): Any? {
        return base.invokeOrigin(method, receiver, *args)
    }

    fun remotePrefs(name: String) = base.getRemotePreferences(name)
}

object CallbackRegistry {
    private val callbacks = ConcurrentHashMap<Member, MethodHookCallback>()

    fun register(member: Member, callback: MethodHookCallback) {
        callbacks[member] = callback
    }

    fun get(member: Member): MethodHookCallback? = callbacks[member]
}

@XposedHooker
class DynamicHooker : Hooker {
    companion object {
        @BeforeInvocation
        @JvmStatic
        fun before(callback: BeforeHookCallback) {
            CallbackRegistry.get(callback.member)?.let { methodCallback ->
                try {
                    methodCallback.beforeHook(HookParam.fromBefore(callback))
                } catch (t: Throwable) {
                    Xposed.log("Error in beforeHook: ${callback.member}", t)
                }
            }
        }

        @AfterInvocation
        @JvmStatic
        fun after(callback: AfterHookCallback) {
            CallbackRegistry.get(callback.member)?.let { methodCallback ->
                try {
                    methodCallback.afterHook(HookParam.fromAfter(callback))
                } catch (t: Throwable) {
                    Xposed.log("Error in afterHook: ${callback.member}", t)
                }
            }
        }
    }
}
