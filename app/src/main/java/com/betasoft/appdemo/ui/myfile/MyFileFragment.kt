package com.betasoft.appdemo.ui.myfile

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.databinding.FragmentMyFileBinding
import com.betasoft.appdemo.ui.adpter.MyFileAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.home.HomeFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MyFileFragment : AbsBaseFragment<FragmentMyFileBinding>() {
    @Inject
    lateinit var myFileAdapter: MyFileAdapter

    private val mViewModel: MyFileViewModel by viewModels()

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun getLayout(): Int {
        return R.layout.fragment_my_file
    }

    override fun initView() {
        initRecycleView()
        configureStateListener()
        observer()
        refreshData()
        binding.mySwipeRefreshLayout.setOnRefreshListener {
            binding.mySwipeRefreshLayout.isEnabled = true
            binding.mySwipeRefreshLayout.isRefreshing = true
            refreshData()
        }



        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    searchImageLocal(newText.trim())
                } else {
                    refreshData()
                }
                return false
            }

        })

        myFileAdapter.onClickItem = {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalDetailImageLocalFragment(
                    it
                )
            )
        }

        myFileAdapter.onClickMore = { view, image ->
            showPopupMenu(view, R.menu.popup_menu, image)
        }

    }


    private fun observer() {

    }

    private fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = myFileAdapter
            layoutManager = mLayoutManager
        }

    }


    private fun configureStateListener() {
        myFileAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                // Show ProgressBar
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.mySwipeRefreshLayout.isEnabled = false
                binding.mySwipeRefreshLayout.isRefreshing = false
            } else {
                // Hide ProgressBar
                binding.shimmerLayout.visibility = View.GONE
                binding.mySwipeRefreshLayout.isEnabled = true
                binding.mySwipeRefreshLayout.isRefreshing = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.shimmerLayout.visibility = View.GONE

                }

            }
        }
    }
    ////

    private fun refreshData() {
        lifecycleScope.launch {
            mViewModel.getAllImageLocal().collectLatest { response ->
                Timber.d("onCreate: $response")
                myFileAdapter.submitData(response)
            }
        }
    }

    private fun searchImageLocal(searchQuery: String) {
        lifecycleScope.launch {
            mViewModel.searchImageLocal(searchQuery).collectLatest { response ->
                myFileAdapter.submitData(response)
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(v: View, @MenuRes menuRes: Int, imageLocal: ImageLocal) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, (6).toFloat(), resources.displayMetrics
                    )
                        .toInt()
                if (item.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                    } else {
                        item.icon =
                            object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
        }
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when (menuItem.itemId) {
                R.id.share -> {
                    /*val bitmap = binding.item.getDrawable().bitmap

                    ShareFile.shareImageandText(bitmap)*/
                    true
                }
                R.id.delete -> {
                    showDialogDelete(imageLocal)
                    true
                }
                else -> false
            }
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }

        popup.show()
    }

    private fun showDialogDelete(imageLocal: ImageLocal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.message_dialog_delete))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                // Respond to positive button press
                mViewModel.deleteImage(imageLocal)
                Snackbar.make(
                    binding.rootLayout,
                    R.string.successfully_delete_image,
                    Snackbar.LENGTH_SHORT
                )
                    .show()

            }
            .show()
    }

}