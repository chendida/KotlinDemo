package helloworld.zq.com.kotlindemo.mvp.contract

interface KnowledgeContract {
    interface View : CommonContract.View{
        fun scrollToTop()
    }
}