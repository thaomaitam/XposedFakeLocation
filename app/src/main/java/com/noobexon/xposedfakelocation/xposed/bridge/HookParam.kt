package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import java.lang.reflect.Member

class HookParam private constructor(
    private val before: BeforeHookCallback? = null,
    private val after: AfterHookCallback? = null
) {
    companion object {
        fun fromBefore(callback: BeforeHookCallback) = HookParam(before = callback)
        fun fromAfter(callback: AfterHookCallback) = HookParam(after = callback)
    }

    val method: Member get() = before?.member ?: after!!.member
    val thisObject: Any? get() = before?.thisObject ?: after!!.thisObject
    val args: Array<Any?> get() = before?.args ?: after!!.args

    var result: Any?
        get() = after?.result
        set(value) {
            before?.returnAndSkip(value) ?: after?.setResult(value)
        }

    var throwable: Throwable?
        get() = after?.throwable
        set(value) {
            if (value != null) {
                before?.throwAndSkip(value) ?: after?.setThrowable(value)
            }
        }

    val isSkipped: Boolean get() = after?.isSkipped ?: false
}
