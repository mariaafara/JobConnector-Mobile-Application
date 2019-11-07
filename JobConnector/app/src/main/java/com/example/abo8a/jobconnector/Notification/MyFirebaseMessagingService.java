package com.example.abo8a.jobconnector.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.abo8a.jobconnector.Company.CompanyAppliedEmployeeActivity;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;
import com.example.abo8a.jobconnector.searchedprofiles.ActivityEmployeeViewprofile;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        System.out.println("++++++++++++++++++++++++\n+++++ I have got a notification\n++++++++++++++"+remoteMessage.toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("text");
            String username = remoteMessage.getData().get("username");
            String uid = remoteMessage.getData().get("uid");
            String fcmToken = remoteMessage.getData().get("fcm_token");


            // Don't show notification if chat activity is open.
            if (!FirebaseChatMainApp.isChatActivityOpen()) {
                sendNotification(title,
                        message,
                        username,
                        uid,
                        fcmToken);
            } else {
                EventBus.getDefault().post(new PushNotificationEvent(title,
                        message,
                        username,
                        uid,
                        fcmToken));
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        System .out.println("++++gghhgghh+++++++++++ "+message);

        Intent intent;
        String content=message;
        if(message.split(",").length>1) {
            intent = new Intent(this, CompanyAppliedEmployeeActivity.class);
            intent.putExtra("pid",Integer.parseInt(message.split(",")[1]));
            content=message.split(",")[0];
        }

        else
            if(message.split("/").length>1)
            {
                intent = new Intent(this, ActivityEmployeeViewprofile.class);
                intent.putExtra("eid",Integer.parseInt(message.split("/")[1]));
                intent.putExtra("email",message.split("/")[2]);
                intent.putExtra("request",message.split("/")[3]);
                intent.putExtra("comeFromNotification",true);
                content=message.split("/")[0];

            }
            else
        intent= new Intent(this,OnBoardingActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))

                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}