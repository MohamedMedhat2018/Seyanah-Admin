package com.beyond_tech.seyanah_admin.interfaces

import com.beyond_tech.seyanah_admin.models.Notification

//import android.app.Notification

interface OnNotificationClicked {
//    fun onNotificationClciked(notification: Notification)
    fun onNotificationClciked(notification: Notification, position: Int)
}