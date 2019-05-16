package com.hfut.utils.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Created by wzt on 2019/5/16
 *
 */
class ToastUtils{
    companion object {
        private var toast : Toast? = null

        fun show(context : Context, resId :Int, duration: Int = Toast.LENGTH_SHORT){
            if (resId <= 0) {
                return
            }
            show(context, context.getString(resId), duration)
        }

        fun show(context: Context, content: String, duration: Int = Toast.LENGTH_SHORT) {
            val handler = Handler(Looper.getMainLooper())
            handler.post({
                toast?.cancel()
                toast = Toast.makeText(context, content, duration)
                toast?.show()
            })
        }

        fun showTestToast(context : Context, resId :Int, duration: Int = Toast.LENGTH_SHORT){
            if (Utils.isDebuggable(context)) {
                show(context, resId, duration)
            }
        }

        fun showTestToast(context : Context, content: String, duration: Int = Toast.LENGTH_SHORT){
            if (Utils.isDebuggable(context)) {
                show(context, content, duration)
            }
        }

    }
}