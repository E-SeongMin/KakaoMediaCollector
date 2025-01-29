package com.collector.presentation.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber Init
        Timber.plant(Timber.DebugTree())
    }

    init {
        instance = this
    }

    companion object {

        private var instance: MainApplication? = null

        @JvmStatic
        fun applicationContext(): Context = instance!!.applicationContext

        @JvmStatic
        fun application(): Application = instance!!

        fun resource(): Resources = instance!!.applicationContext.resources
    }
}