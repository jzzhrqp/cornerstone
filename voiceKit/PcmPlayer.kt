package com.cornerstone.voiceKit

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.cornerstone.log.L

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by xianjie.zhang on 2016/10/14.
 * 播放pcm
 */

class PcmPlayer() {
    private var mAudioTrack: AudioTrack
     private var filePath = ""
    private var play = true
    private var rawInputStream: InputStream? = null

    constructor(filePath: String) : this() {
        this.filePath = filePath
    }

    constructor(rawInputStream: InputStream) : this() {
        this@PcmPlayer. rawInputStream = rawInputStream
    }

    init {
        val minBufSize = AudioTrack.getMinBufferSize(
                16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT)

        mAudioTrack = AudioTrack(AudioManager.STREAM_MUSIC,
                16000,
                AudioFormat.CHANNEL_OUT_MONO, //单声道
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize,
                AudioTrack.MODE_STREAM)
        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume())
        // mAudioTrack.setNotificationMarkerPosition(sample_count);
        mAudioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(track: AudioTrack) {}

            override fun onPeriodicNotification(track: AudioTrack) {

            }
        })
    }

    fun setFilePath(filePath: String) {
        this.filePath = filePath
    }

    fun start(onThread: Boolean) {
        if (onThread) {
            PlayThread().start()
        } else {
            work()
        }
    }

    internal inner class PlayThread : Thread() {
        override fun run() {
            work()
        }
    }

    private fun work() {
        var file: File? = null
        if (rawInputStream == null) {
            file = File(filePath)
            if (!file.exists()) {
                L.d("pcm filePath not exists")
                return
            }
        }
        mAudioTrack.play()
        var audiois: InputStream? = null
        var b: ByteArray? = null
        if (file != null) {
            b = ByteArray(file.length().toInt())
        } else
            b = ByteArray(10240)
        var red = 0
        try {
            if (rawInputStream == null)
                audiois = FileInputStream(file!!)
            else {
                audiois = rawInputStream
            }
            L.d("播放pcm...")
            while (play && red >= 0) {
                red = audiois?.read(b, 0, b.size)!!
                mAudioTrack.write(b, 0, red)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                audiois!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        mAudioTrack.stop()
    }
}
