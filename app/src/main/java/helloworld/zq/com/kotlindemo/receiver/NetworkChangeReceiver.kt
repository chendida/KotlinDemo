package helloworld.zq.com.kotlindemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.NetworkChangeEvent
import helloworld.zq.com.kotlindemo.utils.NetWorkUtil
import helloworld.zq.com.kotlindemo.utils.Preference
import org.greenrobot.eventbus.EventBus

/**
 * Created by chenxz on 2018/8/1.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}