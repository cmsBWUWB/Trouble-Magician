package com.hfut.utils.callbacks

/**
 * Created by wzt on 2019/5/22
 *
 */
interface DefaultCallback<T> {
    fun onSuccess(value : T)
    fun onFail(errorCode: Int, errorMsg: String)
}