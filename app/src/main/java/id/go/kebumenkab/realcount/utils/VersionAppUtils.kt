package id.go.kebumenkab.realcount.utils

import android.annotation.SuppressLint
import android.widget.TextView
//import id.go.kebumenkab.epresence.BuildConfig

@SuppressLint("SetTextI18n")
fun versionApp(textView: TextView) {
    val versionName = "20240328.1"//BuildConfig.VERSION_NAME
    textView.text = "ver.$versionName"
}