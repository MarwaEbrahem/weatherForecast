package com.mad41.weatherForecast.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.mad41.weatherForecast.R


class SettingFragment1 : Fragment() {
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)
        var root = inflater.inflate(R.layout.fragment_setting, container, false)

        if (savedInstanceState == null) {
            val fragment: Fragment = SettingFragment2()
            val fragmentManager: FragmentManager? = fragmentManager
            fragmentManager?.beginTransaction()?.replace(R.id.settings, fragment)?.commit()
        }
        return root
    }

}