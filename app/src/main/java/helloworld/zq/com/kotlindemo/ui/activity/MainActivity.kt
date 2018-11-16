package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.widget.TextView
import com.tencent.bugly.beta.Beta
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.MainContract
import helloworld.zq.com.kotlindemo.mvp.presenter.MainPresenter
import helloworld.zq.com.kotlindemo.ui.fragment.*
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

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

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showLogoutSuccess(success: Boolean) {
    }

    override fun showError(errMsg: String) {
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
                    /*Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }*/
                } else {

                }
            }
        }

        //showFragment(mIndex)

        floating_action_btn.run {
            //setOnClickListener(onFABClickListener)
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

    /**
     * 退出登录
     */
    private fun logout() {
    }

    override fun start() {
    }
}
