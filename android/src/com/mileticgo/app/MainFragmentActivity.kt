package com.mileticgo.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.ar.core.ArCoreApk
import com.mileticgo.app.databinding.MainActivityBinding
import com.mileticgo.app.utils.SharedPrefs
import com.mileticgo.app.view.MainMenuFragment
import com.mileticgo.app.utils.twoButtonsDialog

//added to inform which fragment is visible in main activity
val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

class MainFragmentActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkARCoreAvailability()) {  //!isSupportedDevice() &&
            setArFlag(false)
        } else {
            setArFlag(true)
        }
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    //set flag for ar support
    private fun setArFlag(supported: Boolean) {
        SharedPrefs.save(this, getString(R.string.ar_supported_flag), supported)
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

    /*private fun isSupportedDevice(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val openGlVersionString = activityManager.deviceConfigurationInfo.glEsVersion
        if (openGlVersionString.toDouble() < 3.0) {
            Toast.makeText(this, "AR view requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            //finish()
            return false
        }
        return true
    }*/

    private fun checkARCoreAvailability(): Boolean {
        return when (ArCoreApk.getInstance().checkAvailability(this)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
                true
            } else -> {
                false
            }
        }
    }
}