package helloworld.zq.com.kotlindemo.base

import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.afollestad.materialdialogs.color.CircleView
import com.just.agentweb.AgentWebUtils.checkNetwork
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.NetworkChangeEvent
import helloworld.zq.com.kotlindemo.receiver.NetworkChangeReceiver
import helloworld.zq.com.kotlindemo.utils.CommonUtil
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import helloworld.zq.com.kotlindemo.utils.StatusBarUtil
import helloworld.zq.com.kotlindemo.widget.MultipleStatusView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {
    /**
     * check login
     */
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    /**
     * 缓存上一次网络状态
     */
    protected var hasNetwork:Boolean by Preference(Constant.HAS_NETWORK_KEY,true)

    /**
     * 网络状态变化的广播
     */
    protected var mNetworkChangeReceiver:NetworkChangeReceiver? = null

    /**
     * 主题颜色
     */
    protected var mThemeColor: Int = SettingUtil.getColor()

    /**
     * 多种状态的 View的切换
     */
    protected var mLayoutStatusView : MultipleStatusView? = null

    /**
     * 提示View
     */
    protected lateinit var mTipView : View
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    /**
     * 布局文件id
     */
    protected abstract fun attachLayoutRes() : Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * 是否使用EventBus
     */
    open fun useEventBus() : Boolean = true

    /**
     * 是否需要显示TipView
     */
    open fun enableNetworkTip() : Boolean = true

    /**
     * 无网状态—>有网状态 的自动重连操作，子类可重写该方法
     */
    open fun doReConnected(){
        start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        if (useEventBus()){
            EventBus.getDefault().register(this)
        }
        initData()
        iniTipView()
        initView()
        start()
        initListener()
    }

    /**
     * 初始化TipView
     */
    private fun iniTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip,null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.windowAnimations = R.style.anim_float_view
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    /**
     * 重试的点击监听
     */
    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        start()
    }

    override fun onResume() {
        //动态注册网络变化广播
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver,filter)

        super.onResume()

        initColor()
    }

    open fun initColor() {
        mThemeColor = if (!SettingUtil.getIsNightMode()){
            SettingUtil.getColor()
        }else{
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this,mThemeColor,0)
        if (supportActionBar != null){
            supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = CircleView.shiftColorDown(mThemeColor)
//            // 最近任务栏上色
//            val tDesc = ActivityManager.TaskDescription(
//                    getString(R.string.app_name),
//                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
//                    mThemeColor)
//            setTaskDescription(tDesc)
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = CircleView.shiftColorDown(mThemeColor)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }

    protected fun initToolBar(toolbar: Toolbar, homeAsUpEnabled : Boolean, title : String){
        toolbar?.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event:NetworkChangeEvent){
        hasNetwork = event.isConnected
        checkNetwork(event.isConnected)
    }

    /**
     * 网络状态变化的操作
     */
    private fun checkNetwork(isConnected: Boolean) {
        if (enableNetworkTip()){
            if (isConnected){
                doReConnected()
                if (mTipView != null && mTipView.parent != null){
                    mWindowManager.removeView(mTipView)
                }
            }else{
                if (mTipView.parent != null){
                    mWindowManager.addView(mTipView,mLayoutParams)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onPause() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        CommonUtil.fixInputMethodManagerLeak(this)
        App.getRefWatcher(this)?.watch(this)
    }

    override fun finish() {
        super.finish()
        if (mTipView != null && mTipView.parent != null) {
            mWindowManager.removeView(mTipView)
        }
    }
}