package com.hfut.utils.model

import com.hfut.utils.utils.GsonUtils

/**
 * Created by wzt on 2019/5/16
 * 用户类
 */
data class User(var id: Long, var createDate: Long, var name: String, var hxAccount: String?, var hxPassword: String?, var phone: String?, var age: Int, var gender: Int) {
    companion object {
        @JvmStatic
        fun fromJson(json: String): User? {
            return GsonUtils.fromJson(json, User::class.java)
        }
    }
}