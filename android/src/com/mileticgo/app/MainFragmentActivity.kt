package com.mileticgo.app

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.ar.core.ArCoreApk
import com.mileticgo.app.view.MainMenuFragment
import com.mileticgo.app.view.twoButtonsDialog

//added to inform which fragment is visible in main activity
val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

class MainFragmentActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isSupportedDevice() && !checkARCoreAvailability()) {
            setArFlag(false)
        } else {
            setArFlag(true)
        }
        setContentView(R.layout.main_activity)

    }

    //set flag for ar support
    private fun setArFlag(supported: Boolean) {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(getString(R.string.ar_supported_flag), supported).apply()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.currentNavigationFragment
        if (currentFragment is MainMenuFragment) {
            this.twoButtonsDialog(resources.getString(R.string.exit_title), resources.getString(R.string.exit_message),
                resources.getString(R.string.yes), resources.getString(R.string.no), firstButtonCallback = {
                    finish()
                }
            )
        } else {
            super.onBackPressed()
        }
    }

    private fun isSupportedDevice(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val openGlVersionString = activityManager.deviceConfigurationInfo.glEsVersion
        if (openGlVersionString.toDouble() < 3.0) {
            Toast.makeText(this, "AR view requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            finish()
            return false
        }
        return true
    }

    private fun checkARCoreAvailability(): Boolean {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                checkARCoreAvailability()
            }, 200)
        }
        return availability.isSupported
    }
}