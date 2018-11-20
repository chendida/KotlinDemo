package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.mvp.contract.KnowledgeContract
import helloworld.zq.com.kotlindemo.mvp.model.KnowledgeModel

class KnowledgePresenter : CommonPresenter<KnowledgeContract.View>(),KnowledgeContract.Presenter {

    private val knowledgeModel : KnowledgeModel by lazy {
        KnowledgeModel()
    }

    override fun requestKonwledgeList(page: Int, cid: Int) {
        mView?.showLoading()

        val disposable = knowledgeModel.getKnowledgeList(page,cid)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setKnowledgeList(results.data)
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