package com.cornerstone.voiceKit

import android.content.Context
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioRecord
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by iron on 17-12-16.
 * pcm录音工具。
 * 需要权限：
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
 */
class PcmRecorder(var context: Context, var listener: RecordListener?){
    private val tag="PcmRecorder"
   
    private val voice_record_init_error="录音初始化失败,有可能录音被占用！"
    private val voice_record_start_recording_error="开启录音失败！"
    private var recorder: AudioRecord? = null
    private var sampleRateInHz=16000
    private var channelConfig=16
    private var audioSource=6
    private var audioFormat=ENCODING_PCM_16BIT
    private var bufferSizeInBytes=AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)*20
    private var handler:Handler = Handler(Looper.getMainLooper())
    private var inputLength=160
    private var atomicBoolean = AtomicBoolean(false)

    private var recordThread:RecordThread?=null

    public interface RecordListener{
        /**
         * 当录音出错的时候
         */
        fun onError(msg:String)

        /**
         * 当录音开始的时候
         */
        fun onRecordBeginning()
        /**
         * 非异步录音数据输出
         */
        fun onOutputData(data: ShortArray, nread: Int)

        /**
         * 当录音结束的时候
         */
        fun onRecordEnd()
    }

    public fun stop(){
        atomicBoolean.set(false)
        try {
            recordThread?.join()
        }catch (e:Exception){
            e.printStackTrace()
        }
        recordThread=null
    }

    public fun start(){
        stop()
        recordThread=RecordThread(context)
        recordThread?.start()
    }

    private inner class RecordThread(var context: Context) :Thread(){
        private val inputAudio = ShortArray(inputLength)

        override fun run() {
            super.run()
            atomicBoolean.set(true)
            recorder = AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes)
            var recorder=recorder!!
            if (recorder.state == 0) {
                postError(voice_record_init_error)
                recorder.release()
                return
            }
            try {
                recorder.startRecording()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                postError(voice_record_start_recording_error)
                return
            }
            if (recorder.recordingState == 1) {
                recorder.stop()
                postError(voice_record_start_recording_error)
                return
            }
            postBeginning()
            while (atomicBoolean.get()){
                val nread = recorder.read(inputAudio, 0, inputLength)

                if (-1 == nread) {
                    Log.e(tag,"initError reading audio buffe")
                    throw RuntimeException("initError reading audio buffer")
                }
                if(nread > 0) {
                    outputData(inputAudio.clone(),nread)
                }
            }
            recorder.stop()
            recorder.release()
            this@PcmRecorder.recorder=null
            postEnd()

        }

        private fun postEnd() {
            handler.post { listener?.onRecordEnd() }
        }

        private fun outputData(data: ShortArray, nread: Int) {
            listener?.onOutputData(data,nread)
        }

        private fun postBeginning() {
            handler.post { listener?.onRecordBeginning() }
        }

        private fun postError(string: String?) {
            handler.post { listener?.onError(string?:"") }
        }
    }

}