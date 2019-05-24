package com.hfut.utils.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wzt on 2019/5/23
 *
 */
class FileUtils {
    companion object {
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

        @JvmStatic
        fun isExist(file: File?): Boolean {
            return file != null && file.exists()
        }

        @JvmStatic
        fun getFilePathByUri(context: Context, uri: Uri) : String?{
            var path : String? = null
            if (uri.scheme == ContentResolver.SCHEME_FILE) {
                path = uri.path
            }else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    if (isExternalStorageDocument(uri)) {
                        val docId: String = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":")
                        val type = split[0]
                        if ("primary".equals(type, true)) {
                            path = "${Environment.getExternalStorageDirectory()}/$split[1]"
                        }
                    }else if (isDownloadsDocument(uri)) {
                        val docId: String = DocumentsContract.getDocumentId(uri)
                        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                        path = getDataColumn(context,contentUri,null,null)
                    }else if (isMediaDocument(uri)) {
                        val docId: String = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":")
                        val type = split[0]
                        var contentUri: Uri? = null
                        if ("image" == type) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video" == type) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio" == type) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        val selection = "_id=?"
                        val selectionArgs = Array(1){split[1]}
                        contentUri?.let { path = getDataColumn(context, it, selection, selectionArgs) }
                    }
                }
            }
            return path
        }

        @JvmStatic
        fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
            val projection = Array(1) { MediaStore.Images.Media.DATA }
            val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val path = cursor.getString(columnIndex)
                    Log.i("wzt", "path = $path")
                    cursor.close()
                    return path
                }
            }
            return null
        }

        private fun isExternalStorageDocument(uri : Uri) : Boolean{
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri : Uri) : Boolean{
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri : Uri) : Boolean{
            return "com.android.providers.media.documents" == uri.authority
        }
    }

}