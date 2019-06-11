package com.hfut.voice

import android.content.Context
import android.os.Bundle
import com.iflytek.cloud.*
import com.socks.library.KLog

/**
 * Created by wzt on 2019/6/4
 * 相关api文档http://mscdoc.xfyun.cn/android/api/
 */
object VoiceManager{
    const val TAG = "VoiceManager"
    var mRecognizer: SpeechRecognizer? = null

    fun init(context: Context){
        SpeechUtility.createUtility(context, "appid=${context.getString(R.string.app_key)}")
        mRecognizer = SpeechRecognizer.createRecognizer(context) { KLog.i("VoiceManager", "init code = $it") }
    }

    fun startListener() {
        mRecognizer?.let {
            //设置听写参数
            it.setParameter(SpeechConstant.DOMAIN, "iat")
            //设置为中文
            it.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
            //设置为普通话
            it.setParameter(SpeechConstant.ACCENT, "mandarin")
            //设置返回方式为纯文本，目前支持json,xml以及plain三种格式，其中plain为纯听写文本内容
            it.setParameter(SpeechConstant.RESULT_TYPE,"plain")

            it.startListening(object : RecognizerListener {
                /**
                 * 音量变化
                 * @param volume 当前音量值，范围[0-30]
                 * @param data 录音数据
                 */
                override fun onVolumeChanged(volume: Int, data: ByteArray?) {
                    KLog.i(TAG, "[onVolumeChanged]volume = $volume")
                }

                /**
                 * 返回结果 返回的结果可能为null，请增加判断处理
                 * @param result 结果数据
                 * @param islast 是否最后一次结果标记
                 */
                override fun onResult(result: RecognizerResult?, islast: Boolean) {
                    KLog.i(TAG, "[onResult]islast = $islast,str = ${result?.resultString}")
                }

                /**
                 * 调用开始录音函数后，会自动开启系统的录音 机，并在录音机开启后，会回调此函数
                 */
                override fun onBeginOfSpeech() {
                    KLog.i(TAG,"[onBeginOfSpeech]")
                }

                /**
                 * 事件扩展用接口，由具体业务进行约定
                 */
                override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) {
                    KLog.i(TAG,"[onEvent]")
                }

                /**
                 * 结束说话 在SDK检测到音频的静音端点时，回调此函数（在录音模式或写音频模式下都会回调， 应用层主动调用SpeechRecognizer.stopListening()则不会回调此函数， 在识别出错时，可能不会回调此函数）。
                 */
                override fun onEndOfSpeech() {
                    KLog.i(TAG,"[onEndOfSpeech]")
                }

                /**
                 * 错误回调 当此函数回调时，说明当次会话出现错误，会话自动结束，录音也会停止
                 */
                override fun onError(error: SpeechError?) {
                    KLog.i(TAG, "[onError]errorCode = ${error?.errorCode}")
                }
            })

        }
    }

    fun stopListener() {
        mRecognizer?.stopListening()
    }



}