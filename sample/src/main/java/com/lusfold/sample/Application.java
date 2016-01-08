package com.lusfold.sample;

import com.lusfold.androidkeyvaluestore.KVStore;


public class Application extends android.app.Application {
    private static final String TAG = "Application";
    @Override
    public void onCreate() {
        super.onCreate();
        KVStore.init(this,TAG);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        KVStore.destroy();
    }
}
