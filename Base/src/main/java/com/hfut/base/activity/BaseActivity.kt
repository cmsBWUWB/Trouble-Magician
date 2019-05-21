package com.hfut.base.activity

import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hfut.utils.utils.ToastUtils

/**
 * Created by wzt on 2019/5/16
 *
 */
open class BaseActivity : AppCompatActivity() {
    fun showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        showToast(getString(resId), duration)
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        ToastUtils.show(this, message, duration)
    }
}