package kz.edu.kmf.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import kz.edu.kmf.R
import kz.edu.kmf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cirLoginButton.setOnClickListener {
            ShowInfoDialog().show(supportFragmentManager, null)
        }
        binding.cirRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}