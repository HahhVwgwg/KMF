package kz.edu.kmf.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kz.edu.kmf.R.*
import kz.edu.kmf.data.model.User
import kz.edu.kmf.databinding.DialogShowInfoBinding
import kz.edu.kmf.ui.viewmodel.MainViewModel
import kz.edu.kmf.utils.LoadingDialog
import kz.edu.kmf.utils.Status

class ShowInfoDialog : DialogFragment() {
    private lateinit var loadingDialog: LoadingDialog
    private var counter = 0
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: DialogShowInfoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogShowInfoBinding.inflate(layoutInflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        loadingDialog = LoadingDialog()
        binding.cirRegisterButton.setOnClickListener {
            if (binding.container.visibility == View.VISIBLE){
                dismiss()
            } else {
                loadingDialog.show(childFragmentManager, null)
                hideSoftKeyboard()
                searchUser(binding.editTextLogin.text.toString())
            }
        }

        return builder.create()
    }

    private fun searchUser(userName: String) {
        viewModel.getUsers(userName).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        val userResponse = resource.data
                        if (userResponse != null) {
                            viewModel.addUser(
                                User(0, userResponse.username.toString(),
                                userResponse.firstName.toString(),
                                userResponse.lastName.toString(), userResponse.email.toString(),
                                userResponse.password.toString(), userResponse.phone.toString(),
                                userResponse.userStatus.toString().toInt())
                            )
                        }
                        binding.container.visibility = View.VISIBLE
                        binding.cirRegisterButton.text = "Закрыть"
                        binding.textInputLogin.visibility = View.GONE
                        showFromLocalDatabase()
                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        dismiss()
                        Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showFromLocalDatabase() {
        viewModel.readAllData.observe(this, {
            if (it.isNotEmpty() && counter == 0) {
                val user = it[it.size - 1]
                binding.email.text = "${binding.email.text} ${user.email}"
                binding.password.text = "${binding.password.text} ${user.password}"
                binding.number.text = "${binding.number.text} ${user.phone}"
                binding.status.text = "${binding.status.text} ${user.userStatus}"
                binding.login.text = "${binding.login.text} ${user.username}"
                binding.firstNameSurname.text =
                    "${binding.firstNameSurname.text} ${user.firstName} ${user.lastName}"
                counter++
            }
        })
    }

    private fun hideSoftKeyboard() {
        try {
            val windowToken = dialog!!.window!!.decorView.rootView
            val imm =
                dialog!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (ex: Exception) {
            println(ex)
        }
    }

    override fun onStart() {
        super.onStart()
        this@ShowInfoDialog.dialog?.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}