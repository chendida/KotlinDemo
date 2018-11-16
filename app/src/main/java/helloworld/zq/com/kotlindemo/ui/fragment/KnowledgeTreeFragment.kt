package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

/**
 * 知识体系
 */
class KnowledgeTreeFragment : BaseFragment() {
    companion object {
        fun getInstance() : KnowledgeTreeFragment = KnowledgeTreeFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_knowledge

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}