package uniftec.bsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ImageButton locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        user = (UserSearch) getIntent().getSerializableExtra("user");

        userCache = new UserCache(this);
        userCache.initialize();

        sendMsgBtn = (Button) findViewById(R.id.sendMsg);
        sendMsgBtn.setOnClickListener(this);

        locationBtn = (ImageButton) findViewById(R.id.otherprofile_location_button);
        locationBtn.setOnClickListener(this);

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
            case R.id.otherprofile_location_button:
                openMap();
            break;

        }
    }


    private void sendMsg(FragmentManager manager) {
        MessageFragment messageFragment = new MessageFragment();

        Bundle args = new Bundle();
        args.putString(MessageFragment.USER_ID, user.getId());
        args.putString(MessageFragment.TYPE, "randommsg");
        messageFragment.setArguments(args);

        messageFragment.show(manager, "enviar_mensagem");
    }

    private void loadUser() {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setProfileId(user.getId());

        TextView nameText = (TextView) findViewById(R.id.nameAgeText);
        nameText.setText(user.getName());

        TextView pctText = (TextView) findViewById(R.id.pctText);
        if (user.getCompatibilidade() >= 90)
            pctText.setTextColor(getResources().getColor(R.color.colorHighPct));
        else if (user.getCompatibilidade() >= 60)
            pctText.setTextColor(getResources().getColor(R.color.colorNormalPct));
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

    private void openMap() {
        String userGeo = user.getLatitude() + "," + user.getLongitude();
        String userCacheGeo = userCache.getUser().getLatitude() + "," + userCache.getUser().getLongitude();

        if (userGeo.equals(userCacheGeo))
            Toast.makeText(getApplicationContext(), "Vocês estão super próximos!", Toast.LENGTH_SHORT).show();
        else {
            try {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + userGeo + "&daddr=" + userCacheGeo));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Você deve baixar o Google Maps para utilizar esse recurso!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onFragmentInteraction(Uri uri) { }
}
