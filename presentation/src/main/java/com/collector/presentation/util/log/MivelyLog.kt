package com.collector.presentation.util.log

import timber.log.Timber

object collectorLog {

    const val LOG_TAG = "collectorLog"

    @JvmStatic fun d(message: String, vararg args: Any?) {
        Timber.tag(LOG_TAG)
        Timber.d(message, *args)
    }
}