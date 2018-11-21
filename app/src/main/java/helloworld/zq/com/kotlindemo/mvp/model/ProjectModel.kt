package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.ProjectTreeBean
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class ProjectModel : BaseModel() {

    fun getProjectTree() : Observable<HttpResult<List<ProjectTreeBean>>>{
        return RetrofitHelper.service.getProjectTree()
                .compose(SchedulerUtils.ioToMain())
    }
}