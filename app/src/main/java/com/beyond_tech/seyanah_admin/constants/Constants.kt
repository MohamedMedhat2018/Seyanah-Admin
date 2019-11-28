package com.beyond_tech.seyanah_admin.constants

class Constants {
    companion object {

        @JvmField
        val USER_TYPE_FREE = "FREE"


        val CUSTOMER_ID: String = "customerId"
        val OFFER: String = "Offer"
        val KEYWORD: String = "keyword"
        val KEY_EMAIL_ADDRESS: String = "email"
        val KEY_PHONE_NUMBER: String = "phoneNumber"
        val KEY_PASSWORD: String = "password"


        val USER_ADMIN: String? = "USER_ADMIN"
        val LOGGED_BEFORE: String? = "LOGGED_BEFORE"
        var ADMINS: String = "Admins"
        //        var REG_USERNAMES: String = "userName"
        var REG_USERNAME: String = "username"
        var REG_EMAIL: String = "email"


        val SHOWN_NOTI = "shown"
        val CATEGORY_NAME = "categoryName"
        val REQUESTS = "Requests"
        val NOTIFICATION_CUSTOMER = "NotificationCustomer"
        val NOTIFICATION_FREELANCER = "NotificationFreelancer"
        val FIREBASEUSERID = "firebaseUserId"
        val MESSAGE = "message"
        val ORDERID = "orderId"
        val SHOWN = "NotificationFreelancer"
        val TITLE = "title"


        val ID_REQUEST_ORDER = 0
        val ID_MY_ORDERS = 1
        val ID_NOTIFICATION = 2
        val ID_MORE = 3
        val ID_ACCOUNT = 4



        val USER_TYPE_PREMIUM = "PREMIUM"
        val NETWORK_ERROR = "Network connection error"


        val SELECTED_LANG = "SELECTED_LANG"
        val LANG_ARABIC = "LANG_ARABIC"
        val LANG_ENGLISH = "LANG_ENGLISH"

        val USERS = "Users"
        val FREELANCERS = "Freelancers"
//    public static final String USERS = "USERS";

        val PHONE = "userPhoneNumber"
        val INVALID_PHONE = "Phone is invalid"
        val INVALID_PASS = "Password is invalid"
        val VERIFICATRION_ID = "VERIFICATRION_ID"
        val PHONE_NUMBER = "PHONE_NUMBER"
        val USER = "USER"

        val CATEGORY = "Category"
        val CATEGORY_ID = "categoryId"
        val CATEGORY_QUESTIONS = "CategoryQuestion"


        val PASSWORD = "userPassword"
        val FIREBASE_UID = "FIREBASE_UID"
        val NULL = "Null"

        val USER_PHOTO = "userPhoto"
        val EDIT_FIELD_TYPE = "EDIT_FIELD_TYPE"


        val EDIT_FULLNAME = "EDIT_FULLNAME "
        val EDIT_EMAIL = "EDIT_EMAIL"
        val EDIT_PHONE_NUMBER = "EDIT_PHONE_NUMBER"
        val EDIT_PASSWORD = "EDIT_PASSWORD"
        val TABLE_NAME = "Notifications_table"
        val DATABASE_NAME = "Notification_DB"
        val NOTIIFI_ITEM = "NOTIIFI_ITEM"


        val STATUS_NOT_SURE = "STATUS_NOT_SURE"
        val STATUS_ACCEPTED = "STATUS_ACCEPTED"
        val STATUS_NOT_ACCEPTED = "STATUS_NOT_ACCEPTED"

        val IS_DATE_IS_ACCEPTED = "isDateApplicableOrNot"


        val WORKER_TYPE_CM = "CM"
        val WORKER_TYPE_FREELANCER = "FREELANCER"


        val ERROR_OCCURRES = "Netwrok Error !!!!"
        val REG_PHONES = "RegPhone"
        val PHOTOS_UPLOADED_SIZE = "PHOTOS_UPLOADED_SIZE"
        val KEY_VIEW_HOLDER = "KEY_VIEW_HOLDER "
        val KEY_CAT = "KEY_CAT"
        val GONE = "GONE "
        val VISIBLE = "VISIBLE"
        val POS_SELECTED = "POS_SELECTED"
        val ORDER = "ORDER"


        val APP_FIREBASE_DATABASE_REF = "HomeServices"
        val RQUEST_PENDING = "PendingRequests"


        val WORKER_OR_CUSTOMER = "WORKER_OR_CUSTOMER"
        val CUSTOMERS = "CUSTOMERS"

        val WORKERS = "Workers"

        val STATE_CODE_SENT = 2
        val STATE_VERIFY_FAILED = 3
        val STATE_VERIFY_SUCCESS = 4
        val STATE_SIGNIN_FAILED = 5
        val STATE_SIGNIN_SUCCESS = 6
        val STATE_INITIALIZED = 1


        val PHONE_FROM_USER_MODEL = "phoneNumber"//must
        val PHONE_MATERIALS = "PHONE_MATERIALS"
        val PHONE_EXIST = "Phone already exist !!!!"

        val WORKER_CM = "WORKER_CM"
        val WORKER_TYPE_NORMAL = "WORKER_TYPE_NORMAL"
        val WORKER = "WORKER"

        val CM = "CM"
        val FREELANCER = "FREELANCER"


        val PENDING = "PENDING"
        val CM_WORKING = "CM_WORKING"
        val CM_FINISHED = "CM_FINISHED"
        val POST = "POST"
        val FREELANCE_WORKING = "FREELANCER_WORKING"
        val FREELANCE_FINISHED = "FREELANCE_FINISHED"
        val REJECTED_FROM_CUSTOMER = "REJECTED_FROM_CUSTOMER"
        val REJECTED_FROM_COMPANY = "REJECTED_FROM_COMPANY"

        val WORKER_LOGGED_AS = "WORKER_LOGGED_AS"

        val RESULT_FROM_LOGIN_TO_FRAGMENT_TO_REQUEST_ORDER = 203
        val ORDER_STATE = "state"
        val TYPE = "type"
        val LOCATIONS = "Locations"
        val CITIES = "Cities"
        val COUNTRIES = "Countries"
        val COMMENTS = "Comments"
        val SELECT_COUNTRY = "Select country"
        val SELECT_CITY = "Select city"


        val NOTIFI_FREELANCER = "NotificationFreelancer"
        val NOTIFI_CM = "NotificationCM"
        val NOTIFI_CUSTOMER = "NotificationCustomer"
        val CM_TASK = "CM_TASK"
        val ORDER_REQUEST = "ORDER_REQUEST"
        val CM_FOR_USER = "CM_FOR_USER"
        val REQUEST_ID = "requestId"
        val ACCEPTED_COMMENT = "selected"
        val EXIST = "EXIST"
        val RATINGS = "Ratings"
        val ORDER_ID = "orderId"


        val ORDER_OR_POST = "ORDER_OR_POST"
        val RATE = "rate"
        val CAT_NAME = "categoryName"
        val USER_PHOTO_URI = "USER_PHOTO_URI"
        val FREE_CUSTOMER_CONNECTION = "FreeCustomerConnection"
        val FREE_LANCER_ID = "freelancerId"
        val WORKER_PHOTO: Any = "workerPhoto"
        val WORKING_OR_FINISHED = "WORKING_OR_FINISHED"

        val LOCALE_ARABIC: CharSequence = "ar_EG"
        val LOCALE_ENGLISH: CharSequence = "en_US"
        val EDIT_NAME_IN_EN = "EDIT_NAME_IN_EN"
        val EDIT_NAME_IN_AR = "EDIT_NAME_IN_AR"
        val COMMENT = "COMMENT"
        val FREE_PHOTO_URL = "FREE_PHOTO_URL"
        val CM_WORKER = "CM_WORKER"
        val CP_NOTIFICATIONS = "CPNotification"
        val FORGOT_PASS = "FORGOT_PASS"
        val FLAG_RATE = "sos"
        val ORDER_ID_NOTIFI_BAR = "ORDER_ID_NOTIFI_BAR"
        val NO_CURRENT_USER = "NO_CURRENT_USER"
        val MESSAGE_TOKEN = "messageToken"
        val INSTANCE_ID = "INSTANCE_ID"
        val LOGIN_PHONE = "LOGIN_PHONE"
        val LOGIN = "login"
        val LOGGED_FROM_ANOTHER_DEVICE = "LOGGED_FROM_ANOTHER_DEVICE"
        val POS_TO_TAB = "POS_TO_TAB"
        val PASSWORD_KEY = "PASSWORD_KEY"


        var REQUEST_CODE_Login_TO_RequestOrder = 22
        var RESULT_CODE_Login_To_RequestOrder = 55


        var CM_TASKS = "CmTasks"

//        val LOGGED_FROM_ANOTHER_DEVICE = "LOGGED_FROM_ANOTHER_DEVICE"
//        val LOGGED_FROM_ANOTHER_DEVICE = "LOGGED_FROM_ANOTHER_DEVICE"
//        val LOGGED_FROM_ANOTHER_DEVICE = "LOGGED_FROM_ANOTHER_DEVICE"


    }
}