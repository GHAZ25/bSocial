package uniftec.bsocial;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.cache.NotificationCache;
import uniftec.bsocial.entities.Notification;
import uniftec.bsocial.fragments.MessageFragment;

public class OtherUserMessageActivity extends AppCompatActivity implements View.OnClickListener, MessageFragment.OnFragmentInteractionListener {

    private Profile profile;
    private String userId;
    private String userName;
    private Button sendMsg;
    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messages;
    private NotificationCache messageCache;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_message);

        userName = (String) getIntent().getSerializableExtra("userName");
        setTitle(R.string.messages_from + userName);

        sendMsg = (Button) findViewById(R.id.send_message);
        sendMsg.setOnClickListener(this);

        profile = Profile.getCurrentProfile();
        userId = (String) getIntent().getSerializableExtra("userId");

        messageCache = new NotificationCache(this, "mensagem", userId);
        messageCache.initializeMessages();

        messages = new ArrayList<>();

        messagesListView = (ListView) findViewById(R.id.other_user_messages_listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, messages);

        messagesListView.setAdapter(adapter);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (messageCache.listNotifications().size() != 0) {
                                for (Notification message : messageCache.listNotifications()) {
                                    updateMessages(message.getMessage());
                                }
                                timer.cancel();
                            }
                        }
                    });
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000, 4000);
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag("enviar_mensagem");

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        switch (view.getId()) {
            case R.id.send_message:
                sendMsg(manager);
            break;
        }
    }

    private void sendMsg(FragmentManager manager) {
        MessageFragment messageFragment = new MessageFragment();

        Bundle args = new Bundle();
        args.putString(MessageFragment.USER_ID, userId);
        args.putString(MessageFragment.TYPE, "messagelistview");
        messageFragment.setArguments(args);

        messageFragment.show(manager, "enviar_mensagem");
    }

    public void updateMessages(String message) {
        messages.add(message);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
