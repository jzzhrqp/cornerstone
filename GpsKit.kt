package com.cornerstone

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.app.PendingIntent.CanceledException
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat.startActivityForResult


/**
 * Created by iron on 18-1-3.
 *
 *
 */

class GpsKit {
//    /*在AndroidManifest.xml文件里需要添加的权限：*/
//    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
//    <uses-permission android:name="android.permission.INTERNET" />

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    fun isOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network

    }

    /**
     * 强制帮用户打开GPS 5.0前可用
     * @param context
     */
    public fun openGPS(context: Context) {
        val GPSIntent = Intent()
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider")
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
        } catch (e: CanceledException) {
            e.printStackTrace()
        }

    }

    //打开位置信息设置页面让用户自己设置
    public fun openGPS2(activity: Activity,requestCode:Int) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(activity,intent, requestCode,null)
    }
}