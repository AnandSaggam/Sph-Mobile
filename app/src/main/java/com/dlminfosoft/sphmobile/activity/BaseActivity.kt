package com.dlminfosoft.sphmobile.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dlminfosoft.sphmobile.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    /*
    * Initializer abstract method
    */
    abstract fun setup()

    /*
    * Method used for show snackBar message
    */
    protected fun showSnackBar(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    /*
    * Method used for display alert dialog
    */
    protected fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, _ -> dialog.dismiss() }.show()
    }

}
