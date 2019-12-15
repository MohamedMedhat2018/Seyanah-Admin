package com.beyond_tech.seyanah_admin.config;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Locale;


 public class AppConfig extends Application {


    private Locale locale = null;
    private static AppConfig mInstance;

    public static synchronized AppConfig getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));




    }

    @Override
    public void onCreate() {
        super.onCreate();
//        SugarContext.init(this);
//        Fabric.with(this, new Crashlytics());
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getApplicationContext().getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        mInstance = this;


        //method 2
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//        Configuration config = getBaseContext().getResources().getConfiguration();
//        String lang = settings.getString(getString(R.string.pref_locale), "");
//        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
//            locale = new Locale(lang);
//            Locale.setDefault(locale);
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        }


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        SugarContext.terminate();
    }

//    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
//        ConnectivityReceiver.connectivityReceiverListener = listener;
//    }


    //contin. method 2
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (locale != null) {
//            newConfig.locale = locale;
//            Locale.getDefault().setDefault(locale);
//            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
//        }
    }


}