package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HotSearchBean
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class SearchModel : BaseModel(){

    fun searchHotData() : Observable<HttpResult<MutableList<HotSearchBean>>>{
        return RetrofitHelper.service.getHotSearchData()
                .compose(SchedulerUtils.ioToMain())
    }
}