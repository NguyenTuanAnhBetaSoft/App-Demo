package com.betasoft.appdemo.ui.openart

import android.annotation.SuppressLint
import android.content.Context
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
    private var listItemSelect = mutableListOf<ItemsItem>()
    private var listItemSelected = mutableListOf<ItemsItem>()

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

            listSelect = {
                listItemSelect.clear()
                listItemSelect.addAll(it)
            }

            listSelected = {
                mViewModel.updateListItemSelected(it)
                listItemSelected.clear()
                listItemSelected.addAll(it)
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

        binding.btnClose.setOnClickListener {
            updateUnSelect()
        }


        binding.btnMutableDownLoad.setOnClickListener {
            if (listItemSelect.size > 0) {
                mViewModel.downloadImagesUrl(listItemSelected, true, requireContext())
            }
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

        mViewModel.downloadImagesLiveData.observe(this) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        ToastUtils.getInstance(requireContext()).showToast("DownloadSuccess")
                        val body = (it as DataResponse.DataSuccess).body
                        if (body != null && body.isNotEmpty()) {
                            mViewModel.insertImagesLocal(body)
                        }
                        updateUnSelect()
                    }
                    LoadingStatus.Error -> {
                        ToastUtils.getInstance(requireContext()).showToast("image already exists")
                    }
                    else -> {

                    }
                }
            }
        }


        mViewModel.listItemSelectedLiveData.observe(this) {
            it?.let {
                if (imageAdapter.isSelect()) {
                    mViewModel.isSelect(true)
                    val total = it.size
                    binding.tvNumberDownload.text = "Do you want to download $total images?"
                }
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
                    updateUnSelect()
                } else {
                    if (isAdded) {
                        requireActivity().finish()
                    }
                }
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun updateUnSelect() {
        mViewModel.isSelect(false)
        imageAdapter.setSelect(false)
        imageAdapter.notifyItemRangeChanged(0, listItemSelect.size)
        imageAdapter.cleanListItemChecked()
    }


}