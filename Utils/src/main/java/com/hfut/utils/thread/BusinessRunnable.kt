package com.hfut.utils.thread

import android.os.Looper
import android.os.Process

/**
 * Created by wzt on 2019/5/15
 *
 */
abstract class BusinessRunnable : Runnable, Comparable<BusinessRunnable>{
    private var mPriority: BusinessPriority = DEFAULT_PRIORITY
    abstract fun doWorkInRun()
    companion object {
        private val DEFAULT_PRIORITY = BusinessPriority.NORMAL
    }

    override fun compareTo(other: BusinessRunnable): Int {
        return mPriority.value() - other.mPriority.value()
    }

    constructor()

    constructor(businessPriority : BusinessPriority){
        mPriority = businessPriority
    }

    fun setPriority(priority: BusinessPriority) {
        this.mPriority = priority
    }

    fun getPriority(): BusinessPriority {
        return mPriority
    }

    override fun run() {
        //非主线程时才执行线程优先级的设置,避免在主UI线程中执行setThreadPriority
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Process.setThreadPriority(mPriority.value())
            doWorkInRun()
        } else {
            doWorkInRun()
        }
    }
}