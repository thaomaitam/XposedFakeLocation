package com.noobexon.xposedfakelocation.xposed.arch

import com.noobexon.xposedfakelocation.xposed.bridge.HookParam
import com.noobexon.xposedfakelocation.xposed.bridge.MethodHookCallback
import com.noobexon.xposedfakelocation.xposed.bridge.Xposed
import java.lang.reflect.Method

typealias HookCallback = (HookParam) -> Unit

fun Class<*>.hookAllAfter(methodName: String, callback: HookCallback) {
    this.declaredMethods.filter { it.name == methodName }.forEach { method ->
        Xposed.hookMethod(method, object : MethodHookCallback() {
            override fun afterHook(param: HookParam) = callback(param)
        })
    }
}

fun Class<*>.hookAllBefore(methodName: String, callback: HookCallback) {
    this.declaredMethods.filter { it.name == methodName }.forEach { method ->
        Xposed.hookMethod(method, object : MethodHookCallback() {
            override fun beforeHook(param: HookParam) = callback(param)
        })
    }
}

fun Class<*>.hookAllReplace(methodName: String, callback: (HookParam) -> Any?) {
    this.declaredMethods.filter { it.name == methodName }.forEach { method ->
        Xposed.hookMethod(method, object : MethodHookCallback() {
            override fun beforeHook(param: HookParam) {
                param.result = callback(param)
            }
        })
    }
}

fun Class<*>.hookAllNop(methodName: String) {
    hookAllReplace(methodName) { null }
}

fun Class<*>.hookAfter(methodName: String, vararg parameterTypes: Class<*>, callback: HookCallback) {
    val method = this.getDeclaredMethod(methodName, *parameterTypes)
    Xposed.hookMethod(method, object : MethodHookCallback() {
        override fun afterHook(param: HookParam) = callback(param)
    })
}
