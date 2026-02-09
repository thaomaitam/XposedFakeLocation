package com.noobexon.xposedfakelocation.xposed.bridge

import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam

class LoadPackageParam(private val param: PackageLoadedParam) {
    val packageName: String get() = param.packageName
    val classLoader: ClassLoader get() = param.classLoader
    val isFirstPackage: Boolean get() = param.isFirstPackage
    val original: PackageLoadedParam get() = param
}
