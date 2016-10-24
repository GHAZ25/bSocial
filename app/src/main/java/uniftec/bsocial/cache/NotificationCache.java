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

import uniftec.bsocial.entities.Notification;
import uniftec.bsocial.entities.User;

public class NotificationCache {
    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private Profile profile = null;
    private ArrayList<Notification> notifications = null;

    public NotificationCache(FragmentActivity activity) {
        super();

        this.activity = activity;
        notifications = new ArrayList<Notification>();

        profile = Profile.getCurrentProfile();
    }

    public void initialize() {
        ListNotification listNotification = new ListNotification();
        listNotification.execute();
    }

    public void sendNotification(String texto, String nome, String destino) {
        String[] params = new String[3];

        params[0] = texto;
        params[1] = nome;
        params[2] = destino;

        SendNotification sendNotification = new SendNotification();
        sendNotification.execute(params);
    }

    public void inviteNotification(String texto, String nome, String destino) {
        String[] params = new String[3];

        params[0] = texto;
        params[1] = nome;
        params[2] = destino;

        InviteNotification inviteNotification = new InviteNotification();
        inviteNotification.execute(params);
    }

    public ArrayList<Notification> listNotifications() {
        return notifications;
    }

    public void updateNotifications() {
        UpdateNotification updateNotification = new UpdateNotification();
        updateNotification.execute();
    }

    private class ListNotification extends AsyncTask<Void, Void, Notification[]> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected Notification[] doInBackground(Void... params) {
            Notification[] retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/list");

                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, Notification[].class);

                content.close();

                return retorno;
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Notification[] list) {
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    notifications.add(list[i]);
                }
            }
        }
    }

    private class UpdateNotification extends AsyncTask<Void, Void, Notification[]> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected Notification[] doInBackground(Void... params) {
            Notification[] retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/new");

                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, Notification[].class);

                content.close();

                return retorno;
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Notification[] list) {
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    notifications.add(list[i]);
                }
            }
        }
    }

    private class SendNotification extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(String... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/send");

                values.add(new BasicNameValuePair("texto", params[0]));
                values.add(new BasicNameValuePair("nome", params[1]));
                values.add(new BasicNameValuePair("origem", profile.getId()));
                values.add(new BasicNameValuePair("destino", params[2]));
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
                Toast.makeText(activity, "Mensagem enviada com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class InviteNotification extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(String... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/invite");

                values.add(new BasicNameValuePair("texto", params[0]));
                values.add(new BasicNameValuePair("nome", params[1]));
                values.add(new BasicNameValuePair("origem", profile.getId()));
                values.add(new BasicNameValuePair("destino", params[2]));
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
                Toast.makeText(activity, "Mensagem enviada com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
