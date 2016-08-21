package uniftec.bsocial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog load;
    private JSONObject jsonObject;
    private String name;
    private String email;
    private TextView nameText;
    private TextView emailText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle("bSocial");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        jsonObject = object;
                        UserValidate validate = new UserValidate();
                        validate.execute();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,age_range.fields(min)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registerView(JSONObject object) {
        setContentView(R.layout.activity_sign_up);
        setTitle("Cadastro");
        setProfilePic(object);
        nameText = (TextView) findViewById(R.id.nameText);
        setName(object);
        emailText = (TextView) findViewById(R.id.emailText);
        setEmail(object);
        setAge(object);
        setGender(object);
        save();
        cancel();
    }

    private void setProfilePic(JSONObject object) {
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setProfileId(object.optString("id"));
    }

    private void setName(JSONObject object) {
       nameText.setText(object.optString("name"));
    }

    private void setEmail(JSONObject object) {
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
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameText.getText().toString();
                email = emailText.getText().toString();

                UserRegister register = new UserRegister();
                register.execute();
            }
        });
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

    private class UserValidate extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(SignUpActivity.this, "Aguarde", "Validando usuário...");
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/user/consultar?id=" + jsonObject.optString("id")));
                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();

                return retorno.get("message").toString();
            }catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                switch (message) {
                    case "true":
                        startActivity(new Intent(SignUpActivity.this, NavigationDrawerActivity.class));
                        finish();
                    break;
                    case "false":
                        registerView(jsonObject);
                    break;
                    default:
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                }
            }
            load.dismiss();
        }
    }

    private class UserRegister extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(SignUpActivity.this, "Aguarde", "Cadastrando usuário...");
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/user/cadastrar");

                List<NameValuePair> values = new ArrayList<>(2);
                values.add(new BasicNameValuePair("nome", name));
                values.add(new BasicNameValuePair("email", email));
                values.add(new BasicNameValuePair("id", jsonObject.optString("id")));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                //request.setURI(new URI("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/user/cadastrar?nome=" + name + "&email=" + email + "&id=" + jsonObject.optString("id")));
                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();

                return retorno.get("message").toString();
            }catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                switch (message) {
                    case "true":
                        startActivity(new Intent(SignUpActivity.this, NavigationDrawerActivity.class));
                        finish();
                    default:
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro ao efetuar o cadastro. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }
}
