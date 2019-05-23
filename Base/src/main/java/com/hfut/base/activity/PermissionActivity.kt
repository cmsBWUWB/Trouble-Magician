package com.hfut.base.activity


import android.Manifest
import android.os.Bundle
import com.hfut.base.R
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

/**
 * Created by wzt on 2019/5/23
 *
 */
abstract class PermissionActivity : BaseActivity() {
    abstract fun onPermissionAccept()
    private var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable =RxPermissions(this)
                .request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        onPermissionAccept()
                    } else {
                        //没有权限就别用了吧
                        showToast(R.string.permission_denied)
                        finish()
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}