package com.cornerstone.devicesKit;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public final class MacKit {
    private  String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public final String getAdresseMAC( Context context) {
        WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMan==null){
            return marshmallowMacAddress;
        }
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

            if(wifiInf != null &&marshmallowMacAddress.equals( wifiInf.getMacAddress())) {
                String result = (String)null;
                try {
                    result = this.getAdressMacByInterface();
                    if(result != null) {
                        return result;
                    }
                    result = this.getAddressMacByFile(wifiMan);
                    return result;
                } catch (IOException var6) {
                    Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
                } catch (Exception var7) {
                    Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
                }

                return this.marshmallowMacAddress;
            } else {
                return wifiInf != null && wifiInf.getMacAddress() != null?wifiInf.getMacAddress():"";
            }
    }

    private final String getAdressMacByInterface() {
        try {
            ArrayList all = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var3 = all.iterator();

            while(var3.hasNext()) {
                NetworkInterface nif = (NetworkInterface)var3.next();
                if("wlan0".equals(nif.getName())) {
                    byte[] var10000 = nif.getHardwareAddress();
                    if(var10000 == null) {
                        return "";
                    }

                    byte[] macBytes = var10000;
                    StringBuilder res1 = new StringBuilder();
                    int var8 = macBytes.length;

                    for(int var7 = 0; var7 < var8; ++var7) {
                        byte b = macBytes[var7];
                        String var10 = "%02X:";
                        Object[] var11 = new Object[]{Byte.valueOf(b)};
                        String var16 = String.format(var10, Arrays.copyOf(var11, var11.length));
                        String var13 = var16;
                        res1.append(var13);
                    }

                    CharSequence var15 = (CharSequence)res1;
                    if(var15.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }

                    return res1.toString();
                }
            }
        } catch (Exception var14) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }

        return null;
    }

    private final String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        int wifiState = wifiMan.getWifiState();
        wifiMan.setWifiEnabled(true);
        File fl = new File(this.fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        String ret = this.crunchifyGetStringFromStream((InputStream)fin);
        fin.close();
        boolean enabled = 3 == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private final String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if(crunchifyStream != null) {
            StringWriter crunchifyWriter = new StringWriter();
            char[] crunchifyBuffer = new char[2048];

            try {
                BufferedReader crunchifyReader = new BufferedReader((Reader)(new InputStreamReader(crunchifyStream, "UTF-8")));

                for(int counter = crunchifyReader.read(crunchifyBuffer); counter != -1; counter = crunchifyReader.read(crunchifyBuffer)) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }

            return crunchifyWriter.toString();
        } else {
            return "No AoshuoCloudContents";
        }
    }
}
