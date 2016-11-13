package uniftec.bsocial.cache;

import android.app.ProgressDialog;
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

import uniftec.bsocial.entities.Message;

public class MessageCache {
    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private Profile profile = null;
    private ArrayList<Message> messages = null;

    public MessageCache(FragmentActivity activity) {
        super();

        this.activity = activity;
        messages = new ArrayList<Message>();

        profile = Profile.getCurrentProfile();
    }

    public void initialize() {
        ListNotification listNotification = new ListNotification();
        listNotification.execute();
    }

    public ArrayList<Message> listMessages() {
        return messages;
    }

    private class ListNotification extends AsyncTask<Void, Void, Message[]> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando convites...");
        }

        @Override
        protected Message[] doInBackground(Void... params) {
            Message[] retorno = null;
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
                retorno = gson.fromJson(reader, Message[].class);

                content.close();

                return retorno;
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message[] list) {
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    messages.add(list[i]);
                }
            }

            load.dismiss();
        }
    }
}
