package io.RentEasy.support;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * Created by arthonsystechnologies on 16/12/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mInstance = this;
//        FirebaseApp.initializeApp(mInstance);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

}
