package com.beyond_tech.seyanah_admin.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.beyond_tech.seyanah_admin.R;
import com.beyond_tech.seyanah_admin.activities.main.HomeActivity;
import com.beyond_tech.seyanah_admin.config.AppConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getTo());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getData());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getMessageType());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getSentTime());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getMessageId());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getTtl());
//        Log.e(TAG, "onMessageReceived: " + remoteMessage.getTtl());
//        Log.e(TAG, "onMessageReceived: " + remoteMessage.getTtl());

//        if(getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).contains(Constants.INSTANCE_ID)){
//        if (Prefs.contains(Constants.INSTANCE_ID)) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData());
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " +
                    remoteMessage.getNotification().getBody() + "...." + remoteMessage.getNotification().getTitle());
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        }


    }

    public void sendNotification(Map<String, String> data) {
        String title = null, body = null;
        String orderId = null, detailsType = "";
        if (data != null) {
//            body = data.get("body");
//            title = data.get("title");
//            orderId = data.get("serviceId");
//            orderId = data.get(Constants.TYPE);
//            detailsType = data.get(Constants.NOTIFI_DETAILS_TYPE);
        }
        Intent intent = null;
//        switch (detailsType) {
//            case Constants.FREELANCER_POST_DETAILS:
//                //        intent = new Intent(getApplicationContext(), CustomerPostDetails.class);
//                intent = new Intent(AppConfig.getInstance(), FreelancerPostDetails.class);
//
//
//                break;
//            case Constants.CUSTOMER_POST_DETAILS:
//                //        intent = new Intent(getApplicationContext(), CustomerPostDetails.class);
//                intent = new Intent(AppConfig.getInstance(), CustomerPostDetails.class);
//
//                break;
//            case Constants.CUSTOMER_ORDER_DETAILS:
//                //        intent = new Intent(getApplicationContext(), CustomerPostDetails.class);
//                intent = new Intent(AppConfig.getInstance(), CustomerOrderDetails.class);
//
//                break;
//            default:
//                break;
//        }


//        intent.putExtra(Constants.ORDER_ID_NOTIFI_BAR, orderId);
//        intent.putExtra(Constants.NOTIFI_DETAILS_TYPE, detailsType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        intent.putExtra();
//        intent.putExtra();
//        intent.putExtra();
//


        //Assign BigText style notification
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(body);
//        bigText.setSummaryText(getResources().getString(R.string.app_name_root));
        bigText.setSummaryText(AppConfig.getInstance().getResources().getString(R.string.app_name_root));

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppConfig.getInstance(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = AppConfig.getInstance().getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(AppConfig.getInstance(), channelId)
//                        .setSmallIcon(R.drawable.noti_icon)
//                        .setSmallIcon(R.drawable.seyanah_logo)
//                        .setSmallIcon(R.drawable.seyanaicon2)
                        .setSmallIcon(R.drawable.iconseyana)
                        .setContentTitle(title)
                        .setContentText(body)
//                        .setLargeIcon(BitmapFactory.decodeResource(AppConfig.getInstance().getResources(), R.drawable.noti_icon))
                        .setAutoCancel(true)
//                        .setSmallIcon(R.drawable.noti_icon)
                        .setSound(defaultSoundUri)
                        .setOngoing(false)
                        .setTicker(title)
                        .setStyle(bigText)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) AppConfig.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public void sendNotification(String messageBody, String title) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.seyana_logo5)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed_token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");

    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {


    }

}