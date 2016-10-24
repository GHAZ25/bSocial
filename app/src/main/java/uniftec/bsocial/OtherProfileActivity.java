package uniftec.bsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.cache.UserCache;
import uniftec.bsocial.entities.UserSearch;
import uniftec.bsocial.fragments.MessageFragment;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener, MessageFragment.OnFragmentInteractionListener {

    private UserSearch user;
    private LikeAdapter likeAdapter;
    private UserCache userCache = null;
    private Button sendMsgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        user = (UserSearch) getIntent().getSerializableExtra("user");

        userCache = new UserCache(this);
        userCache.initialize();

        sendMsgBtn = (Button) findViewById(R.id.sendMsg);
        sendMsgBtn.setOnClickListener(this);

        createLikeList();
        loadUser();

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

    private void loadUser() {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setProfileId(user.getId());

        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userGeo = user.getLatitude() + "," + user.getLongitude();
                String userCacheGeo = userCache.getUser().getLatitude() + "," + userCache.getUser().getLongitude();

                if (userGeo.equals(userCacheGeo))
                    Toast.makeText(getApplicationContext(), "Vocês estão super próximos!", Toast.LENGTH_SHORT).show();
                else {
                    final Intent intent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                            "saddr=" + userGeo + "&daddr=" + userCacheGeo));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }
        });

        TextView nameText = (TextView) findViewById(R.id.nameAgeText);
        nameText.setText(user.getName());

        TextView pctText = (TextView) findViewById(R.id.pctText);
        if (user.getCompatibilidade() >= 90)
            pctText.setTextColor(getResources().getColor(R.color.colorHighPct));
        else if (user.getCompatibilidade() >= 30)
            pctText.setTextColor(getResources().getColor(R.color.colorMediumPct));
        else pctText.setTextColor(getResources().getColor(R.color.colorLowPct));

        String pct = String.valueOf(Math.round(user.getCompatibilidade()));

        pctText.setText("Compatibilidade: " + pct + "%");
    }

    private void createLikeList() {
        ListView likesListView = (ListView) findViewById(R.id.likesListView);
        likeAdapter = new LikeAdapter(getApplicationContext(), user.getLikes());
        likesListView.setAdapter(likeAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onFragmentInteraction(Uri uri) { }
}
