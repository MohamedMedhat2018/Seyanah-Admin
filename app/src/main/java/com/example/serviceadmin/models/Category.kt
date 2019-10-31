package com.example.serviceadmin.models

class Category {

    lateinit var categoryIcon: String
    lateinit var categoryName: String

    constructor(categoryIcon: String, categoryName: String) {

        this.categoryIcon = categoryIcon
        this.categoryName = categoryName
    }
    constructor()

}