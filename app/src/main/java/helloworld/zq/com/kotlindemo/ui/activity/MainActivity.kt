package helloworld.zq.com.kotlindemo.ui.activity

import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.widget.TextView
import com.tencent.bugly.beta.Beta
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.MainContract
import helloworld.zq.com.kotlindemo.utils.Preference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
class MainActivity : BaseActivity(),MainContract.View {


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
        Beta.checkUpgrade(false,false)
    }

    override fun initView() {
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
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.action_home -> {
                        //showFragment(FRAGMENT_HOME)
                        true
                    }
                    R.id.action_knowledge_system -> {
                        //showFragment(FRAGMENT_KNOWLEDGE)
                        true
                    }
                    R.id.action_navigation -> {
                        //showFragment(FRAGMENT_NAVIGATION)
                        true
                    }
                    R.id.action_project -> {
                        //showFragment(FRAGMENT_PROJECT)
                        true
                    }
                    R.id.action_wechat -> {
                        //showFragment(FRAGMENT_WECHAT)
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
                    R.id.nav_collect -> {
                        if (isLogin) {
                            /*Intent(this@MainActivity, CommonActivity::class.java).run {
                                putExtra(Constant.TYPE_KEY, Constant.Type.COLLECT_TYPE_KEY)
                                startActivity(this)
                            }*/
                        } else {
                            /*showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }*/
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_setting -> {
                        /*Intent(this@MainActivity, SettingActivity::class.java).run {
                            // putExtra(Constant.TYPE_KEY, Constant.Type.SETTING_TYPE_KEY)
                            startActivity(this)
                        }*/
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_about_us -> {
                        /*Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                            startActivity(this)
                        }*/
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_logout -> {
                        //logout()
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_night_mode -> {
                        /*if (SettingUtil.getIsNightMode()) {
                            SettingUtil.setIsNightMode(false)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        } else {
                            SettingUtil.setIsNightMode(true)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)*/
                        recreate()
                    }
                    R.id.nav_todo -> {
                        if (isLogin) {
                            /*Intent(this@MainActivity, TodoActivity::class.java).run {
                                startActivity(this)
                            }*/
                        } else {
                            /*showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }*/
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                }
                true
            }

    override fun start() {
    }
}
