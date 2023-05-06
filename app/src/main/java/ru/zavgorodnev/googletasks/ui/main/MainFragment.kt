package ru.zavgorodnev.googletasks.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import ru.zavgorodnev.googletasks.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val tabTitles = listOf<String>("Избранные", "Все задачи", "Выполненные")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.categoryViewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager2) { tab, pos ->
            tab.text = tabTitles[pos]
        }.attach()
    }

}