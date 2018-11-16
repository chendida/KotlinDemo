package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

/**
 * 项目
 */
class ProjectFragment : BaseFragment() {
    companion object {
        fun getInstance() : ProjectFragment = ProjectFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_project

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}