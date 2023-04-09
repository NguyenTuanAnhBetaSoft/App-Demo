package com.betasoft.appdemo.ui.home

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentHomeBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.myfile.MyFileFragment
import com.betasoft.appdemo.ui.openart.OpenArtFragment

class HomeFragment : AbsBaseFragment<FragmentHomeBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        initViewPager()
        onBackPressed()

    }

    private fun initViewPager() {
        // Define fragments to display in viewPager2
        val listOfFragments = arrayListOf<Fragment>(
            OpenArtFragment(),
            MyFileFragment(),
            MyFileFragment(),
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
                    2 -> binding.bottomNavigation.menu.findItem(R.id.myFileFragment2).isChecked =
                        true
                    3 -> binding.bottomNavigation.menu.findItem(R.id.myFileFragment3).isChecked =
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
                R.id.myFileFragment2 -> {
                    binding.viewPager.setCurrentItem(2, true)
                    return@setOnItemSelectedListener true
                }
                R.id.myFileFragment3 -> {
                    binding.viewPager.setCurrentItem(3, true)
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


}