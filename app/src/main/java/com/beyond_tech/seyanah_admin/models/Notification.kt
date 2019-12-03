package com.beyond_tech.seyanah_admin.models

class Notification() {

//
//    lateinit var id: String
//    lateinit var iamgeUrl: String
//    lateinit var title: String
//    lateinit var body: String

    var customerId: String? = null
    var date: String? = null
    var freelancerId: String? = null
    //    private var requestId: String? = null
//    var firebaseUserId = ""
    var requestId = ""
    var notiType = ""
    var title = ""
    var message = ""
    var shown: Boolean = true

    init {

    }

    constructor(
        customerId: String?,
        freelancerId: String?,
        orderId: String,
        title: String,
        message: String,
        shown: Boolean
    ) : this() {
        this.customerId = customerId!!
        this.freelancerId = freelancerId!!
        this.requestId = orderId
        this.title = title
        this.message = message
        this.shown = shown
    }


}