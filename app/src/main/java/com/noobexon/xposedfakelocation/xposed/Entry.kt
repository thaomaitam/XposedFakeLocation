package com.noobexon.xposedfakelocation.xposed

import com.noobexon.xposedfakelocation.data.MANAGER_APP_PACKAGE_NAME
import com.noobexon.xposedfakelocation.xposed.arch.IHook
import com.noobexon.xposedfakelocation.xposed.bridge.Xposed
import com.noobexon.xposedfakelocation.xposed.handler.LocationHandler
import com.noobexon.xposedfakelocation.xposed.handler.SystemHandler
import com.noobexon.xposedfakelocation.xposed.utils.PreferencesUtil
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam

class Entry(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param) {
    init {
        Xposed.init(base, this)
    }

    override fun onPackageLoaded(param: PackageLoadedParam) {
        if (param.packageName == MANAGER_APP_PACKAGE_NAME) return

        // If not playing, do not proceed with hooking
        if (PreferencesUtil.getIsPlaying() != true) return

        IHook.currentClassLoader = param.classLoader

        if (param.packageName == "android") {
            SystemHandler().onHook()
        }

        LocationHandler().onHook()
    }
}
