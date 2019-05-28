package com.hfut.utils.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by wzt on 2019/5/28
 *
 */
class SPUtils {
    companion object{
        private const val FILE_NAME = "hfut_trouble"

        /**
         * 立即提交所存储的数据到SharePreference，并有结果返回
         */
        @JvmStatic
        fun putImmediatly(context: Context, key: String, obj: Any): Boolean {
            return putInternal(context,key, obj).commit()
        }

        /**
         * 它不会立即将数据同步到本地持久缓存中,只是暂时写入内存缓存,而后异步真正提交到硬件磁盘
         * 假如是退出应用前需要保存的数据，建议使用{@link #putImmediatly}
         */
        @JvmStatic
        fun put(context: Context, key: String, obj: Any) {
            putInternal(context,key, obj).apply()
        }

        @JvmStatic
        fun get(context: Context, key: String, defaultObj: Any) : Any {
            if (key.isEmpty()) {
                return defaultObj
            }
            val sp = getSP(context)
            return when (defaultObj) {
                is String -> sp.getString(key, defaultObj)
                is Int -> sp.getInt(key, defaultObj)
                is Boolean -> sp.getBoolean(key, defaultObj)
                is Float -> sp.getFloat(key, defaultObj)
                is Long -> sp.getLong(key, defaultObj)
                else -> defaultObj
            }
        }

        @JvmStatic
        fun remove(context: Context, key: String):Boolean {
            val editor = getSP(context).edit()
            return editor.remove(key).commit()
        }

        @JvmStatic
        fun clear(context: Context): Boolean {
            val editor = getSP(context).edit()
            return editor.clear().commit()
        }

        private fun putInternal(context: Context, key: String, obj: Any):SharedPreferences.Editor {
            val sp = getSP(context)
            val editor = sp.edit()
            when (obj) {
                is String -> editor.putString(key, obj)
                is Int -> editor.putInt(key, obj)
                is Boolean -> editor.putBoolean(key, obj)
                is Float -> editor.putFloat(key, obj)
                is Long -> editor.putLong(key, obj)
                else -> editor.putString(key, obj.toString())
            }
            return editor
        }

        private fun getSP(context: Context): SharedPreferences {
            return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        }
    }
}