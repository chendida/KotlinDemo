package helloworld.zq.com.kotlindemo.mvp.contract

interface ProjectListContract {
    interface View : CommonContract.View{
        fun scrollToTop()
    }
}