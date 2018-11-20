package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.KnowledgeTreeContract
import helloworld.zq.com.kotlindemo.mvp.model.KnowledgeTreeModel

class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.View>(),KnowledgeTreeContract.Presenter {

    private val knowledgeTreeModel : KnowledgeTreeModel by lazy {
        KnowledgeTreeModel()
    }

    override fun requestKnowledgeTree() {
        mView?.showLoading()
        val disposable = knowledgeTreeModel.requestKnowledgeTree()
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setKnowledgeTree(results.data)
                        }
                        hideLoading()
                    }
                },{t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }
}