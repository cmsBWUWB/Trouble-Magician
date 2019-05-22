package com.hfut.base.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.hfut.utils.utils.ToastUtils

/**
 * Created by wzt on 2019/5/22
 *
 */
abstract class BaseFragment : Fragment() {
    abstract fun getLayout():Int
    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayout(), container, false)
        ButterKnife.bind(this,view)
        return view?.let { it }
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
        context?.let { ToastUtils.show(it, message, duration) }
    }

    fun showProgressDialog(messageId : Int) {
        showProgressDialog(getString(messageId))
    }

    fun showProgressDialog(message : String) {
        context?.let {
            val progressBar = ProgressBar(context)
            dialog = AlertDialog.Builder(it)
                    .setView(progressBar)
                    .setMessage(message)
                    .create()
            dialog?.show()
        }
    }

    fun dismissProgressDialog(){
        dialog?.dismiss()
    }
}