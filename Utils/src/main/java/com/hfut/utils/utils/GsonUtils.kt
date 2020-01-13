package com.hfut.utils.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hfut.utils.utils.log.LogPrint
import java.lang.reflect.Type
import java.util.*

/**
 * Created by wzt on 2019/5/16
 *
 */
class GsonUtils{
    companion object {
        fun toJson(src: Any?): String {
            if (src == null) {
                return ""
            }
            val gson = Gson()
            return gson.toJson(src)
        }

        fun toJson(src: Any?, typeOfSrc: Type): String {
            if (src == null) {
                return ""
            }
            val gson = Gson()
            return gson.toJson(src, typeOfSrc)
        }

        fun <T> fromJson(json: String?, classOfT: Class<T>): T? {
            if (TextUtils.isEmpty(json)) {
                return null
            }
            val gson = Gson()
            return try {
                gson.fromJson(json, classOfT)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                null
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                null
            }

        }

        fun <T> fromJson(json: String?, type: Type): T? {
            if (TextUtils.isEmpty(json)) {
                return null
            }
            val gson = Gson()
            try {
                return gson.fromJson<T>(json, type)
            } catch (e: Exception) {
                LogPrint.json(json)
                e.printStackTrace()
            }

            return null
        }

        fun <T> fromJsonToList(json: String?, clazz: Class<Array<T>>): List<T> {
            if (TextUtils.isEmpty(json)) {
                return ArrayList()
            }
            try {
                val arr = Gson().fromJson(json, clazz)
                if (arr == null || arr.isEmpty()) {
                    return ArrayList()
                }
                val ts = Arrays.asList(*arr)
                return ArrayList(ts)
            } catch (e: JsonSyntaxException) {
                LogPrint.json(json)
                e.printStackTrace()
                return ArrayList()
            }

        }
    }
}