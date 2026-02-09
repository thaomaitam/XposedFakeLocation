package com.noobexon.xposedfakelocation.xposed.arch

import android.os.Build
import org.lsposed.hiddenapibypass.HiddenApiBypass

fun enableHiddenApiBypass(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        HiddenApiBypass.addHiddenApiExemptions("")
    } else true
}
