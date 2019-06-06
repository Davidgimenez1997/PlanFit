package com.utad.david.planfit;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class PlanFitApplication extends MultiDexApplication{

    private static Context context;

    /******************************** DAMOS VALOR A CONTEXT *************************************+/
     *
     */

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
