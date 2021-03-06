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
import java.util.Map;

import uniftec.bsocial.entities.messages.MessagePreferences;

public class LikesChosenCache {
    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private ArrayList<String> preferences = null;

    public LikesChosenCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        file = "chosenLikes" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        preferences = new ArrayList<>();
    }

    public ArrayList<String> listPreferences() {
        return preferences;
    }

    public Map<String, Boolean> mapPreferences() {
        Map<String, Boolean> map = new HashMap<String, Boolean>();

        for (String key : preferences) {
            map.put(key, true);
        }

        return map;
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            ListPreferences listPreferences = new ListPreferences();
            listPreferences.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                preferences.add(sharedpreferences.getString("preference" + i, ""));
            }
        }
    }

    public void remove(String id) {
        int cont = 0;

        while (cont < preferences.size()) {
            if (id.equals(preferences.get(cont))) {
                preferences.remove(cont);
                cont = preferences.size();
            } else {
                cont++;
            }
        }
    }

    private void recreate() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();

        for (int i = 0; i < preferences.size(); i++) {
            editor.putString("preference" + i, preferences.get(i));
        }

        editor.putInt("size", preferences.size());
        editor.commit();
    }

    public void update() {
        String json = "{ \"preferences\": [";
        int cont = 0;

        for (int i = 0; i < preferences.size(); i++) {
            if (cont > 0) {
                json += ",";
            }
            json += "{\"preference\":\"" + preferences.get(i) + "\"}";

            cont++;
        }

        json += "] }";

        UpdatePreferences updatePreferences = new UpdatePreferences();
        updatePreferences.execute(json);
    }

    private class UpdatePreferences extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Alterando preferências...");
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap retorno = null;
            String mensagem = "true";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/update");

                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                values.add(new BasicNameValuePair("json", params[0]));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                if (!retorno.get("message").toString().equals("true")) {
                    mensagem = retorno.get("message").toString();
                }

                content.close();

                return mensagem;
            } catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                switch (message) {
                    case "true":
                        recreate();
                        Toast.makeText(activity, "Preferências alteradas com sucesso.", Toast.LENGTH_LONG).show();
                    break;
                    default:
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }

    private class ListPreferences extends AsyncTask<Void, Void, MessagePreferences> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando preferências...");
        }

        @Override
        protected MessagePreferences doInBackground(Void... params) {
            MessagePreferences retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/list");
                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, MessagePreferences.class);

                content.close();
            } catch (Exception e) { }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessagePreferences retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    for (int i = 0; i < retorno.getPreferences().size(); i++) {
                        preferences.add(retorno.getPreferences().get(i).toString());
                    }

                    recreate();

                    Toast.makeText(activity, "Gostos atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, retorno.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Não foi possível listar suas preferências. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
        }
    }
}
