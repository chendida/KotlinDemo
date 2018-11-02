package helloworld.zq.com.kotlindemo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.utils.CommonUtil
import helloworld.zq.com.kotlindemo.utils.DisplayManager
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import helloworld.zq.com.kotlindemo.utils.showToast
import org.litepal.LitePal
import java.util.*
import kotlin.properties.Delegates

class App : MultiDexApplication() {
    private var refWatcher : RefWatcher? = null

    /**
     * 伴生对象
     */
    companion object {
        private val TAG = "App"

        var context : Context by Delegates.notNull()
            private set

        lateinit var instance : Application

         fun getRefWatcher(context: Context) : RefWatcher?{
             val app = context.applicationContext as App
             return app.refWatcher
         }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        refWatcher = setupLeakCanary()
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        initTheme()
        initLitePal()
        initBugly()
    }

    /**
     * 初始化Bugly
     */
    private fun initBugly() {
        //获取当前包名
        val packageName = applicationContext.packageName
        //获取当前进程名
        val processName = CommonUtil.getProcessName(Process.myPid())
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onDownloadCompleted(isManual: Boolean) {
            }

            override fun onUpgradeSuccess(isManual: Boolean) {
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_version_fail))
                }
            }

            override fun onUpgrading(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_version_ing))
                }
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_no_version))
                }
            }
        }
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(applicationContext)
        strategy.isUploadProcess = false || processName == packageName
        // CrashReport.initCrashReport(applicationContext, Constant.BUGLY_ID, BuildConfig.DEBUG, strategy)
        Bugly.init(applicationContext, Constant.BUGLY_ID, BuildConfig.DEBUG, strategy)
    }

    /**
     * 初始化LitePal
     */
    private fun initLitePal() {
        LitePal.initialize(this)
    }

    /**
     * 初始化主题
     */
    private fun initTheme() {
        if (SettingUtil.getIsAutoNightMode()) {
            val nightStartHour = SettingUtil.getNightStartHour().toInt()
            val nightStartMinute = SettingUtil.getNightStartMinute().toInt()
            val dayStartHour = SettingUtil.getDayStartHour().toInt()
            val dayStartMinute = SettingUtil.getDayStartMinute().toInt()

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val nightValue = nightStartHour * 60 + nightStartMinute
            val dayValue = dayStartHour * 60 + dayStartMinute
            val currentValue = currentHour * 60 + currentMinute

            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SettingUtil.setIsNightMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SettingUtil.setIsNightMode(false)
            }
        } else {
            // 获取当前的主题
            if (SettingUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    /**
     * 生命周期的注册回调
     */
    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks{
        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityPaused(activity: Activity) {

        }
    }

    /**
     * 初始化日志打印
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(0)
                .methodOffset(7)
                .tag("first kotlin")
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    /**
     * 初始化内存检测
     */
    private fun setupLeakCanary() : RefWatcher{
        return if (LeakCanary.isInAnalyzerProcess(this)){
            RefWatcher.DISABLED
        }else{
            LeakCanary.install(this)
        }
    }
}