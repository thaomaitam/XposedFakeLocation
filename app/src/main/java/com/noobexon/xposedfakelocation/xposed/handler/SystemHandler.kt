package com.noobexon.xposedfakelocation.xposed.handler

import android.location.Location
import android.os.Build
import com.noobexon.xposedfakelocation.xposed.arch.IHook
import com.noobexon.xposedfakelocation.xposed.arch.hookAllBefore
import com.noobexon.xposedfakelocation.xposed.arch.hookAllNop
import com.noobexon.xposedfakelocation.xposed.utils.LocationUtil

class SystemHandler : IHook() {
    override fun onHook() {
        hookSystemServices()
        logI("SystemHandler initialized")
    }

    private fun hookSystemServices() {
        try {
            val locationManagerServiceClass = findClass("com.android.server.LocationManagerService")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                locationManagerServiceClass.hookAllBefore("getLastLocation") { param ->
                    param.result = LocationUtil.createFakeLocation()
                }
            }

            locationManagerServiceClass.apply {
                hookAllNop("addGnssBatchingCallback")
                hookAllNop("addGnssMeasurementsListener")
                hookAllNop("addGnssNavigationMessageListener")
            }

            findClass("com.android.server.LocationManagerService\$Receiver").apply {
                hookAllBefore("callLocationChangedLocked") { param ->
                    val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                    param.args[0] = fakeLocation
                }
            }
        } catch (e: Exception) {
            logE("Error hooking system services", e)
        }
    }
}
