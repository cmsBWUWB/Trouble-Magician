package com.hfut.utils.utils

import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wzt on 2019/5/23
 *
 */
class FileUtils {
    companion object{
        //格式化的模板
        private const val TIME_FORMAT = "_yyyyMMdd_HHmmss"
        @JvmStatic
        //系统相机目录
        val CAMERA_PHOTO_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/Camera/"

        /**
         * @param timeFormatHeader 格式化的头(除去时间部分)
         * @param extension        后缀名
         * @return 返回时间格式化后的文件名
         */
        @JvmStatic
        fun getFileNameByTime(timeFormatHeader: String, extension: String): String {
            return "${getTimeFormatName(timeFormatHeader)}.$extension"
        }

        private fun getTimeFormatName(timeFormatHeader: String): String {
            val date = Date(System.currentTimeMillis())
            //必须要加上单引号
            val dateFormat = SimpleDateFormat("'$timeFormatHeader'$TIME_FORMAT", Locale.getDefault())
            return dateFormat.format(date)
        }
    }
}