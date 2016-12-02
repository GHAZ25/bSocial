package uniftec.bsocial.cache;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import uniftec.bsocial.OtherUserMessageActivity;
import uniftec.bsocial.entities.Notification;
import uniftec.bsocial.entities.messages.MessageNotifications;

public class NotificationCache {
    private FragmentActivity activity = null;
    private Context context = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private MessageCache messageCache = null;
    private String file = null;
    private String type = null;
    private String contato = null;
    private ArrayList<Notification> notifications = null;
    private String texto = null;
    private String nome = null;
    private String nomeContato = null;

    public NotificationCache(FragmentActivity activity, String type, String contato) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        messageCache = new MessageCache(activity.getApplicationContext());
        this.type = type;
        this.contato = contato;
        file = type + profile.getId() + contato;
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        notifications = new ArrayList<>();
    }

    public NotificationCache(String type, String contato, Context context) {
        super();

        profile = Profile.getCurrentProfile();
        messageCache = new MessageCache(context);
        this.type = type;
        this.contato = contato;
        this.context = context;
        file = type + profile.getId() + contato;
        sharedpreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            ListNotification listNotification = new ListNotification();
            listNotification.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Notification notification = new Notification(sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("message" + i, ""), type, sharedpreferences.getString("messageId" + i, ""));
                notifications.add(notification);
            }
        }
    }

    public void initializeMessages() {
        if (sharedpreferences.getAll().size() == 0) {
            ListMessages listMessages = new ListMessages();
            listMessages.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Notification notification = new Notification(sharedpreferences.getString("id" + i, ""), sharedpreferences.getString("message" + i, ""), type, sharedpreferences.getString("messageId" + i, ""));
                notifications.add(notification);
            }
        }
    }

    public void sendNotification(String texto, String nome, String destino, String nomeContato) {
        String[] params = new String[4];

        params[0] = texto;
        params[1] = nome;
        params[2] = destino;
        params[3] = nomeContato;

        SendNotification sendNotification = new SendNotification();
        sendNotification.execute(params);
    }

    public void sendConfirm(String id, String message) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("id" + sharedpreferences.getInt("size", 0), id);
        editor.putString("message" + sharedpreferences.getInt("size", 0), message);

        editor.putInt("size", sharedpreferences.getInt("size", 0) + 1);

        editor.commit();
    }

    public void newInvite(String id, String message, String messageId) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("id" + sharedpreferences.getInt("size", 0), id);
        editor.putString("message" + sharedpreferences.getInt("size", 0), message);
        editor.putString("messageId" + sharedpreferences.getInt("size", 0), messageId);

        editor.putInt("size", sharedpreferences.getInt("size", 0) + 1);

        editor.commit();
    }

    public void acceptInvite(String origem, String aceite, String mensagemId) {
        String[] params = new String[3];

        params[0] = origem;      //o ID do usuário que enviou a solicitação
        params[1] = aceite;      //se for aceito o valor deve ser "true"
        params[2] = mensagemId;  // ID da mensagem enviada para a solicitação

        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getMessageId().equals(mensagemId)) {
                notifications.remove(i);
            }
        }

        AcceptInvite acceptInvite = new AcceptInvite();
        acceptInvite.execute(params);
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

    /* public void updateNotifications() {
        UpdateNotification updateNotification = new UpdateNotification();
        updateNotification.execute();
    } */

    private class ListNotification extends AsyncTask<Void, Void, MessageNotifications> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando convites...");
        }

        @Override
        protected MessageNotifications doInBackground(Void... params) {
            MessageNotifications retorno = null;
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
                retorno = gson.fromJson(reader, MessageNotifications.class);

                content.close();
            } catch (Exception e){ }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessageNotifications retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();

                    for (int i = 0; i < retorno.getNotifications().size(); i++) {
                        editor.putString("id" + i, retorno.getNotifications().get(i).getId());
                        editor.putString("message" + i, retorno.getNotifications().get(i).getMessage());
                        editor.putString("messageId" + i, retorno.getNotifications().get(i).getMessageId());

                        notifications.add(retorno.getNotifications().get(i));
                    }
                    editor.putInt("size", notifications.size());

                    editor.commit();
                } else {
                    Toast.makeText(activity, retorno.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Não foi possível carregar a conversa. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
        }
    }

    /* private class UpdateNotification extends AsyncTask<Void, Void, MessageNotifications> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected MessageNotifications doInBackground(Void... params) {
            MessageNotifications retorno = null;
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
                retorno = gson.fromJson(reader, MessageNotifications.class);

                content.close();
            } catch (Exception e){ }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessageNotifications retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    for (int i = 0; i < retorno.getNotifications().size(); i++) {
                        notifications.add(retorno.getNotifications().get(i));
                    }
                }
            }
        }
    } */

    private class ListMessages extends AsyncTask<Void, Void, MessageNotifications> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando mensagens...");
        }

        @Override
        protected MessageNotifications doInBackground(Void... params) {
            MessageNotifications retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/gcm/listMessage");

                values.add(new BasicNameValuePair("id", profile.getId()));
                values.add(new BasicNameValuePair("contato", contato));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, MessageNotifications.class);

                content.close();
            } catch (Exception e){ }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessageNotifications retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();

                    for (int i = 0; i < retorno.getNotifications().size(); i++) {
                        editor.putString("id" + i, retorno.getNotifications().get(i).getId());
                        editor.putString("message" + i, retorno.getNotifications().get(i).getMessage());
                        //editor.putString("messageId" + i, retorno.getNotifications().get(i).getMessageId());

                        notifications.add(retorno.getNotifications().get(i));
                    }
                    editor.putInt("size", notifications.size());

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

                texto = params[0];
                nome = params[1];
                nomeContato = params[3];
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
                if (activity.getLocalClassName().equals("OtherUserMessageActivity")) {
                    OtherUserMessageActivity otherUserMessageActivity = (OtherUserMessageActivity) activity;
                    otherUserMessageActivity.updateMessages("Você: " + texto);
                }

                sendConfirm(profile.getId(), "Você: " + texto);
                messageCache.messageConfirm(contato, texto, nomeContato);
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
                Toast.makeText(activity, "Convite enviado com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AcceptInvite extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(String... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/contact/invitation");

                values.add(new BasicNameValuePair("origem", params[0]));
                values.add(new BasicNameValuePair("destino", profile.getId()));
                values.add(new BasicNameValuePair("aceite", params[1]));
                values.add(new BasicNameValuePair("mensagemId", params[2]));
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
                Toast.makeText(activity, "Resposta enviada com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
