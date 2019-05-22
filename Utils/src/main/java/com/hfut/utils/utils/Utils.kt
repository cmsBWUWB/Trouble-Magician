package com.hfut.utils.utils

import android.content.Context
import android.content.pm.ApplicationInfo

/**
 * Created by wzt on 2019/5/16
 *
 */
class Utils {
    companion object {
        /**
         * 判断当前应用是否是debug模式
         */
        @JvmStatic
        fun isDebuggable(context: Context): Boolean {
            return try {
                val info = context.applicationInfo
                info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        @JvmStatic
        fun isListEmpty(list: Collection<*>?): Boolean {
            return list == null || list.isEmpty()
        }
    }
}