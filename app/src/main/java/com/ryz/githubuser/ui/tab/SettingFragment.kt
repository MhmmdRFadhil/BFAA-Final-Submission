package com.ryz.githubuser.ui.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.ryz.githubuser.databinding.FragmentSettingBinding
import com.ryz.githubuser.settings.SettingViewModelFactory
import com.ryz.githubuser.viewmodel.SettingViewModel

class SettingFragment : Fragment() {
    private var binding: FragmentSettingBinding? = null
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel = obtainViewModel(context as AppCompatActivity)

        binding?.switchTheme?.apply {
            settingViewModel.getThemeSettings().observe(viewLifecycleOwner, {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    this.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    this.isChecked = false
                }
            })
            setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                settingViewModel.saveThemeSetting(isChecked)

            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): SettingViewModel {
        val factory = SettingViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SettingViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}