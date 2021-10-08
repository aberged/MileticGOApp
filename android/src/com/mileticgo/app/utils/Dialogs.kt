package com.mileticgo.app.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mileticgo.app.R
import com.mileticgo.app.databinding.DialogOneButtonBinding
import com.mileticgo.app.databinding.DialogTwoButtonsBinding

fun Context.twoButtonsDialog(
    title: String? = null, message: String?, firstButtonText: String,
    secondButtonText: String, firstButtonCallback: (() -> Unit)? = null,
    secondButtonCallback: (() -> Unit)? = null
): AlertDialog {
    val binding = DataBindingUtil.inflate<DialogTwoButtonsBinding>(
        getLayoutInflater(),
        R.layout.dialog_two_buttons,
        null,
        false
    )
    binding.tvDialogTitle.text = title
    binding.tvDialogMessage.text = message

    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
        .setCancelable(false)

    val alert = builder.create()
    //alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    binding.btnFirst.text = firstButtonText
    binding.btnSecond.text = secondButtonText

    binding.btnFirst.setOnClickListener {
        firstButtonCallback?.invoke()
        alert.dismiss()
    }

    binding.btnSecond.setOnClickListener {
        secondButtonCallback?.invoke()
        alert.dismiss()
    }

    alert.show()

    return alert
}

fun Context.oneButtonDialog(
    title: String? = null, message: String?, buttonText: String,
    buttonCallback: (() -> Unit)? = null
): AlertDialog {
    val binding = DataBindingUtil.inflate<DialogOneButtonBinding>(getLayoutInflater(), R.layout.dialog_one_button, null, false)
    binding.tvDialogTitle.text = title
    binding.tvDialogMessage.text = message

    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
        .setCancelable(false)

    val alert = builder.create()
    binding.btnConfirm.text = buttonText

    binding.btnConfirm.setOnClickListener {
        buttonCallback?.invoke()
        alert.dismiss()
    }

    alert.show()

    return alert
}

fun Context.getLayoutInflater(): LayoutInflater {
    return (this as? AppCompatActivity)?.layoutInflater
        ?: getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}