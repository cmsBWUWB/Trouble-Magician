package com.hfut.base.thread

import android.os.Handler
import android.os.Looper
import com.socks.library.KLog
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by wzt on 2019/5/15
 *
 */
class ThreadDispatcher private constructor() {
    private val threadPoolExecutor : ThreadPoolExecutor
    private val waitingQueue : UIBusinessPriorityQueue
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
        waitingQueue = UIBusinessPriorityQueue()
        threadPoolExecutor = ThreadPoolExecutor(MAXIMUM_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS, waitingQueue)
        threadPoolExecutor.allowCoreThreadTimeOut(true)
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    private object ThreadDispatcherInstance {
        //静态内部类的单例模式
        //好处是节省资源，如果完全没有调用过则不会初始化
        val instance = ThreadDispatcher()
    }

    inner class UIBusinessPriorityQueue : PriorityBlockingQueue<Runnable>() {
        override fun poll(timeout: Long, unit: TimeUnit?): Runnable? {
            val first = peek() as BusinessRunnable?
            return if (shouldExecute(first)) super.poll(timeout, unit) else null
        }
    }

    private fun shouldExecute(runnable: BusinessRunnable?): Boolean {
        return runnable != null && BusinessPriority.UI === runnable.getPriority() || MAXIMUM_POOL_SIZE - threadPoolExecutor.activeCount > HOLD_THREAD_COUNT
    }

    fun postToMainThread(run: Runnable?): Boolean {
        if (run == null) {
            KLog.e("wzt","ThreadDispatcher : postToMainThread : runnable is null")
            return false
        }
        return mainThreadHandler.post(run)
    }

    fun postToMainThreadDelayed(runnable: Runnable?, delay: Long): Boolean {
        if (runnable == null) {
            KLog.d("wzt","ThreadDispatcher : postToMainThreadDelayed : runnable is null, delayed " + delay)
            return false
        }
        return mainThreadHandler.postDelayed(runnable, delay)
    }

    fun postToBusinessThread(run: BusinessRunnable): Boolean {
        threadPoolExecutor.execute(run)
        return true
    }
}