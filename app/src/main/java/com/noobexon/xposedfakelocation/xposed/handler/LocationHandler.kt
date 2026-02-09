package com.noobexon.xposedfakelocation.xposed.handler

import android.location.Location
import com.noobexon.xposedfakelocation.xposed.arch.IHook
import com.noobexon.xposedfakelocation.xposed.arch.hookAllAfter
import com.noobexon.xposedfakelocation.xposed.utils.LocationUtil
import com.noobexon.xposedfakelocation.xposed.utils.PreferencesUtil

class LocationHandler : IHook() {
    override fun onHook() {
        hookLocation()
        hookLocationManager()
        logI("LocationHandler initialized")
    }

    private fun hookLocation() {
        findClass("android.location.Location").apply {
            hookAllAfter("getLatitude") { param ->
                LocationUtil.updateLocation()
                param.result = LocationUtil.latitude
            }

            hookAllAfter("getLongitude") { param ->
                LocationUtil.updateLocation()
                param.result = LocationUtil.longitude
            }

            hookAllAfter("getAccuracy") { param ->
                LocationUtil.updateLocation()
                if (PreferencesUtil.getUseAccuracy() == true) {
                    param.result = LocationUtil.accuracy
                }
            }

            hookAllAfter("getAltitude") { param ->
                LocationUtil.updateLocation()
                if (PreferencesUtil.getUseAltitude() == true) {
                    param.result = LocationUtil.altitude
                }
            }

            hookAllAfter("getVerticalAccuracyMeters") { param ->
                LocationUtil.updateLocation()
                if (PreferencesUtil.getUseVerticalAccuracy() == true) {
                    param.result = LocationUtil.verticalAccuracy
                }
            }

            hookAllAfter("getSpeed") { param ->
                LocationUtil.updateLocation()
                if (PreferencesUtil.getUseSpeed() == true) {
                    param.result = LocationUtil.speed
                }
            }

            hookAllAfter("getSpeedAccuracyMetersPerSecond") { param ->
                LocationUtil.updateLocation()
                if (PreferencesUtil.getUseSpeedAccuracy() == true) {
                    param.result = LocationUtil.speedAccuracy
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                hookAllAfter("getMslAltitudeMeters") { param ->
                    LocationUtil.updateLocation()
                    if (PreferencesUtil.getUseMeanSeaLevel() == true) {
                        param.result = LocationUtil.meanSeaLevel
                    }
                }

                hookAllAfter("getMslAltitudeAccuracyMeters") { param ->
                    LocationUtil.updateLocation()
                    if (PreferencesUtil.getUseMeanSeaLevelAccuracy() == true) {
                        param.result = LocationUtil.meanSeaLevelAccuracy
                    }
                }
            }
        }
    }

    private fun hookLocationManager() {
        findClass("android.location.LocationManager").apply {
            hookAllAfter("getLastKnownLocation") { param ->
                val provider = param.args[0] as String
                param.result = LocationUtil.createFakeLocation(provider = provider)
            }
        }
    }
}
