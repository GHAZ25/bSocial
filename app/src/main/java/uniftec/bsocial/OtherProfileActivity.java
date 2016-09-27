package uniftec.bsocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.cache.LikesCache;
import uniftec.bsocial.entities.UserSearch;

public class OtherProfileActivity extends AppCompatActivity {

    private UserSearch user;
    private LikeAdapter likeAdapter;
    private LikesCache likesCache = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        user = (UserSearch) getIntent().getSerializableExtra("user");

        //createLikeList();
        loadUser();
    }

    private void loadUser() {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setProfileId(user.getId());

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
}
