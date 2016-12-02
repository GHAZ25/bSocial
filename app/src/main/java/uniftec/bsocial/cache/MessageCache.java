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

import uniftec.bsocial.entities.Message;
import uniftec.bsocial.entities.messages.MessageMessages;

public class MessageCache {
    private FragmentActivity activity = null;
    private Context context = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private ArrayList<Message> messages = null;

    public MessageCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        file = "messages" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        messages = new ArrayList<>();
    }

    public MessageCache(Context context) {
        super();

        profile = Profile.getCurrentProfile();
        file = "messages" + profile.getId();
        this.context = context;
        sharedpreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            ListNotification listNotification = new ListNotification();
            listNotification.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Message message = new Message(profile.getId(), sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("message" + i, ""));
                messages.add(message);
            }
        }
    }

    public void messageConfirm(String id, String message, String name) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        boolean newMessage = true;

        for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
            if (sharedpreferences.getString("id" + i, "").equals(id)) {
                editor.putString("message" + i, message);
                newMessage = false;
            }
        }

        if (newMessage) {
            editor.putString("id" + sharedpreferences.getInt("size", 0), id);
            editor.putString("name" + sharedpreferences.getInt("size", 0), name);
            editor.putString("message" + sharedpreferences.getInt("size", 0), message);

            editor.putInt("size", sharedpreferences.getInt("size", 0) + 1);
        }

        editor.commit();
    }

    public ArrayList<Message> listMessages() {
        return messages;
    }

    private class ListNotification extends AsyncTask<Void, Void, MessageMessages> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando conversas...");
        }

        @Override
        protected MessageMessages doInBackground(Void... params) {
            MessageMessages retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/messages");

                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, MessageMessages.class);

                content.close();
            } catch (Exception e){ }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessageMessages retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();

                    for (int i = 0; i < retorno.getMessages().size(); i++) {
                        editor.putString("id" + i, retorno.getMessages().get(i).getSentUserId());
                        editor.putString("name" + i, retorno.getMessages().get(i).getSentUserName());
                        //editor.putString("url" + i, retorno.getMessages().get(i).getSentUserPicUrl());
                        editor.putString("message" + i, retorno.getMessages().get(i).getMessage());

                        messages.add(retorno.getMessages().get(i));
                    }
                    editor.putInt("size", messages.size());

                    editor.commit();
                } else {
                    Toast.makeText(activity, retorno.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Não foi possível listar suas mensagens. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
        }
    }
}
