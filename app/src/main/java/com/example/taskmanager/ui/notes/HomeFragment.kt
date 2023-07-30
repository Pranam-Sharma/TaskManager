package com.example.taskmanager.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.taskmanager.R
import com.example.taskmanager.adapters.MyPagerAdapter
import com.example.taskmanager.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = MyPagerAdapter(requireParentFragment().childFragmentManager,binding.tabLayout.tabCount,requireContext())
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.apply {
            val tabTitles = listOf("Sports","Entertainment","Business","Politics")
            val tabIcons = listOf(
                R.drawable.baseline_sports_soccer_24,
                R.drawable.baseline_music_video_24,
                R.drawable.baseline_business_center_24,
                R.drawable.baseline_people_24
            )

            for (i in tabTitles.indices) {
                val tab = this.newTab().setIcon(tabIcons[i]).setText(tabTitles[i])
                addTab(tab)
            }

             tabGravity = TabLayout.GRAVITY_FILL


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        binding.viewPager.currentItem = it
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }


}
