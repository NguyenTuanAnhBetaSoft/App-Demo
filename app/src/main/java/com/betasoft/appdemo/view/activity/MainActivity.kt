package com.betasoft.appdemo.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.ActivityMainBinding
import com.betasoft.appdemo.utils.Utils.isNetworkAvailable
import com.betasoft.appdemo.view.base.AbsBaseActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbsBaseActivity<ActivityMainBinding>() {
    //private lateinit var internetBroadcastReceiver: BroadcastReceiver

    override fun getFragmentID(): Int {
        return R.id.navContainerViewMain
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        internetBroadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(contxt: Context, intent: Intent?) {
//
//                if (isNetworkAvailable(contxt)) {
//                    //
//
//                } else {
//                    Snackbar.make(
//                        binding.navContainerViewMain,
//                        R.string.connect_to_the_internet,
//                        Snackbar.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }
//        }
//        this.registerReceiver(
//            internetBroadcastReceiver, IntentFilter(
//                ConnectivityManager
//                    .CONNECTIVITY_ACTION
//            )
//        )
    }

    override fun onDestroy() {
        super.onDestroy()
        //this.unregisterReceiver(internetBroadcastReceiver)
    }

}