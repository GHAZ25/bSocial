package uniftec.bsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
    public static CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Bem-vindo!");

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        if(AccessToken.getCurrentAccessToken() != null) {
            if (AccessToken.getCurrentAccessToken().isExpired()){
                Toast.makeText(getApplicationContext(), "Seu login expirou. Faça login novamente.", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
                finish();
            }
        }


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_likes, user_birthday");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               //startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
               startActivity(new Intent(MainActivity.this, SignUpActivity.class));
               finish();
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