package com.betasoft.appdemo.view.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentHomeBinding
import com.betasoft.appdemo.view.adpter.ViewPagerHomeAdapter
import com.betasoft.appdemo.view.base.AbsBaseFragment
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

        binding.toolbarHome.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting -> {
                    true
                }
                else -> false
            }
        }

    }

    private fun initViewPager() {
        // Define fragments to display in viewPager2
        val listOfFragments = arrayListOf<Fragment>(
            OpenArtFragment(),
            MyFileFragment(),
            Tast2Fragment()
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
                    2 -> binding.bottomNavigation.menu.findItem(R.id.tast2Fragment).isChecked =
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

                R.id.tast2Fragment -> {
                    binding.viewPager.setCurrentItem(2, true)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
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