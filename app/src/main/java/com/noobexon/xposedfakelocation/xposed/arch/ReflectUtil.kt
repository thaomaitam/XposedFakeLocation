package com.noobexon.xposedfakelocation.xposed.arch

import com.noobexon.xposedfakelocation.xposed.bridge.Xposed
import java.lang.reflect.Member
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
fun <T> Any?.getObjAs(name: String): T {
    val field = this!!::class.java.getDeclaredField(name).apply { isAccessible = true }
    return field.get(this) as T
}

fun Any?.setObj(name: String, value: Any?) {
    val field = this!!::class.java.getDeclaredField(name).apply { isAccessible = true }
    field.set(this, value)
}

fun Any?.call(name: String, vararg args: Any?): Any? {
    val types = args.map { it?.let { it::class.javaPrimitiveType ?: it::class.java } ?: Any::class.java }.toTypedArray()
    val method = this!!::class.java.getDeclaredMethods().find { it.name == name && it.parameterCount == args.size }
    method?.isAccessible = true
    return method?.invoke(this, *args)
}

fun Member.callOrig(receiver: Any?, vararg args: Any?): Any? {
    return if (this is Method) {
        Xposed.invokeOriginal(this, receiver, *args)
    } else null
}
