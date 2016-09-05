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
import java.util.List;

import uniftec.bsocial.domain.Preference;

public class LikesChosenCache {
    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private ArrayList<Preference> preferences = null;

    public LikesChosenCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        file = "chosenLikes" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        preferences = new ArrayList<>();

        if (sharedpreferences.getAll().size() == 0) {
            ListPreferences listPreferences = new ListPreferences();
            listPreferences.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Preference preference = new Preference(sharedpreferences.getString("id" + i, ""));

                preferences.add(preference);
            }
        }
    }

    public ArrayList<Preference> listPreferences() {
        return preferences;
    }

    public void update() {
        UpdatePreferences updatePreferences = new UpdatePreferences();
        updatePreferences.execute();
    }

    private class UpdatePreferences extends AsyncTask<Integer, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Alterando preferências...");
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                for (int i = 0; i < 9; i++) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost request = null;

                    List<NameValuePair> values = new ArrayList<>(2);

                    if (i > (preferences.size() - 1)) {
                        request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/remove");
                    } else {
                        request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/update");
                        values.add(new BasicNameValuePair("id_preferencia", preferences.get(i).getId()));
                    }

                    values.add(new BasicNameValuePair("ordem", Integer.toString(i)));
                    values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                    request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                    HttpResponse response = httpclient.execute(request);
                    InputStream content = response.getEntity().getContent();
                    Reader reader = new InputStreamReader(content);

                    content.close();
                }

                return "true";
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

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();

                        for (int i = 0; i < preferences.size(); i++) {
                            editor.putString("id" + i, preferences.get(i).getId());
                        }

                        editor.putInt("size", preferences.size());
                        editor.commit();
                    break;
                    default:
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }

    private class ListPreferences extends AsyncTask<Void, Void, Preference[]> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando preferências...");
        }

        @Override
        protected Preference[] doInBackground(Void... params) {
            Preference[] retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/list");
                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, Preference[].class);

                content.close();
            } catch (Exception e) { }

            return retorno;
        }

        @Override
        protected void onPostExecute(Preference[] retorno) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();

            if (retorno != null) {
                int cont = 0;

                for (int i = 0; i < retorno.length; i++) {
                    editor.putString("id" + i, retorno[i].getId());
                    preferences.add(retorno[i]);

                    cont++;
                }

                editor.putInt("size", cont);

                Toast.makeText(activity, "Gostos atualizados com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Ocorreu um erro ao listar suas preferências. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            editor.commit();
            load.dismiss();
        }
    }
}
