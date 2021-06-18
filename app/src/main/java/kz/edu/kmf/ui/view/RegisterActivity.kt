package kz.edu.kmf.ui.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import kz.edu.kmf.R
import kz.edu.kmf.data.model.User
import kz.edu.kmf.databinding.ActivityRegisterBinding
import kz.edu.kmf.ui.viewmodel.MainViewModel
import kz.edu.kmf.utils.LoadingDialog
import kz.edu.kmf.utils.Status
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var loadingDialog: LoadingDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.register_bk_color)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog()
        binding.cirRegisterButton.setOnClickListener {
            if (!validate(binding.editTextEmail.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Недопустимый адрес электронной почты",
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding.editTextMobile.text.length < 9) {
                Toast.makeText(
                    applicationContext,
                    "Недопустимый формат номера",
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding.editTextLogin.text.isEmpty() || binding.editTextName.text.isEmpty() || binding.editTextPassword.text.isEmpty() || binding.editTextStatus.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Заполните все поля",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                loadingDialog.show(supportFragmentManager, null)
                registerUser(
                    User(
                        0,
                        binding.editTextLogin.text.toString(),
                        binding.editTextName.text.toString().split(" ")[0],
                        binding.editTextName.text.toString().split(" ")[1],
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString(),
                        binding.editTextMobile.text.toString(),
                        binding.editTextStatus.text.toString().toInt()
                    )
                )
            }
        }
    }

    private val regexEmail: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private fun validate(emailStr: String?): Boolean {
        val matcher: Matcher = regexEmail.matcher(emailStr)
        return matcher.find()
    }

    private fun registerUser(user: User) {
        viewModel.registerUser(user).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Пользователь успешно создан",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.editTextLogin.text.clear()
                        binding.editTextName.text.clear()
                        binding.editTextName.text.clear()
                        binding.editTextEmail.text.clear()
                        binding.editTextPassword.text.clear()
                        binding.editTextMobile.text.clear()
                        binding.editTextStatus.text.clear()
                        val view: View? = this.currentFocus
                        if (view != null) {
                            val imm: InputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(applicationContext, "Ошибка", Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}