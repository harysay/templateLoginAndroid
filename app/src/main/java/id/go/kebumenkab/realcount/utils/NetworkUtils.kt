package id.go.kebumenkab.realcount.utils

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

private var connectivityManager: ConnectivityManager? = null
private var networkInfo: NetworkInfo? = null

fun isNetworkAvailable(context: Context): Boolean {
    return try {
        connectivityManager = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            networkInfo = connectivityManager!!.activeNetworkInfo
        }
        networkInfo != null && networkInfo!!.isConnected
    } catch (e: NullPointerException) {
        false
    }
}









