package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedInterface

class Unhook(private val unhooker: XposedInterface.Unhooker) {
    fun unhook() = unhooker.unhook()
}
