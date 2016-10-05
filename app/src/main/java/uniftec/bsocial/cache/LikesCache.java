package uniftec.bsocial.cache;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uniftec.bsocial.entities.Like;

public class LikesCache {
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private Date today = null;
    private JSONObject obj = null;
    private Like[] likes = null;

    public LikesCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        today = new Date();
        file = "preferences" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        likes = new Like[sharedpreferences.getInt("size", 0)];
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            LoadPreference loadPreference = new LoadPreference();
            loadPreference.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Like like = new Like(sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("picture" + i, ""), null);

                likes[i] = like;
            }
        }
    }

    public void verify() {
        LoadPreference loadPreference = new LoadPreference();

        if (sharedpreferences.getAll().size() == 0) {
            loadPreference.execute();
        } else {
            try {
                Date update = dateFormat.parse(sharedpreferences.getString("update", "erro"));

                if (!dateFormat.format(today).toString().equals(dateFormat.format(update).toString())) {
                    loadPreference.execute();
                } else {
                    for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                        Like like = new Like(sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("picture" + i, ""), null);

                        likes[i] = like;
                    }
                }
            } catch (ParseException e) {
                Toast.makeText(activity, "Ocorreu um erro ao verificar a ultima atualização.", Toast.LENGTH_LONG).show();
                loadPreference.execute();
            }
        }
    }

    private void updateFacebook() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        obj = object;

                        UpdateLikes updateLikes = new UpdateLikes();
                        updateLikes.execute();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes.fields(id,name,picture.type(large),category)");
        parameters.putString("locale", "pt_br");
        request.setParameters(parameters);

        request.executeAsync();
    }

    public Like[] listLikes() { return likes; }

    private class LoadPreference extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(activity, "Aguarde", "Carregando preferências...");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                updateFacebook();
            } catch (Exception e) {
                return e.getMessage();
            }
            return "true";
        }

        @Override
        protected void onPostExecute(String message) {
            load.dismiss();
            if (!message.equals("true")) {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Preferências atualizadas com sucesso.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateLikes extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(activity, "Aguarde", "Atualizando likes...");
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/like/insert");
                values.add(new BasicNameValuePair("json", obj.toString()));
                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();
            } catch (Exception e) {
                return e.getMessage();
            }
            return retorno.get("message").toString();
        }

        @Override
        protected void onPostExecute(String message) {
            load.dismiss();

            if (message.equals("true")) {

                JSONObject list = obj.optJSONObject("likes");
                JSONArray jsonArray = list.optJSONArray("data");

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();

                int cont = 0;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    String id = jsonObject.optString("id");
                    String name = jsonObject.optString("name");
                    String category = jsonObject.optString("category");

                    jsonObject = jsonObject.optJSONObject("picture");
                    jsonObject = jsonObject.optJSONObject("data");
                    String pictureUrl = jsonObject.optString("url");

                    Like like = new Like(id, name, pictureUrl, category);
                    likes[cont] = like;

                    editor.putString("id" + i, id);
                    editor.putString("name" + i, name);
                    editor.putString("picture" + i, pictureUrl);
                    editor.putString("category" + i, category);

                    cont++;
                }

                editor.putString("update", dateFormat.format(today).toString());
                editor.putInt("size", cont);
                editor.commit();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
