package com.betasoft.appdemo.ui.home

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentHomeBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.myfile.MyFileFragment
import com.betasoft.appdemo.ui.openart.OpenArtFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@AndroidEntryPoint
class HomeFragment : AbsBaseFragment<FragmentHomeBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

        observer()
        initViewPager()
        onBackPressed()

    }

    private fun initViewPager() {
        // Define fragments to display in viewPager2
        val listOfFragments = arrayListOf<Fragment>(
            OpenArtFragment(),
            MyFileFragment()
        )
        binding.viewPager.adapter =
            ViewPagerHomeAdapter(listOfFragments, childFragmentManager, lifecycle)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavigation.menu.findItem(R.id.openArtFragment).isChecked =
                        true
                    1 -> binding.bottomNavigation.menu.findItem(R.id.myFileFragment).isChecked =
                        true
                }
            }
        })

        // Listen bottom navigation tabs change
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.openArtFragment -> {
                    binding.viewPager.setCurrentItem(0, true)
                    return@setOnItemSelectedListener true

                }
                R.id.myFileFragment -> {
                    binding.viewPager.setCurrentItem(1, true)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun observer() {
        activity?.let {
            KeyboardVisibilityEvent.setEventListener(it, object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (isOpen) binding.bottomNavigation.visibility = View.GONE
                    else {
                        lifecycleScope.launch {
                            delay(200)
                            binding.bottomNavigation.visibility = View.VISIBLE
                        }
                    }

                }
            })
        }
    }


}