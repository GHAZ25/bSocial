package uniftec.bsocial;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import uniftec.bsocial.cache.MessageCache;
import uniftec.bsocial.cache.NotificationCache;
import uniftec.bsocial.entities.PushReturn;

public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            Gson gson = new Gson();
            String message = data.getString("message");

            PushReturn push = gson.fromJson(message, PushReturn.class);

            switch (push.getType()) {
                case "mensagem":
                    NotificationCache notificationCache = new NotificationCache(push.getNot().getType(), push.getNot().getId(), getApplicationContext());

                    switch (push.getNot().getType()) {
                        case "mensagem":
                            notificationCache.sendConfirm(push.getNot().getId(), push.getNot().getMessage());

                            MessageCache messageCache = new MessageCache(getApplicationContext());
                            messageCache.messageConfirm(push.getMes().getSentUserId(), push.getMes().getMessage(), push.getMes().getSentUserName());
                        break;
                        case "convite":
                            notificationCache.newInvite(push.getNot().getId(), push.getNot().getMessage(), push.getNot().getMessageId());
                        break;
                    }
                    break;
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(R.mipmap.ic_profile);

            Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentIntent(pendingIntent);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

            builder.setContentTitle("bSocial");

            builder.setContentText(push.getMessage());

            builder.setSubText("Clique aqui para mais informações.");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(null, 1, builder.build());
        }
    }
}
