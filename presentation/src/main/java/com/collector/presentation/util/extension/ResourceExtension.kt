package com.collector.presentation.util.extension

import com.collector.presentation.di.MainApplication

inline val Int.toResString: String get() = MainApplication.resource().getString(this)
