package uniftec.bsocial;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import uniftec.bsocial.fragments.MessageFragment;

public class OtherUserMessageActivity extends AppCompatActivity implements View.OnClickListener, MessageFragment.OnFragmentInteractionListener {

    private String userId;
    private String userName;
    private Button sendMsg;
    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_message);

        userName = (String) getIntent().getSerializableExtra("userName");
        setTitle("Mensagens de " + userName);

        sendMsg = (Button) findViewById(R.id.send_message);
        sendMsg.setOnClickListener(this);

        userId = (String) getIntent().getSerializableExtra("userId");

        messagesListView = (ListView) findViewById(R.id.other_user_messages_listview);
        messages = new ArrayList<>();
        messages.add("Maurício Manfro: Olá tudo bem?");
        messages.add("Maurício Manfro: Curte Rock?");
        messages.add("Você: Opaa tudo e contigo?");
        messages.add("Você: Curto sim!");

        adapter = new ArrayAdapter<>(this,
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
