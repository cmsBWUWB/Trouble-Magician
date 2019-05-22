package com.hfut.base.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.hfut.utils.utils.ToastUtils

/**
 * Created by wzt on 2019/5/16
 *
 */
abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayout():Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = getLayout()
        if (layoutId > 0) {
            setContentView(layoutId)
        }
        ButterKnife.bind(this)
    }

    fun showToast(resId: Int) {
        showToast(resId, Toast.LENGTH_SHORT)
    }

    fun showToast(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

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