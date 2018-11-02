package helloworld.zq.com.kotlindemo.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.NetworkChangeEvent
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.widget.MultipleStatusView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment : Fragment() {
    /**
     * check login
     */
    protected var isLogin : Boolean by Preference(Constant.LOGIN_KEY,false)

    /**
     * 缓存上一次网络状态
     */
    protected var hasNetwork : Boolean by Preference(Constant.HAS_NETWORK_KEY,true)

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false

    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    /**
     * 多种状态的View的切换
     */
    protected var mLayoutStatusView : MultipleStatusView? = null

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun attachLayoutRes():Int

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    /**
     * 是否使用EventBus
     */
    open fun useEventBus() : Boolean = true

    /**
     * 无网状态—>有网状态的自动重连操作，子类可重写
     */
    open fun doReConnected(){
        lazyLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(attachLayoutRes(),null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            lazyLoadDataIfPrepare()
        }
    }

    private fun lazyLoadDataIfPrepare() {
        if (userVisibleHint && isViewPrepare && !hasLoadData){
            lazyLoad()
            hasLoadData = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus()){
            EventBus.getDefault().register(this)
        }
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepare()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event:NetworkChangeEvent){
        if (event.isConnected){
            doReConnected()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()){
            EventBus.getDefault().unregister(this)
        }
        activity?.let { App.getRefWatcher(it)?.watch(activity) }
    }
}