package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

class SearchListFragment : BaseFragment() {
    companion object {
        fun getInstance(bundle: Bundle) : SearchListFragment{
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_search_list

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}