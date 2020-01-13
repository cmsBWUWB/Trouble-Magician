package com.hfut.utils.utils.log

import com.socks.library.KLog


/**
 * Created by wzt on 2020/1/13
 *
 */
class LogPrint {
    companion object{
        @JvmStatic
        fun init(isShowLog: Boolean) {
            KLog.init(isShowLog)
        }

        @JvmStatic
        fun v(msg : String?) {
            KLog.v(msg as Any)
        }

        @JvmStatic
        fun v(tag: String, msg: String?) {
            KLog.v(tag, msg)
        }

        @JvmStatic
        fun i(msg : String?) {
            KLog.i(msg as Any)
        }

        @JvmStatic
        fun i(tag: String, msg: String?) {
            KLog.i(tag, msg)
        }

        @JvmStatic
        fun d(msg : String) {
            KLog.d(msg as Any)
        }

        @JvmStatic
        fun d(tag: String, msg: String?) {
            KLog.d(tag, msg)
        }

        @JvmStatic
        fun e(msg : String) {
            KLog.e(msg as Any)
        }

        @JvmStatic
        fun e(tag: String, msg: String?) {
            KLog.e(tag, msg)
        }

        @JvmStatic
        fun json(msg: String?) {
            KLog.json(msg)
        }

        @JvmStatic
        fun json(tag: String, msg: String?) {
            KLog.json(tag, msg)
        }
    }
}