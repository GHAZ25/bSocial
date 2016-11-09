package uniftec.bsocial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uniftec.bsocial.entities.UserSearch;
import uniftec.bsocial.fragments.MessageFragment;

public class OtherUserMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private UserSearch user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_message);

        user = (UserSearch) getIntent().getSerializableExtra("user");

        ListView messagesListView = (ListView) findViewById(R.id.other_user_messages_listview);
        String[] messages = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages);

        messagesListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag("enviar_mensagem");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        switch (view.getId()) {
            case R.id.sendMsg:
                sendMsg(manager);
                break;
        }
    }

    private void sendMsg(FragmentManager manager) {
        MessageFragment messageFragment = new MessageFragment();

        Bundle args = new Bundle();
        args.putString(MessageFragment.USER_ID, user.getId());
        messageFragment.setArguments(args);

        messageFragment.show(manager, "enviar_mensagem");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
