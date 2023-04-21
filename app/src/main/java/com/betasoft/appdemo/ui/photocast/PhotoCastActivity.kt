package com.betasoft.appdemo.ui.photocast

import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.ActivityPhotoCastBinding
import com.betasoft.appdemo.ui.base.AbsBaseActivity

class PhotoCastActivity : AbsBaseActivity<ActivityPhotoCastBinding>() {

    override fun getFragmentID(): Int {
        return R.id.navContainerViewPhotoCast
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photo_cast
    }
}