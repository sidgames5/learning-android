package io.github.sidgames5.learning_android.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    fun check(context:Context, permission:String):Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun grant(activity: Activity?, permission: String, requestCode:Int,  persistent:Boolean? = false):Boolean {
        ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
        return if (check(activity, permission)) {
            true
        } else {
            if (persistent == true) {
                grant(activity, permission, requestCode, persistent)
            } else {
                false
            }
        }
    }
}