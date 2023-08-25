package com.example.cbc_main.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cbc_main.Tip1Fragment
import com.example.cbc_main.Tip2Fragment
import com.example.cbc_main.Tip3Fragment
import com.example.cbc_main.Tip4Fragment


class PagerAdapter (fm : FragmentManager): FragmentPagerAdapter(fm) {
    //position 에 따라 원하는 Fragment로 이동시키기
    override fun getItem(position: Int): Fragment {
        val fragment =  when(position)
        {
            0-> Tip4Fragment().newInstant()
            1-> Tip1Fragment().newInstant()
            2-> Tip2Fragment().newInstant()
            else-> Tip3Fragment().newInstant()
        }
        return fragment
    }

    //tab의 개수만큼 return
    override fun getCount(): Int = 4

    //tab의 이름 fragment마다 바꾸게 하기
    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0->"부품주기\n\nO"
            1->"탈때마다\n\nO"
            2->"일주일\n" +
                    "\n" +
                    "O"
            else->"한달\n" +
                    "\n" +
                    "O"
        }
        return title     }
}