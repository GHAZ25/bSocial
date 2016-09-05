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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uniftec.bsocial.entities.LikeEntity;

public class LikesCache {
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private Date today = null;

    public LikesCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        today = new Date();
        file = "preferences" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public void update() {
        LoadPreference loadPreference = new LoadPreference();

        if (sharedpreferences.getAll().size() == 0) {
            loadPreference.execute();
        } else {
            try {
                Date update = dateFormat.parse(sharedpreferences.getString("update", "erro"));

                if (!dateFormat.format(today).toString().equals(dateFormat.format(update).toString())) {
                    loadPreference.execute();
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
                        JSONObject jsonObject2 = object.optJSONObject("likes");
                        JSONArray jsonArray = jsonObject2.optJSONArray("data");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();

                        int cont = 0;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.optJSONObject(i);
                            String id = jsonObject3.optString("id");
                            String name = jsonObject3.optString("name");

                            jsonObject3 = jsonObject3.optJSONObject("picture");
                            jsonObject3 = jsonObject3.optJSONObject("data");
                            String pictureUrl = jsonObject3.optString("url");

                            editor.putString("id" + i, id);
                            editor.putString("name" + i, name);
                            editor.putString("picture" + i, pictureUrl);

                            cont++;
                        }

                        editor.putString("update", dateFormat.format(today).toString());
                        editor.putInt("size", cont);
                        editor.commit();
                    }
                });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes.fields(id,name,picture.type(large))");
        request.setParameters(parameters);

        request.executeAsync();
    }

    public ArrayList<LikeEntity> listLikes() {
        ArrayList<LikeEntity> likeEntities = new ArrayList<>();

        for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
            LikeEntity likeEntity = new LikeEntity(sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("picture" + i, ""), null);

            likeEntities.add(likeEntity);
        }

        return likeEntities;
    }

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
}
