package com.betasoft.appdemo.ui.openart

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.betasoft.appdemo.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenArtFragment : AbsBaseFragment<FragmentOpenArtBinding>() {
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var cursor = ""
    private var listItemDownLoad = mutableListOf<ItemsItem>()
    private var listItemDownLoad1 = mutableListOf<ItemsItem>()

    private val mViewModel: HomeViewModel by viewModels()

    private lateinit var mLayoutManager: GridLayoutManager

    private val imageAdapter by lazy {
        ImageAdapter().apply {
            onClickItem = {
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalDetailImageFragment(
                        it
                    )
                )
            }
            onClickDownLoad = {
                downLoadImageUrl(
                    url = it.image_url.toString(),
                    name = it.id.toString(),
                    haveSave = true,
                    context = requireContext(),
                    nameAuthor = it.userProfile!!.name.toString(),
                    prompt = it.prompt.toString()
                )
            }
        }

    }

    override fun getLayout(): Int {
        return R.layout.fragment_open_art
    }

    override fun initView() {
        onBackPressed()
        binding.viewModel = mViewModel
        observer()
        fetchImageList(false, cursor)
        initRecycleView()

        binding.mySwipeRefreshLayout.setOnRefreshListener {
            fetchImageList(false, cursor)
        }

        binding.btnReTry.setOnClickListener {
            fetchImageList(false, cursor)
        }

        binding.apply {
            rV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = mLayoutManager.itemCount
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
                    if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                        fetchImageList(true, cursor)
                    }
                }
            })
        }

        imageAdapter.listSelect = {
            if (imageAdapter.isSelect()) {

            } else {

            }
            mViewModel.isSelect(true)
            mViewModel.updateListItemSelect(it)

        }

        binding.btnClose.setOnClickListener {
            mViewModel.isSelect(false)
            imageAdapter.setSelect(false)
            imageAdapter.notifyItemRangeChanged(0, listItemDownLoad.size)
        }

        imageAdapter.listSelected = {
            listItemDownLoad1.clear()
            listItemDownLoad1.addAll(it)
            Log.d("9999999", "listselected = ${it.toString()}")

        }

        binding.btnMutableDownLoad.setOnClickListener {

            Log.d("5656565656", "listDownload = $listItemDownLoad1")
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

    @SuppressLint("SetTextI18n")
    private fun observer() {
        mViewModel.dataAppLiveData.observe(this) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val body = (it as DataResponse.DataSuccess).body
                        cursor = body.cursor.toString()
                        imageAdapter.update(body.curPage > 1, body.items)
                        binding.mySwipeRefreshLayout.isEnabled = true
                        binding.mySwipeRefreshLayout.isRefreshing = false
                    }
                    LoadingStatus.Refresh -> {
                        binding.mySwipeRefreshLayout.isEnabled = true
                        binding.mySwipeRefreshLayout.isRefreshing = true
                    }
                    else -> {
                        binding.mySwipeRefreshLayout.isEnabled = false
                        binding.mySwipeRefreshLayout.isRefreshing = false
                    }
                }

            }

        }

        mViewModel.listItemSelectLiveData.observe(this) {
            val total = it?.size
            binding.tvNumberDownload.text = "Do you want to download $total images?"
        }

        mViewModel.downloadImageLiveData.observe(this) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        ToastUtils.getInstance(requireContext()).showToast("DownloadSuccess")
                        val body = (it as DataResponse.DataSuccess).body
                        mViewModel.insertImageLocal(body)
                    }
                    LoadingStatus.Error -> {
                        ToastUtils.getInstance(requireContext()).showToast("image already exists")
                    }
                    else -> {

                    }
                }
            }
        }

        mViewModel.isSelectLiveData.observe(this) {
            Log.d("55555", "select = $it")
            if (it) {
                binding.cardMutableDownload.visibility = View.VISIBLE
            } else {
                binding.cardMutableDownload.visibility = View.GONE
            }
        }

        mViewModel.listItemSelectLiveData.observe(this) {
            it?.let { list ->
                listItemDownLoad.clear()
                listItemDownLoad.addAll(list)
            }
        }

    }

    private fun downLoadImageUrl(
        url: String,
        name: String,
        haveSave: Boolean,
        context: Context,
        nameAuthor: String,
        prompt: String
    ) {
        mViewModel.downloadImageUrl(
            url, name, haveSave, context, nameAuthor, prompt
        )
    }

    private fun fetchImageList(isLoadMore: Boolean, cursor: String) {
        mViewModel.fetchImageList(isLoadMore, cursor)
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (imageAdapter.isSelect()) {
                    mViewModel.isSelect(false)
                    imageAdapter.setSelect(false)
                    imageAdapter.notifyItemRangeChanged(0, listItemDownLoad.size)
                } else {
                    if (isAdded) {
                        requireActivity().finish()
                    }
                }
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


}