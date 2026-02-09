package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedInterface.MethodUnhooker
import java.lang.reflect.Member

class Unhook(private val unhooker: MethodUnhooker<out Member>) {
    fun unhook() = unhooker.unhook()
}
