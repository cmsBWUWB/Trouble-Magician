package com.hfut.utils.callbacks

/**
 * Created by wzt on 2019/12/30
 *
 */
interface NoSucResultCallback {
    fun onSuccess()
    fun onFail(errorCode: Int, errorMsg: String)
}