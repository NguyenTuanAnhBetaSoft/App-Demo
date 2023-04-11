package com.betasoft.appdemo.ui.myfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentMyFileBinding
import com.betasoft.appdemo.ui.adpter.MyFileAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFileFragment : AbsBaseFragment<FragmentMyFileBinding>() {
    private val mViewModel: MyFileViewModel by viewModels()

    private lateinit var mLayoutManager: LinearLayoutManager

    private val myFileAdapter by lazy {
        MyFileAdapter().apply {
            onClickItemListeners = object : MyFileAdapter.OnClickItemListeners {
                override fun onClickedItem(param: ImageLocal) {

                }

            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_my_file
    }

    override fun initView() {
        initRecycleView()
        observer()
        mViewModel.getAllImageLocal()
    }

    private fun observer() {
        mViewModel.allImageLocalLiveData.observe(this) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    val body = (it as DataResponse.DataSuccess).body
                    Log.d("gsdf", "body = ${body.toString()}")
                    myFileAdapter.update(body)
                }
            }
        }
    }

    private fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = myFileAdapter
            layoutManager = mLayoutManager

        }

    }

    private var isViewShown = false
    @Suppress("DEPRECATION")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (view != null && isVisibleToUser) {
            isViewShown =
                true
            mViewModel.getAllImageLocal()
        } else {
            isViewShown = false
        }
    }

}