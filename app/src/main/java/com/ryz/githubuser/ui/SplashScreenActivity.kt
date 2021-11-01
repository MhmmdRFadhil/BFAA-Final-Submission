package com.ryz.githubuser.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.ryz.githubuser.R
import com.ryz.githubuser.databinding.ActivitySplashScreenBinding
import com.ryz.githubuser.settings.SettingViewModelFactory
import com.ryz.githubuser.viewmodel.SettingViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel = obtainViewModel(this)

        settingViewModel.getThemeSettings().observe(this, {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, TIMEOUT.toLong())

        transparentStatusAndNavigation()
    }

    private fun obtainViewModel(activity: AppCompatActivity): SettingViewModel {
        val factory = SettingViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SettingViewModel::class.java]
    }

    private fun transparentStatusAndNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val wic = WindowCompat.getInsetsController(window, window.decorView)
            wic?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE

            when (this.resources?.configuration?.uiMode?.and((Configuration.UI_MODE_NIGHT_MASK))) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    window.statusBarColor = Color.TRANSPARENT
                    window.navigationBarColor = Color.TRANSPARENT
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    wic?.isAppearanceLightStatusBars = true
                    wic?.isAppearanceLightNavigationBars = true
                    window.statusBarColor = Color.TRANSPARENT
                    window.navigationBarColor = Color.TRANSPARENT
                }
            }
        }
    }

    companion object {
        private const val TIMEOUT = 5400
    }
}