package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseSwipeBackActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.RefreshEvent
import helloworld.zq.com.kotlindemo.mvp.contract.CommonContract
import helloworld.zq.com.kotlindemo.mvp.presenter.CommonPresenter
import helloworld.zq.com.kotlindemo.utils.getAgentWeb
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.container.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class ContentActivity : BaseSwipeBackActivity(),CommonContract.View {

    override fun attachLayoutRes(): Int = R.layout.activity_content

    private var agentWeb : AgentWeb? = null
    private lateinit var shareTitle : String
    private lateinit var shareUrl : String
    private var shareId : Int = 0

    private val mPresenter by lazy {
        CommonPresenter<CommonContract.View>()
    }

    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)

        toolbar.run {
            title = getString(R.string.loading)
            setSupportActionBar(this)//将toolbar转换成actionbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)//给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        }

        intent.extras.let {
            shareId = it!!.getInt(Constant.CONTENT_ID_KEY,-1)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY,"")
            shareUrl = it.getString(Constant.CONTENT_URL_KEY,"")
        }
    }

    override fun start() {
        agentWeb = shareUrl.getAgentWeb(this, container,
                LinearLayout.LayoutParams(-1, -1),
                webChromeClient,
                webViewClient)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share ->{
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,getString(
                            R.string.share_article_url,
                            getString(R.string.app_name),
                            shareTitle,
                            shareUrl
                    ))
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this,getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like ->{
                if (isLogin) {
                    mPresenter.addCollectArticle(shareId)
                } else {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showToast(resources.getString(R.string.login_tint))
                }
                return true
            }
            R.id.action_browser ->{
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode,event)!!) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()

    }

    /**
     * webViewClient
     */
    private val webViewClient = object : WebViewClient() {
    }

    /**
     * webChromeClient
     */
    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            title.let {
                toolbar.title = it
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.collect_success))
            EventBus.getDefault().post(RefreshEvent(true))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshEvent(true))
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }
}
