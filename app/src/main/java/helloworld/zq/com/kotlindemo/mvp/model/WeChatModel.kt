package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.WXChapterBean
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class WeChatModel : BaseModel() {

    fun getWXChapters() : Observable<HttpResult<MutableList<WXChapterBean>>>{
        return RetrofitHelper.service.getWXChapters()
                .compose(SchedulerUtils.ioToMain())
    }
}