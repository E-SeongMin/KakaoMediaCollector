package com.collector.data.log

import timber.log.Timber

object CustomLog {

    const val LOG_TAG = "test_min"

    @JvmStatic fun d(message: String, vararg args: Any?) {
        Timber.tag(LOG_TAG)
        Timber.d(message, *args)
    }
}