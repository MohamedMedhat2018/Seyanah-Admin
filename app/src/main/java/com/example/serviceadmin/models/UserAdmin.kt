package com.example.serviceadmin.models

class UserAdmin {

    var id: String? = null
    var username: String? = null
    var email: String? = null
    var password: String? = null
    var phoneNumber: String? = null
    var profilePhoto: String? = null

    constructor(username: String, password: String) {
        this.username = username
        this.password = password

    }

    constructor(id: String, username: String, email: String, password: String,
                phoneNumber: String) {
        this.id = id
        this.username = username
        this.email = email
        this.password = password
        this.phoneNumber = phoneNumber
    }


    constructor()
    constructor(
        id: String?,
        username: String?,
        email: String?,
        password: String?,
        phoneNumber: String?,
        profilePhoto: String?
    ) {
        this.id = id
        this.username = username
        this.email = email
        this.password = password
        this.phoneNumber = phoneNumber
        this.profilePhoto = profilePhoto
    }

}