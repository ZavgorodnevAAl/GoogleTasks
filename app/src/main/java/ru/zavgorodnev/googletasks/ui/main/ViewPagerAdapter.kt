package ru.zavgorodnev.googletasks.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.zavgorodnev.googletasks.ui.list.ListFragment

private const val NUMBER_OF_PAGES = 3

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUMBER_OF_PAGES

    override fun createFragment(position: Int): Fragment = ListFragment()

}