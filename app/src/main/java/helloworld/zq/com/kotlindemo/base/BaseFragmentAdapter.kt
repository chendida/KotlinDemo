package helloworld.zq.com.kotlindemo.base

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class BaseFragmentAdapter : FragmentPagerAdapter {
    private var fragmentList : List<Fragment>? = ArrayList()
    private var mTitles : List<String>? = null

    constructor(fm : FragmentManager,fragmentList : List<Fragment>) : super(fm){
        this.fragmentList = fragmentList
    }

    constructor(fm: FragmentManager, fragmentList: List<Fragment>, titles : List<String>) : super(fm){
        this.mTitles = titles
        setFragments(fm,fragmentList,titles)
    }

    //刷新fragment
    @SuppressLint("CommitTransaction")
    private fun setFragments(fm: FragmentManager, fragmentList: List<Fragment>, titles: List<String>) {
        this.mTitles = titles
        if (this.fragmentList != null){
            val ft = fm.beginTransaction()
            for (f in this.fragmentList!!){
                ft.remove(f)
            }
            ft.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
        this.fragmentList = fragmentList
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (null != mTitles) mTitles!![position] else ""
    }

    override fun getItem(p0: Int): Fragment {
        return fragmentList!![p0]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }
}