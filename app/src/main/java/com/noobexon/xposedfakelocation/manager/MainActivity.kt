package com.noobexon.xposedfakelocation.manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.noobexon.xposedfakelocation.manager.ui.components.ErrorScreen
import com.noobexon.xposedfakelocation.manager.ui.navigation.AppNavGraph
import com.noobexon.xposedfakelocation.manager.ui.theme.XposedFakeLocationTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isXposedModuleEnabled = true
        // If the module is not enabled then the app won't have permission to use MODE_WORLD_READABLE.
        try {
            getPreferences(MODE_WORLD_READABLE)
        } catch (e: SecurityException) {
            isXposedModuleEnabled = false
            Log.e(TAG, "SecurityException: ${e.message}", e)
        } catch (e: Exception) {
            isXposedModuleEnabled = false
            Log.e(TAG, "Exception: ${e.message}", e)
        }

        enableEdgeToEdge()
        
        setContent {
            XposedFakeLocationTheme {
                if (isXposedModuleEnabled) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                } else {
                    ErrorScreen(
                        onDismiss = { finish() },
                        onConfirm = { finish() }
                    )
                }
            }
        }
    }
}