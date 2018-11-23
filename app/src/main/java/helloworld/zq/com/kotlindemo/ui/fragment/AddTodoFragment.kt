package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

class AddTodoFragment : BaseFragment() {
    companion object {
        fun getInstance(bundle: Bundle) : AddTodoFragment{
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_add_todo

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}