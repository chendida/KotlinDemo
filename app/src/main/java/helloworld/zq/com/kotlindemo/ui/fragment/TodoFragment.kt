package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant

class TodoFragment : BaseFragment() {

    companion object {
        fun getInstance(type : Int) : TodoFragment{
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.TODO_TYPE,type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}