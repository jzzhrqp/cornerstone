package com.cornerstone.kit

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.cornerstone.log.L
import edu.cmu.pocketsphinx.Decoder
import java.io.File


/**
 * Created by Administrator on 2018/3/28.
 * edu.cmu.pocketsphinx 语音识别工具
 */
class DecoderKit constructor(val context: Context,val decoderListener: DecoderListener) {

    private lateinit var decoder:Decoder
   val KWS_SEARCH = "wakeup"
   val SLM_SEARCH = "slm"
   var started=false
   var handler= Handler(Looper.getMainLooper())

   interface DecoderListener{
      fun generalMsg(msg:String)
      fun initError(msg:String)
      fun initReady(msg:String);
      fun processError(s: String)
   }

   init {
      System.loadLibrary("lvcsr_jni")
      initDecoder(context)
   }

   fun initDecoder(context: Context){

      try {
         var assets = Assets(context)
         var assetsDir = assets.syncAssets()

         var  config = Decoder.defaultConfig()
         config.setString("-hmm", File(assetsDir, "acmod.dat").path)
         config.setString("-dict", File(assetsDir, "lvcsr.dct").path)
         // config.setString("-rawlogdir", assetsDir.getPath());
         config.setFloat("-kws_threshold", 1e-30f.toDouble()) //1e-5f, 1e-50f 35f
         config.setBoolean("-allphone_ci", true)

         this.decoder = Decoder(config)

         var languageModel: File? = File(assetsDir, "lvcsr.bin")
         decoder.setKws(KWS_SEARCH, File(assetsDir, "kws.lst").path)
         if (languageModel!=null)
         decoder.setLmFile(SLM_SEARCH, languageModel?.path)
         assetsDir = null
         languageModel = null

      }catch (e:Exception){
         e.printStackTrace()
         decoderListener.initError("decoder init 失败:"+e.message)

      }
      decoderListener.initReady("可以开始进行识别")

   }

   fun setWakeupSearch(){
      decoder.search=KWS_SEARCH
   }

   fun setSlmSearch(){
      decoder.search=SLM_SEARCH
   }

   fun start(){
      if (!started) {
         started=true
         decoder.startUtt()
      }else{
         decoderListener.generalMsg("decoder早已经启动！")
      }
   }

   fun process(data:ShortArray,length:Long,noSearch:Boolean,fullUtt:Boolean){
     if (!started){
        decoderListener.processError("decoder还没有启动，不能接收数据！")
     }else {
        decoder.processRaw(data, length, noSearch, fullUtt)
     }
   }

   fun hyp():String{
      val hypothesis = decoder.hyp()
      if (null != hypothesis && !TextUtils.isEmpty(hypothesis.hypstr)){
       return  hypothesis.hypstr
      }else{
         return ""
      }
   }

   fun stop(){
      if(started){
         try{
            started=false
            decoder.endUtt()
         }catch (e:Exception){
            e.printStackTrace()
            L.logInLine("decoderkit stop 出错")
         }

      }
   }

}