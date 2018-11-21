package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.ProjectListContract

class ProjectListFragment : BaseFragment(),ProjectListContract.View {
    override fun scrollToTop() {
    }

    override fun showCollectSuccess(success: Boolean) {
    }

    override fun showCancelCollectSuccess(success: Boolean) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
    }


    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    companion object {
        fun getInstance(cid : Int) : ProjectListFragment{
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY,cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}