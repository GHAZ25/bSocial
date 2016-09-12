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

import uniftec.bsocial.entities.Category;

public class CategoriesCache {
    private FragmentActivity activity = null;
    private ProgressDialog load = null;
    private SharedPreferences sharedpreferences = null;
    private Profile profile = null;
    private String file = null;
    private ArrayList<Category> categories = null;

    public CategoriesCache(FragmentActivity activity) {
        super();

        this.activity = activity;

        profile = Profile.getCurrentProfile();
        file = "categories" + profile.getId();
        sharedpreferences = activity.getSharedPreferences(file, Context.MODE_PRIVATE);
        categories = new ArrayList<>();
    }

    public void initialize() {
        if (sharedpreferences.getAll().size() == 0) {
            LoadCategories loadCategories = new LoadCategories();
            loadCategories.execute();
        } else {
            for (int i = 0; i < sharedpreferences.getInt("size", 0); i++) {
                Category like = new Category(sharedpreferences.getString("name" + i, ""), sharedpreferences.getBoolean("select" + i, false));

                categories.add(like);
            }
        }
    }

    public ArrayList<Category> listCategories() { return categories; }

    private class LoadCategories extends AsyncTask<Void, Void, Category[]> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(activity, "Aguarde", "Buscando categorias...");
        }

        @Override
        protected Category[] doInBackground(Void... params) {
            Category[] retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/like/categories");
                values.add(new BasicNameValuePair("id_facebook", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, Category[].class);

                content.close();
            } catch (Exception e) { }

            return retorno;
        }

        @Override
        protected void onPostExecute(Category[] retorno) {
            if (retorno != null) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();

                int cont = 0;

                for (int i = 0; i < retorno.length; i++) {
                    editor.putString("name" + i, retorno[i].getNome());
                    editor.putBoolean("select" + i, retorno[i].isSelecionada());
                    categories.add(retorno[i]);

                    cont++;
                }

                editor.putInt("size", cont);

                Toast.makeText(activity, "Categorias atualizadas com sucesso.", Toast.LENGTH_LONG).show();

                editor.commit();
            } else {
                Toast.makeText(activity, "Ocorreu um erro ao listar as categorias. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
        }
    }
}
