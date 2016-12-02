package uniftec.bsocial;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import uniftec.bsocial.fragments.ProfileFragment;

public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);
        //Log.i("Mensagem: ", message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.mipmap.ic_profile);

        // This intent is fired when notification is clicked
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stacktips.com/"));

        Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        // builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle("bSocial");

        // Content text, which appears in smaller text below the title
        builder.setContentText(message);

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText("Clique aqui para mais informações.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(null, 1, builder.build());
    }
}
