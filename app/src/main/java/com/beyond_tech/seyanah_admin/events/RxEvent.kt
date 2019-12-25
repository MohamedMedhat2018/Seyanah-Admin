package com.beyond_tech.seyanah_admin.events

class RxEvent {
    data class EventUserDataUpdated(val updated: Boolean)
    data class EventShowMakeAllAsRead(val updated: Boolean)
}