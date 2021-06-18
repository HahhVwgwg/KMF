package kz.edu.kmf.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kz.edu.kmf.R.*
import kz.edu.kmf.databinding.DialogLoadingBinding

class LoadingDialog : DialogFragment() {
    private lateinit var binding: DialogLoadingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogLoadingBinding.inflate(layoutInflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        isCancelable = false
        return builder.create()
    }
    override fun onStart() {
        super.onStart()
        this@LoadingDialog.dialog?.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}