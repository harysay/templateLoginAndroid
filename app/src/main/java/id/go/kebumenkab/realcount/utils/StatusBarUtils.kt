package id.go.kebumenkab.realcount.utils

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import id.go.kebumenkab.realcount.R

class StatusBarUtils {
    companion object {
        fun setStatusBarColor(activity: Activity, lightStatusBar: Boolean = true) {
            val window = activity.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val color = if (lightStatusBar) R.color.white else R.color.blackColor
                window.statusBarColor = ContextCompat.getColor(activity, color)
                window.decorView.systemUiVisibility = if (lightStatusBar) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
            } else {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.blackColor)
            }
        }
    }
}