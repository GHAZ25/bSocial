package uniftec.bsocial.cache;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uniftec.bsocial.entities.User;

public class UserCache {
    private FragmentActivity activity = null;
    private Context context = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String id = null;
    private String file = null;
    private User user = null;

    public UserCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        file = "settings" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public UserCache(String id, Context context) {
        super();

        this.context = context;

        this.id = id;
        file = "settings" + id;
        sharedpreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            ListPreferences listPreferences = new ListPreferences();
            listPreferences.execute();
        } else {
            user = new User(sharedpreferences.getString("nome", ""),
                    sharedpreferences.getString("email", ""),
                    sharedpreferences.getString("id", ""),
                    sharedpreferences.getBoolean("oculto", false),
                    sharedpreferences.getBoolean("notifica", false),
                    Double.parseDouble(sharedpreferences.getString("latitude", "0")),
                    Double.parseDouble(sharedpreferences.getString("longitude", "0")));
        }
    }

    public void saveUser(User user) {
        this.user = user;

        updateSettings();
    }

    public void update() {
        UpdatePreferences updatePreferences = new UpdatePreferences();
        updatePreferences.execute();
    }

    public User getUser() {
        return user;
    }

    public void updateLocation() {
        UpdateLocation updateLocation = new UpdateLocation();
        updateLocation.execute();
    }

    private void updateSettings() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();

        editor.putString("nome", user.getNome());
        editor.putString("email", user.getEmail());
        editor.putString("id", user.getIdFacebook());
        editor.putBoolean("oculto", user.isOculto());
        editor.putBoolean("notifica", user.isNotifica());
        editor.putString("latitude", Double.toString(user.getLatitude()));
        editor.putString("longitude", Double.toString(user.getLongitude()));

        editor.commit();
    }

    private class ListPreferences extends AsyncTask<Void, Void, User> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected User doInBackground(Void... params) {
            User retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/user/preference");
                if (id == null) {
                    values.add(new BasicNameValuePair("id", profile.getId()));
                } else {
                    values.add(new BasicNameValuePair("id", id));
                }
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, User.class);

                content.close();

                return retorno;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            if (result != null) {
                user = result;

                updateSettings();
            } else {
                if (id == null) {
                    Toast.makeText(activity, "Ocorreu um erro ao carregar as preferências.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Ocorreu um erro ao carregar as preferências.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class UpdatePreferences extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Alterando preferências...");
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/user/update");

                String oculto = "false";
                String notifica = "false";

                if (user.isOculto()) {
                    oculto = "true";
                }

                if (user.isNotifica()) {
                    notifica = "true";
                }

                values.add(new BasicNameValuePair("oculto", oculto));
                values.add(new BasicNameValuePair("notifica", notifica));
                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();

                return retorno.get("message").toString();
            } catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                switch (message) {
                    case "true":
                        Toast.makeText(activity, "Preferências alteradas com sucesso.", Toast.LENGTH_LONG).show();

                        updateSettings();
                    break;
                    default:
                        Toast.makeText(activity, "Ocorreu um erro ao alterar as preferências. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }

    private class UpdateLocation extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/user/location");

                values.add(new BasicNameValuePair("latitude", Double.toString(user.getLatitude())));
                values.add(new BasicNameValuePair("longitude", Double.toString(user.getLongitude())));
                values.add(new BasicNameValuePair("id", id));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();

                return retorno.get("message").toString();
            } catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (message.equals("true")) {
                updateSettings();
            }
        }
    }
}
