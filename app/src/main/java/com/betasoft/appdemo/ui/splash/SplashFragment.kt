package com.betasoft.appdemo.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentSplashBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment

class SplashFragment : AbsBaseFragment<FragmentSplashBinding>() {
    override fun getLayout(): Int = R.layout.fragment_splash
    private val handler = Handler(Looper.myLooper()!!)
    private val runnable = Runnable {
        findNavController().navigate(SplashFragmentDirections.actionGlobalHomeFragment())
    }

    override fun initView() {

    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 1500)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}