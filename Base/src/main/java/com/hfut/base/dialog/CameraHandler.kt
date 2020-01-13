package com.hfut.base.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.hfut.base.R
import com.hfut.base.model.CameraImageBean
import com.hfut.base.model.RequestCodes
import com.hfut.utils.utils.FileUtils
import com.hfut.utils.utils.log.LogPrint
import java.io.File

/**
 * Created by wzt on 2019/5/23
 *
 */
class CameraHandler(private val context: Context) : View.OnClickListener {
    private var fragment: Fragment? = null
    constructor(fragment: Fragment) : this(fragment.context!!){
        this.fragment = fragment
    }

    private val dialog = AlertDialog.Builder(context).create()

    fun beginChooseDialog() {
        dialog.show()
        val window = dialog.window
        window?.let {
            it.setContentView(R.layout.dialog_camera_panel)
            it.setGravity(Gravity.BOTTOM)
            it.setWindowAnimations(R.style.anim_panel_up_from_bottom)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //设置属性
            val params = it.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            //设置背景的暗度，0.0f是完全不暗，1.0f是背景全部变黑暗
            params.dimAmount = 0.5f
            it.attributes = params

            it.findViewById<View>(R.id.photodialog_btn_cancel).setOnClickListener(this)
            it.findViewById<View>(R.id.photodialog_btn_take).setOnClickListener(this)
            it.findViewById<View>(R.id.photodialog_btn_native).setOnClickListener(this)
        }
    }

    private fun getPhotoName(): String {
        return FileUtils.getFileNameByTime("IMG", "jpg")
    }

    private fun takePhoto() {
        val currentPhotoName = getPhotoName()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val tempFile = File(FileUtils.CAMERA_PHOTO_DIR, currentPhotoName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider获取文件的Uri
            LogPrint.i(tempFile.path)
            val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", tempFile)
            CameraImageBean.path = Uri.fromFile(tempFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        } else {
            val uri = Uri.fromFile(tempFile)
            CameraImageBean.path = Uri.fromFile(tempFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        if (fragment != null) {
            fragment?.startActivityForResult(intent, RequestCodes.TAKE_PHOTO)
        } else if (context is Activity) {
            context.startActivityForResult(intent, RequestCodes.TAKE_PHOTO)
        }
    }

    private fun pickPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (fragment != null) {
            fragment?.startActivityForResult(Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO)
        } else if (context is Activity) {
            context.startActivityForResult(Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO)
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.photodialog_btn_take) {
            takePhoto()
        } else if (id == R.id.photodialog_btn_native) {
            pickPhoto()
        }
        dialog.cancel()
    }
}