package com.beyond_tech.seyanahadminapp.config

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs

class AppConfiguration : Application() {


    private var mInstance: AppConfiguration? = null

    @Synchronized
    fun getInstance(): AppConfiguration? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()


        mInstance = this


    }

}