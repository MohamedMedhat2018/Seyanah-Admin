package com.beyond_tech.seyanah_admin.fire_utils

import com.beyond_tech.seyanah_admin.constants.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RefBase {

    companion object {
        private fun root(): DatabaseReference {
            return FirebaseDatabase.getInstance().reference
        }

        fun customerNotification(): DatabaseReference {
            return root().child(Constants.NOTIFICATION_CUSTOMER)
        }

        fun freelancerNotification(): DatabaseReference {
            return root().child(Constants.NOTIFICATION_FREELANCER)
        }

        fun requests(id: String): DatabaseReference {
            return root().child(Constants.REQUESTS).child(id)
        }

        fun category(id: String): DatabaseReference {
            return Companion.root().child(Constants.CATEGORY).child(id)
        }


        fun refAdmins(id: String): DatabaseReference {
            return root().child(Constants.ADMINS).child(id)
        }


        fun cpNotification(): DatabaseReference {
            return root().child(Constants.CP_NOTIFICATIONS)

        }

        //for the requests
        fun refRequest(reqId: String): DatabaseReference {
            return root()
                .child(Constants.REQUESTS)
                .child(reqId)

        }

        fun refUser(userId: String): DatabaseReference {
            return root()
                .child(Constants.USERS)
                .child(userId)

        }
    }
}