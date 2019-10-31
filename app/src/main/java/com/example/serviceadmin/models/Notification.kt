package com.example.serviceadmin.models

class Notification {

//
//    lateinit var id: String
//    lateinit var iamgeUrl: String
//    lateinit var title: String
//    lateinit var body: String


    var firebaseUserId: String
    var orderId: String
    var title: String
    var message: String
    var shown: Boolean


    constructor(
        firebaseUserId: String?,
        orderId: String,
        title: String,
        message: String,
        shown: Boolean
    ) {
        this.firebaseUserId = firebaseUserId!!
        this.orderId = orderId
        this.title = title
        this.message = message
        this.shown = shown
    }


}