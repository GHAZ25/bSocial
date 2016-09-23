package uniftec.bsocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import uniftec.bsocial.entities.UserSearch;

public class OtherProfileActivity extends AppCompatActivity {

    private UserSearch user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        user = (UserSearch) getIntent().getSerializableExtra("user");

        loadUser();
    }

    private void loadUser() {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setId(Integer.getInteger(user.getId()));

        TextView nameText = (TextView) findViewById(R.id.nameAgeText);
        nameText.setText(user.getName());

        TextView hometown = (TextView) findViewById(R.id.locationText);
    }

    private void createLikeList() {
        ListView likesListView = (ListView) findViewById(R.id.likesListView);
        //likeAdapter = new LikeAdapter(getContext(), likesCache.listLikes());
        //likesListView.setAdapter(likeAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
