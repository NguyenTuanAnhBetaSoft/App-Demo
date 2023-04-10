package com.betasoft.appdemo.ui.openart

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentOpenArtBinding
import com.betasoft.appdemo.ui.adpter.ImageAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenArtFragment : AbsBaseFragment<FragmentOpenArtBinding>() {
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var cursor = ""

    private val mViewModel: HomeViewModel by viewModels()

    private lateinit var mLayoutManager: GridLayoutManager

    private val imageAdapter by lazy {
        ImageAdapter().apply {
            onClickItemListeners = object : ImageAdapter.OnClickItemListeners {
                override fun onClickedItem(param: ItemsItem) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionGlobalDetailImageFragment(
                            param.image_url.toString()
                        )
                    )
                }

            }
        }

    }

    override fun getLayout(): Int {
        return R.layout.fragment_open_art
    }

    override fun initView() {
        binding.viewModel = mViewModel
        mViewModel.fetchImageList(false, cursor)
        observer()
        initRecycleView()

        binding.apply {
            rV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = mLayoutManager.itemCount
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
                    if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                        mViewModel.fetchImageList(true, cursor)
                    }
                }
            })
        }
    }

    private fun initRecycleView() {
        mLayoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = imageAdapter
            layoutManager = mLayoutManager

        }

    }

    private fun observer() {
        mViewModel.dataAppLiveData.observe(this) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    val body = (it as DataResponse.DataSuccess).body
                    cursor = body.cursor.toString()
                    imageAdapter.update(body.curPage > 1, body.items)
                }
            }
        }

    }

}