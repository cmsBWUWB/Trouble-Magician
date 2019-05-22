package com.hfut.base.application

import android.content.Context

/**
 * Created by wzt on 2019/5/22
 *
 */
class CoreManager {
    companion object{
        private var appContext: Context? = null

        @JvmStatic
        fun init(context: Context?) {
            if (context != null) {
                appContext = context.applicationContext
            }
        }

        @JvmStatic
        fun getContext(): Context? {
            return appContext
        }
    }
}