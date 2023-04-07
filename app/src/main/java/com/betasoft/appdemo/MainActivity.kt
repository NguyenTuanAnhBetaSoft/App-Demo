package com.betasoft.appdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.betasoft.appdemo.databinding.ActivityMainBinding
import com.betasoft.appdemo.ui.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbsBaseActivity<ActivityMainBinding>() {

    override fun getFragmentID(): Int {
        return R.id.navContainerViewMain
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}