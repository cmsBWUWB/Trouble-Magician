package com.hfut.base.manager

import android.text.TextUtils
import com.hfut.base.application.CoreManager
import com.hfut.base.model.SPKeys
import com.hfut.imlibrary.model.User
import com.hfut.utils.callbacks.DefaultCallback
import com.hfut.utils.utils.GsonUtils
import com.hfut.utils.utils.SPUtils
import com.hfut.utils.utils.log.LogPrint
import xiaoma.com.bomb.BmobManager

/**
 * Created by wzt on 2019/5/24
 *
 */
object UserManager{
    var currentUser: User? = getUserFromCache()
    var userId: String? = currentUser?.userId

    fun uploadUserIcon(path: String,callback : DefaultCallback<Any>) {
        val prePath = currentUser?.picPath
        if(!TextUtils.equals(prePath,path)){
            currentUser?.picPath = path
            BmobManager.getInstance().update(currentUser,callback)
            currentUser?.let { updateUserToCache(it) }
        }
    }

    fun getUserIcon():String {
        return currentUser?.picPath ?: ""
    }

    private fun getUserFromCache(): User? {
        val str:String = SPUtils.get(CoreManager.getContext(), SPKeys.KEY_USER, "") as String
        LogPrint.json(UserManager.javaClass.simpleName, str)
        return GsonUtils.fromJson(str, User::class.java)
    }

    /**
     * 从服务器更新用户数据
     */
    fun updateUserFromServer() {
        userId?.let { BmobManager.getInstance().queryData("User", Array(1) { "userId" }, Array(1) { userId as String }, object : DefaultCallback<String> {
            override fun onSuccess(value: String) {
                val array = GsonUtils.fromJsonToList(value,Array<User>::class.java)
                val user = array[0]
                LogPrint.v(UserManager.javaClass.simpleName, "update {$user}")
                updateUserToCache(user)
            }

            override fun onFail(errorCode: Int, errorMsg: String) {

            }
        })}
    }

    /**
     * 存储用户数据到缓存与服务器
     */
    fun saveCurrentUser(user: User){
        currentUser = user
        LogPrint.i(UserManager.javaClass.simpleName,"save start,objectId=${user.objectId}")
        BmobManager.getInstance().saveToServer(user, object : DefaultCallback<String> {
            override fun onSuccess(value: String) {
                LogPrint.i(UserManager.javaClass.simpleName,"save success,value = $value,objectId=${user.objectId}")
                //之所以放在onSuccess里保存到SP，是由于保存之后，user的objectId才会确认
                val userJson = GsonUtils.toJson(user)
                if(userJson.isNotEmpty()) SPUtils.putImmediatly(CoreManager.getContext(), SPKeys.KEY_USER, userJson)
            }

            override fun onFail(errorCode: Int, errorMsg: String) {
                LogPrint.e(UserManager.javaClass.simpleName,"errorCode = $errorCode,errorMsg = $errorMsg" )
                //重复上传,则改为更新User
                if (errorCode == 401) {
                    updateUserToServer(user)
                }
            }

        })
    }

    /**
     * 存储用户数据到服务器
     */
    fun saveUserToServer(user: User){
        LogPrint.i(UserManager.javaClass.simpleName,"save start,objectId=${user.objectId}")
        BmobManager.getInstance().saveToServer(user, object : DefaultCallback<String> {
            override fun onSuccess(value: String) {
                LogPrint.i(UserManager.javaClass.simpleName,"save success,value = $value,objectId=${user.objectId}")
            }

            override fun onFail(errorCode: Int, errorMsg: String) {
                LogPrint.e(UserManager.javaClass.simpleName,"errorCode = $errorCode,errorMsg = $errorMsg" )
            }

        })
    }

    /**
     * 更新用户数据到缓存
     */
    fun updateUserToCache(user: User) {
        currentUser = user
        val userJson = GsonUtils.toJson(user)
        if(userJson.isNotEmpty()) SPUtils.putImmediatly(CoreManager.getContext(), SPKeys.KEY_USER, userJson)
    }

    /**
     * 更新用户数据到服务器
     */
    fun updateUserToServer(user: User) {
        BmobManager.getInstance().update(user, object : DefaultCallback<Any> {
            override fun onSuccess(value: Any) {
                LogPrint.i(UserManager.javaClass.simpleName,"update success")
            }

            override fun onFail(errorCode: Int, errorMsg: String) {
                LogPrint.e(UserManager.javaClass.simpleName,"errorCode = $errorCode,errorMsg = $errorMsg" )
            }

        })
    }

    /**
     * 清空缓存用户数据
     */
    fun clearCurrentUser(): Boolean{
        currentUser = null
        return SPUtils.remove(CoreManager.getContext(), SPKeys.KEY_USER)
    }
}