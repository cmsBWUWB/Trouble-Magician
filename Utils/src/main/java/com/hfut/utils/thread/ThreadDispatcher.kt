package com.hfut.base.thread

import android.os.Handler
import android.os.Looper
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by wzt on 2019/5/15
 *
 */
class ThreadDispatcher private constructor() {
    private val threadPoolExecutor : ThreadPoolExecutor
    private val waitingQueue : PriorityBlockingQueue<Runnable> = PriorityBlockingQueue()
    private val mainThreadHandler : Handler

    companion object {
        @JvmStatic
        fun getInstance() = ThreadDispatcherInstance.instance

        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val HOLD_THREAD_COUNT = 1//保留线程数
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + HOLD_THREAD_COUNT
        private val KEEP_ALIVE_SECONDS = 30
    }

    init {
        threadPoolExecutor = ThreadPoolExecutor(MAXIMUM_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS, waitingQueue)
        threadPoolExecutor.allowCoreThreadTimeOut(true)
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    private object ThreadDispatcherInstance {
        //静态内部类的单例模式
        //好处是节省资源，如果完全没有调用过则不会初始化
        val instance = ThreadDispatcher()
    }

    private fun createBusinessRunnable(priority: BusinessPriority, runnable: Runnable): BusinessRunnable {
        return object : BusinessRunnable(priority) {
            override fun doWorkInRun() {
                runnable.run()
            }
        }
    }

    /**
     * 提交任务至主线程执行
     */
    fun postToMainThread(run: Runnable): Boolean {
        return mainThreadHandler.post(run)
    }

    /**
     * 提交任务至主线程延迟执行
     */
    fun postToMainThreadDelayed(runnable: Runnable, delay: Long): Boolean {
        return mainThreadHandler.postDelayed(runnable, delay)
    }

    /**
     * 提交任务至子线程执行
     */
    fun postToBusinessThread(run: BusinessRunnable) {
        threadPoolExecutor.execute(run)
    }

    /**
     * 延时提交业务runnable，runnable需设置好对应业务优先级，默认为Normal
     */
    fun postBusinessRunnableDelayed(runnable: BusinessRunnable, delay: Long): Boolean {
        return postToMainThreadDelayed(Runnable { postToBusinessThread(runnable)}, delay)
    }

    /**
     * 提交普通业务runnable
     */
    fun postNormalBusinessRunnable(runnable: Runnable) {
        postToBusinessThread(createBusinessRunnable(BusinessPriority.NORMAL, runnable))
    }

    /**
     * 提交第三方相关业务runnable
     */
    fun postThirdPartyBusinessRunnable(runnable: Runnable) {
        postToBusinessThread(createBusinessRunnable(BusinessPriority.THIRD_PARTY,runnable))
    }

    /**
     * 从队列中移除主线程延时runnable
     */
    fun removeRunnableFromMainThread(runnable: Runnable){
        mainThreadHandler.removeCallbacks(runnable)
    }

    /**
     * 从队列中移除相关业务runnable
     */
    fun removeBusinessRunnable(runnable: BusinessRunnable) {
        mainThreadHandler.removeCallbacks(runnable)
        waitingQueue.remove(runnable)
    }
}