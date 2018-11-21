package helloworld.zq.com.kotlindemo.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.tencent.bugly.beta.Beta
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.ColorEvent
import helloworld.zq.com.kotlindemo.event.LoginEvent
import helloworld.zq.com.kotlindemo.event.RefreshEvent
import helloworld.zq.com.kotlindemo.mvp.contract.MainContract
import helloworld.zq.com.kotlindemo.mvp.presenter.MainPresenter
import helloworld.zq.com.kotlindemo.ui.fragment.*
import helloworld.zq.com.kotlindemo.utils.DialogUtil
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class MainActivity : BaseActivity(),MainContract.View {
    private val BOTTOM_INDEX : String = "bottom_index"

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE = 0x02
    private val FRAGMENT_WECHAT = 0x03
    private val FRAGMENT_NAVIGATION = 0x04
    private val FRAGMENT_PROJECT = 0x05

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment : HomeFragment? = null
    private var mKnowledgeTreeFragment : KnowledgeTreeFragment? = null
    private var mWeChatFragment : WeChatFragment? = null
    private var mNavigationFragment : NavigationFragment? = null
    private var mProjectFragment : ProjectFragment? = null

    private val mPresenter : MainPresenter by lazy {
        MainPresenter()
    }

    override fun useEventBus(): Boolean = true

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    /**
     * 退出登录成功后的处理
     */
    override fun showLogoutSuccess(success: Boolean) {
        if (success){
            doAsync {
                Preference.clearPreference()
                uiThread { // anko库中的异步操作，只是持有外部Activity的弱引用，防止内存泄漏
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                    Intent(this@MainActivity,LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_main

    /**
     * local username
     */
    private val username : String by Preference(Constant.USERNAME_KEY,"")


    /**
     * username TextView
     */
    private var nav_username: TextView? = null

    override fun initData() {
        //版本检测
        Beta.checkUpgrade(false,false)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BOTTOM_INDEX,mIndex)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null){
            mIndex = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initColor() {
        super.initColor()
        ColorEvent(true)
    }

    override fun initView() {
        mPresenter.attachView(this)
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawerLayout()

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username?.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }
            setOnClickListener {
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                } else {

                }
            }
        }

        showFragment(mIndex)

        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    /**
     * FAB 监听
     */
    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                mHomeFragment?.scrollToTop()
            }
            FRAGMENT_KNOWLEDGE -> {
                mKnowledgeTreeFragment?.scrollToTop()
            }
            FRAGMENT_NAVIGATION -> {
                mNavigationFragment?.scrollToTop()
            }
            FRAGMENT_PROJECT -> {
                //mProjectFragment?.scrollToTop()
            }
            FRAGMENT_WECHAT -> {
                mWeChatFragment?.scrollToTop()
            }
        }
    }

    /**
     * init DrawerLayout
     */
    private fun initDrawerLayout() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            var params = window.attributes
//            params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//            drawer_layout.fitsSystemWindows = true
//            drawer_layout.clipToPadding = false
//        }
        drawer_layout.run {
            var toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    this,
                    toolbar
                    , R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }

    }

    /**
     * 展示fragment
     * @param index
     */
    private fun showFragment(index : Int){
        val transaction  = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        mIndex = index
        when(index){
            FRAGMENT_HOME //首页
            -> {
                toolbar.title = getString(R.string.app_name)
                if (mHomeFragment == null){
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container,mHomeFragment!!,"home")
                }else{
                    transaction.show(mHomeFragment!!)
                }
            }
            FRAGMENT_KNOWLEDGE // 知识体系
            -> {
                toolbar.title = getString(R.string.knowledge_system)
                if (mKnowledgeTreeFragment == null){
                    mKnowledgeTreeFragment = KnowledgeTreeFragment.getInstance()
                    transaction.add(R.id.container,mKnowledgeTreeFragment!!,"knowledge")
                }else{
                    transaction.show(mKnowledgeTreeFragment!!)
                }
            }
            FRAGMENT_WECHAT //公众号
            -> {
                toolbar.title = getString(R.string.wechat)
                if (mWeChatFragment == null){
                    mWeChatFragment = WeChatFragment.getInstance()
                    transaction.add(R.id.container,mWeChatFragment!!,"weChat")
                }else{
                    transaction.show(mWeChatFragment!!)
                }
            }
            FRAGMENT_NAVIGATION //导航
            -> {
                toolbar.title = getString(R.string.navigation)
                if (mNavigationFragment == null){
                    mNavigationFragment = NavigationFragment.getInstance()
                    transaction.add(R.id.container,mNavigationFragment!!,"navigation")
                }else{
                    transaction.show(mNavigationFragment!!)
                }
            }
            FRAGMENT_PROJECT //项目
            -> {
                toolbar.title = getString(R.string.project)
                if (mProjectFragment == null){
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container,mProjectFragment!!,"project")
                }else{
                    transaction.show(mProjectFragment!!)
                }
            }
        }
        transaction.commit()
    }

    /**
     * 隐藏所有的fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mKnowledgeTreeFragment?.let { transaction.hide(it) }
        mWeChatFragment?.let { transaction.hide(it) }
        mNavigationFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.action_home -> {
                        showFragment(FRAGMENT_HOME)
                        true
                    }
                    R.id.action_knowledge_system -> {
                        showFragment(FRAGMENT_KNOWLEDGE)
                        true
                    }
                    R.id.action_navigation -> {
                        showFragment(FRAGMENT_NAVIGATION)
                        true
                    }
                    R.id.action_project -> {
                        showFragment(FRAGMENT_PROJECT)
                        true
                    }
                    R.id.action_wechat -> {
                        showFragment(FRAGMENT_WECHAT)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_collect -> {//收藏
                        if (isLogin) {
                            Intent(this@MainActivity, CommonActivity::class.java).run {
                                putExtra(Constant.TYPE_KEY, Constant.Type.COLLECT_TYPE_KEY)
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_setting -> {//设置
                        Intent(this@MainActivity, SettingActivity::class.java).run {
                            // putExtra(Constant.TYPE_KEY, Constant.Type.SETTING_TYPE_KEY)
                            startActivity(this)
                        }
                        //drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_about_us -> {
                        Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                            startActivity(this)
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_logout -> {
                        logout()
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_night_mode -> {
                        if (SettingUtil.getIsNightMode()) {
                            SettingUtil.setIsNightMode(false)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        } else {
                            SettingUtil.setIsNightMode(true)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                        recreate()
                    }
                    R.id.nav_todo -> {
                        if (isLogin) {
                            Intent(this@MainActivity, TodoActivity::class.java).run {
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                }
                true
            }
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this,resources.getString(R.string.logout_ing))
    }

    /**
     * 退出登录
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this,resources.getString(R.string.confirm_logout),
                DialogInterface.OnClickListener{_,_->
                    mDialog.show()
                    mPresenter.logout()
                }).show()
    }

    override fun start() {
    }

    /**
     * 内存重启和夜间和日间模式切换时调用
     */
    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mHomeFragment != null){
                fragmentTransaction.remove(mHomeFragment!!)
            }
            if (mKnowledgeTreeFragment != null){
                fragmentTransaction.remove(mKnowledgeTreeFragment!!)
            }
            if (mWeChatFragment != null){
                fragmentTransaction.remove(mWeChatFragment!!)
            }
            if (mNavigationFragment != null){
                fragmentTransaction.remove(mNavigationFragment!!)
            }
            if (mProjectFragment != null){
                fragmentTransaction.remove(mProjectFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        }catch (e : Exception){
            e.printStackTrace()
        }
        super.recreate()
    }

    /**
     * 搜索菜单栏
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_search->{
                Intent(this,SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 2s内连续双击退出程序
     */
    private var mExitTime : Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis().minus(mExitTime) <= 2000){
                finish()
            }else{
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent){
        if (event.isLogin){
            nav_username?.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
        }else{
            nav_username?.text = resources.getString(R.string.login)
            nav_view.menu.findItem(R.id.nav_logout).isVisible = false
        }
        mHomeFragment?.lazyLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event:RefreshEvent){
        if (event.isRefresh){
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColorEvent(event: ColorEvent){
        if (event.isRefresh){
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeFragment = null
        mKnowledgeTreeFragment = null
        mNavigationFragment = null
        mWeChatFragment = null
        mProjectFragment = null
        nav_username = null
    }
}
