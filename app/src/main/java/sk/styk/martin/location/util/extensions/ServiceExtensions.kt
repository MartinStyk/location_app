package sk.styk.martin.location.util.extensions

import android.app.ActivityManager
import android.app.Service
import android.content.Context

fun Service.isRunningInForeground(): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (javaClass.name.equals(service.service.className) && service.foreground) {
            return true
        }
    }
    return false
}