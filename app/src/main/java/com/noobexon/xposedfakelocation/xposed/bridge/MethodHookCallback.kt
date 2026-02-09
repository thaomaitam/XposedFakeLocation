package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedInterface

abstract class MethodHookCallback(val priority: Int = XposedInterface.PRIORITY_DEFAULT) {
    open fun beforeHook(param: HookParam) {}
    open fun afterHook(param: HookParam) {}
}
