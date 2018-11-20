package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.KnowledgeTreeBody
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class KnowledgeTreeModel : BaseModel() {
    fun requestKnowledgeTree() : Observable<HttpResult<List<KnowledgeTreeBody>>>{
        return RetrofitHelper.service.getKnowledgeTree()
                .compose(SchedulerUtils.ioToMain())
    }
}