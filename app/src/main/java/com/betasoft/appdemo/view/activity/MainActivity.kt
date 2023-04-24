package com.betasoft.appdemo.view.activity

import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.ActivityMainBinding
import com.betasoft.appdemo.view.base.AbsBaseActivity
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