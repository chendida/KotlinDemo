package helloworld.zq.com.kotlindemo.rx

import helloworld.zq.com.kotlindemo.rx.scheduler.IoMainScheduler

object SchedulerUtils {
    fun <T> ioToMain() : IoMainScheduler<T>{
        return IoMainScheduler()
    }
}