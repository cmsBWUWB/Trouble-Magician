package com.hfut.base.thread

import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.os.Process.THREAD_PRIORITY_DEFAULT

/**
 * Created by wzt on 2019/5/15
 * 业务优先级，priority越小，优先级越高
 */
enum class BusinessPriority (private var priority: Int){
    UI((THREAD_PRIORITY_DEFAULT + THREAD_PRIORITY_BACKGROUND) / 2),//UI界面业务

    NORMAL(THREAD_PRIORITY_BACKGROUND),//普通业务

    NETWORK(NORMAL.value() + getPriorityIncGradient()),//网络业务

    THIRD_PARTY(NETWORK.value() + getPriorityIncGradient()),//第三方业务

    DOWNLOAD(THIRD_PARTY.value() + getPriorityIncGradient());//下载业务

    fun value(): Int {
        return priority
    }
}

/**
 * 不同等级的优先级增加梯度
 *
 * @return
 */
fun getPriorityIncGradient(): Int {
    return 2
}