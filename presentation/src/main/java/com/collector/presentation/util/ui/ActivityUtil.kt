package com.collector.presentation.util.ui

import android.app.Activity
import android.content.Intent
import com.collector.presentation.view.search.SearchActivity

object ActivityUtil {
    fun startSearchActivity(activity: Activity) {
        Intent(activity, SearchActivity::class.java).run {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(this)
        }
    }
}