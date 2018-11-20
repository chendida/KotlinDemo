package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.KnowledgeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody

/**
 * 公众号碎片显示的fragment
 */
class KnowledgeFragment : BaseFragment(),KnowledgeContract.View {

    override fun setKnowledgeList(articles: ArticleResponseBody) {
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

    override fun scrollToTop() {
    }

    companion object {
        fun getInstance(cid : Int) : KnowledgeFragment{
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY,cid)
            fragment.arguments = args
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_knowledge

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}
