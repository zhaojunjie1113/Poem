package com.charsunny.poem;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 * Created by zjj on 15/11/23.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
