package com.wineadvocate.application

import android.app.Application
import com.wineadvocate.bradixapp.BuildConfig
import timber.log.Timber

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

class BradixApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}