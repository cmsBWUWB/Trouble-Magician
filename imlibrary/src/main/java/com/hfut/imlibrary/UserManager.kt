package com.hfut.imlibrary

import android.text.TextUtils
import com.hfut.imlibrary.model.User
import com.hfut.utils.callbacks.DefaultCallback
import xiaoma.com.bomb.BmobManager

/**
 * Created by wzt on 2019/5/24
 *
 */
object UserManager{
    var currentUser: User? = null
    var userId: String? = currentUser?.userId

    fun uploadUserIcon(path: String,callback : DefaultCallback<Any>) {
        val prePath = currentUser?.picPath
        if(!TextUtils.equals(prePath,path)){
            currentUser?.picPath = path
            BmobManager.getInstance().update(currentUser,callback)
        }
    }

    fun getUserIcon():String {
        return currentUser?.picPath ?: ""
    }
}