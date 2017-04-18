package rmensaje.telcel.com.rmensaje;

/**
 * Created by PIN7025 on 12/04/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    Bitmap bitmapIcon;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
        String message = remoteMessage.getData().get("message");
        //imageUri will contain URL of the image to be displayed with Notification
        String imageUri = remoteMessage.getData().get("image");
        String imageIcon = remoteMessage.getData().get("imageIcon");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");
        String paginaParaDireccionar = remoteMessage.getData().get("otrapagina");
        String ticker = remoteMessage.getData().get("ticker");
        String tituloNotificacion = remoteMessage.getData().get("tituloNotificacion");
        String tituloInterior = remoteMessage.getData().get("tituloInterior");
        String sumario = remoteMessage.getData().get("sumario");


        //To get a Bitmap image from the URL received
        bitmap = getBitmapfromUrl(imageUri);
        bitmapIcon = getBitmapfromUrl(imageIcon);
        sendNotification(message, bitmap, TrueOrFlase,paginaParaDireccionar,bitmapIcon,ticker,tituloNotificacion,sumario,tituloInterior);

    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, Bitmap image, String TrueOrFalse,String paginaParaDireccionar,Bitmap imageIcon,String ticker,String tituloNotificacion,String sumario,String tituloInterior) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        intent.putExtra("paginaParaDireccionar",paginaParaDireccionar);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
               // .addAction(R.mipmap.ic_launcher, "BUTTON 1", pendingIntent) // #0
               // .addAction(R.mipmap.ic_launcher, "BUTTON 2", pendingIntent)  // #1
               // .addAction(R.mipmap.ic_launcher, "BUTTON 3", pendingIntent)     // #2
                .setLargeIcon(image)/*Notification icon image*/
                .setSmallIcon(R.drawable.ic_ondemand)
                //.setContentTitle(messageBody)
                .setContentTitle(tituloNotificacion)
                .setContentText(messageBody)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setLargeIcon(imageIcon)
                .setTicker(ticker)
                .setStyle(new NotificationCompat.BigPictureStyle().setBigContentTitle(tituloInterior)
                        .bigPicture(image).setSummaryText(sumario))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setShowWhen(true)
              .setContentIntent(pendingIntent);

        notificationBuilder.setVibrate(new long[] { 100, 200, 100, 500 });
        // API 11 o mayor
        //    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);
        notificationBuilder.setLights(Color.YELLOW, 300, 100);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}