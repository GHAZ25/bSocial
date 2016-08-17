package uniftec.bsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Cadastro");

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                       setContentView(R.layout.activity_sign_up);
                        setProfilePic(object);
                        setName(object);
                        setEmail(object);
                        setAge(object);
                        setGender(object);
                        save();
                        cancel();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,age_range.fields(min)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setProfilePic(JSONObject object) {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setProfileId(object.optString("id"));
    }

    private void setName(JSONObject object) {
       TextView nameText = (TextView) findViewById(R.id.nameText);
       nameText.setText(object.optString("name"));
    }

    private void setEmail(JSONObject object) {
        TextView emailText = (TextView) findViewById(R.id.emailText);
        emailText.setText(object.optString("email"));
    }

    private void setAge(JSONObject object) {
        TextView ageText = (TextView) findViewById(R.id.ageText);
        JSONObject object1 = object.optJSONObject("age_range");
        String age = object1.optString("min");
        ageText.setText("Idade: " + age + " anos");
    }

    private void setGender(JSONObject object) {
        TextView genderText = (TextView) findViewById(R.id.genderText);
        String gender = object.optString("gender");
        gender = gender.replaceAll("male", "masculino");
        gender = gender.replaceAll("female", "feminino");
        genderText.setText(gender.toUpperCase());

    }

    private void save() {

    }

    private void cancel() {
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        LoginManager.getInstance().logOut();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
}
