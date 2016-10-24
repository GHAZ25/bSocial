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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uniftec.bsocial.entities.Category;
import uniftec.bsocial.entities.User;
import uniftec.bsocial.entities.UserSearch;

public class ContactsCache {
    //private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    //private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    //private Date today = null;
    //private String file = null;
    //private ArrayList<User> contacts = null;
    private ArrayList<UserSearch> contacts = null;

    public ContactsCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        //today = new Date();
        //file = "contacts" + profile.getId();
        //sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        contacts = new ArrayList<>();
    }

    /*public void verify() {
        LoadContacts loadPreference = new LoadContacts();

        if (sharedpreferences.getAll().size() == 0) {
            loadPreference.execute();
        } else {
            try {
                Date update = dateFormat.parse(sharedpreferences.getString("update", "erro"));

                if (!dateFormat.format(today).toString().equals(dateFormat.format(update).toString())) {
                    loadPreference.execute();
                } else {
                    for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                        User contact = new User(sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("email" + i, ""), sharedpreferences.getString("idFacebook" + i, ""), false, false, 0.0, 0.0);

                        contacts.add(contact);
                    }
                }
            } catch (ParseException e) {
                Toast.makeText(activity, "Ocorreu um erro ao verificar a ultima atualização.", Toast.LENGTH_LONG).show();
                loadPreference.execute();
            }
        }
    }*/

    public void initialize() {
        LoadContacts loadContacts = new LoadContacts();
        loadContacts.execute();
        /*if (sharedpreferences.getAll().size() == 0) {
            LoadContacts loadContacts = new LoadContacts();
            loadContacts.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                User contact = new User(sharedpreferences.getString("name" + i, ""), sharedpreferences.getString("email" + i, ""), sharedpreferences.getString("idFacebook" + i, ""), false, false, 0.0, 0.0);

                contacts.add(contact);
            }
        }*/
    }

    public void acceptInvite(String origem, String aceite, String mensagemId) {
        String[] params = new String[3];

        params[0] = origem;      //o ID do usuário que enviou a solicitação
        params[1] = aceite;      //se for aceito o valor deve ser "true"
        params[2] = mensagemId;  // ID da mensagem enviada para a solicitação

        AcceptInvite acceptInvite = new AcceptInvite();
        acceptInvite.execute(params);
    }

    public void deleteContact(String contato) {
        String[] params = new String[3];

        params[0] = contato; //o ID do usuário que quer excluir

        DeleteContact deleteContact = new DeleteContact();
        deleteContact.execute(params);
    }

    public ArrayList<UserSearch> listContacts() { return contacts; }

    private class LoadContacts extends AsyncTask<Void, Void, UserSearch[]> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando contatos...");
        }

        @Override
        protected UserSearch[] doInBackground(Void... params) {
            UserSearch[] retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/contact/list");
                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, UserSearch[].class);

                content.close();
            } catch (Exception e) { }

            return retorno;
        }

        @Override
        protected void onPostExecute(UserSearch[] retorno) {
            if (retorno != null) {
                /*SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();

                int cont = 0;

                for (int i = 0; i < retorno.length; i++) {
                    editor.putString("name" + i, retorno[i].getNome());
                    editor.putString("email" + i, retorno[i].getEmail());
                    editor.putString("idFacebook" + i, retorno[i].getIdFacebook());
                    contacts.add(retorno[i]);

                    cont++;
                }

                editor.putString("update", dateFormat.format(today).toString());
                editor.putInt("size", cont);

                Toast.makeText(activity, "Contatos atualizados com sucesso.", Toast.LENGTH_LONG).show();

                editor.commit(); */

                for (int i = 0; i < retorno.length; i++) {
                    contacts.add(retorno[i]);
                }
            } else {
                Toast.makeText(activity, "Ocorreu um erro ao listar os contatos. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
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

    private class DeleteContact extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(String... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/contact/delete");

                values.add(new BasicNameValuePair("origem", params[0]));
                values.add(new BasicNameValuePair("destino", profile.getId()));
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
                Toast.makeText(activity, "Contato excluído com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
