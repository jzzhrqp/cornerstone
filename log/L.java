package com.cornerstone.log;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/3/21.
 */

public class L {
    private static boolean printLog=true;
    private static boolean printLogD=true;
    private static boolean pringLogInLine=true;
    private static String TAG="zxjDebug";

    public static void d(String string){
        if (printLog&&printLogD){
            Log.d(TAG,""+string);
        }
    }

    public static void logInLine(String string){
        if (printLog){
            if (pringLogInLine){
                Logger.d(TAG+":"+string);
            }else if (printLogD){
                Log.d(TAG,string);
            }
        }
    }

    public static boolean isPrintLog() {
        return printLog;
    }

    public static void setPrintLog(boolean printLog) {
        L.printLog = printLog;
    }

    public static boolean isPrintLogD() {
        return printLogD;
    }

    public static void setPrintLogD(boolean printLogD) {
        L.printLogD = printLogD;
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        L.TAG = TAG;
    }
}
