package com.mileticgo.app

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.ar.core.ArCoreApk
import com.mileticgo.app.view.MainMenuFragment

//added to inform which fragment is visible in main activity
val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

class MainFragmentActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isSupportedDevice() && !checkARCoreAvailability()) {
            return
        }
        setContentView(R.layout.main_activity)

    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.currentNavigationFragment
        if (currentFragment is MainMenuFragment) {
            createExitDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun createExitDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.exit_title)
        alertDialogBuilder.setMessage(R.string.exit_message)

        alertDialogBuilder.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            finish()
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { dialogInterface: DialogInterface, i: Int ->

        }

        //create alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
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
        return availability.isSupported
    }
}