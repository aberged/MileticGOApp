package com.mileticgo.app

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mileticgo.app.view.MainMenuFragment

//added to inform what is visible fragment in main activity
val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

class MainFragmentActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}