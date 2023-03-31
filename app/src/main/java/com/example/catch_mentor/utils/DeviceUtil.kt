package com.example.catch_mentor.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.util.Log

object DeviceUtil {
    var deviceDpWidth:Int?=null
    private set

    fun getDeviceDpWidth(activity:Activity):Int{

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            val den=outMetrics.densityDpi
            val x=outMetrics.widthPixels/(den/160)
            val y=outMetrics.heightPixels/(den/160)
            Log.d("checkfor","1 : $x $y $den")
            deviceDpWidth=x
            return x.toInt()
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
            val size=Point()
            display.getSize(size)
            val den=activity.getResources().getDisplayMetrics().density
            val x=size.x/den
            val y=size.y/den
            Log.d("checkfor","2 : $x $y")
            deviceDpWidth=x.toInt()
            return x.toInt()
        }
        val windowManager=activity.getWindowManager()
    }

    fun getDevicePxWidth(activity:Activity):Int{

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            val den=outMetrics.densityDpi
            val x=outMetrics.widthPixels
            val y=outMetrics.heightPixels
            Log.d("checkfor","1 : $x $y $den")
            return x.toInt()
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
            val size=Point()
            display.getSize(size)
            val den=activity.getResources().getDisplayMetrics().density
            val x=size.x/den
            val y=size.y/den
            Log.d("checkfor","2 : $x $y")
            return x.toInt()
        }

        val windowManager=activity.getWindowManager()
    }

    fun getDevicePxHeight(activity:Activity):Int{

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            val den=outMetrics.densityDpi
            val x=outMetrics.widthPixels
            val y=outMetrics.heightPixels
            Log.d("checkfor","1 : $x $y $den")
            return y.toInt()
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
            val size=Point()
            display.getSize(size)
            val den=activity.getResources().getDisplayMetrics().density
            val x=size.x/den
            val y=size.y/den
            Log.d("checkfor","2 : $x $y")
            return y.toInt()
        }

    }

    fun getDeviceDpHeight(activity:Activity):Int{

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            val den=outMetrics.densityDpi
            val x=outMetrics.widthPixels/(den/160)
            val y=outMetrics.heightPixels/(den/160)
            Log.d("checkfor","1 : $x $y $den")
            deviceDpWidth=x
            return y.toInt()
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
            val size=Point()
            display.getSize(size)
            val den=activity.getResources().getDisplayMetrics().density
            val x=size.x/den
            val y=size.y/den
            Log.d("checkfor","2 : $x $y")
            deviceDpWidth=x.toInt()
            return y.toInt()
        }
        val windowManager=activity.getWindowManager()
    }

    fun Int.dpToPx(context: Context):Float{

            val density = context.getResources().getDisplayMetrics().density
            return Math.round( this * density).toFloat()
    }
}