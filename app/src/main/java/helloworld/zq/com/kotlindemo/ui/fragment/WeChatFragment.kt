package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

/**
 * 公众号
 */
class WeChatFragment : BaseFragment() {
    companion object {
        fun getInstance() : WeChatFragment = WeChatFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_wechat

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}