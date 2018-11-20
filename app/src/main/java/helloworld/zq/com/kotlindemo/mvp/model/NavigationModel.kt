package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.NavigationBean
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class NavigationModel : BaseModel() {
    fun getNavigationList() : Observable<HttpResult<List<NavigationBean>>>{
        return RetrofitHelper.service.getNavigationList()
                .compose(SchedulerUtils.ioToMain())
    }
}