package com.cornerstone;

import android.app.Application;

import com.smartcontrol.net.Http;

/**
 * Created by Administrator on 2018/3/21.
 */

public class Cornerstone {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        Cornerstone.application = application;
        Http.setApplication(application);
    }
}
