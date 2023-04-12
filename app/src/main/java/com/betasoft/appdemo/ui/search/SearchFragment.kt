package com.betasoft.appdemo.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.SeekBarBindingAdapter
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentSearchBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment


class SearchFragment : AbsBaseFragment<FragmentSearchBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {

    }

}