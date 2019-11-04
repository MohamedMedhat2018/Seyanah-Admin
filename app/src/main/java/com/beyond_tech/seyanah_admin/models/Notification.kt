package com.beyond_tech.seyanah_admin.models

class Notification() {

//
//    lateinit var id: String
//    lateinit var iamgeUrl: String
//    lateinit var title: String
//    lateinit var body: String


     var firebaseUserId = ""
     var orderId = ""
     var notiType = ""
     var title = ""
     var message = ""
    var shown: Boolean = true


    constructor(
        firebaseUserId: String?,
        orderId: String,
        title: String,
        message: String,
        shown: Boolean
    ) : this() {
        this.firebaseUserId = firebaseUserId!!
        this.orderId = orderId
        this.title = title
        this.message = message
        this.shown = shown
    }


}