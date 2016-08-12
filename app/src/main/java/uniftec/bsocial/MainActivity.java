package uniftec.bsocial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    public static CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_likes");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
           }

           @Override
           public void onCancel() {
               Toast.makeText(getApplicationContext(), "Ação cancelada.", Toast.LENGTH_LONG).show();
           }

           @Override
           public void onError(FacebookException exception) {
               Toast.makeText(getApplicationContext(), "Erro ao logar. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
           }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}