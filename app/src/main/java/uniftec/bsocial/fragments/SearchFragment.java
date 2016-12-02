package uniftec.bsocial.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import uniftec.bsocial.OtherProfileActivity;
import uniftec.bsocial.R;
import uniftec.bsocial.adapters.SearchAdapter;
import uniftec.bsocial.entities.UserSearch;
import uniftec.bsocial.entities.messages.MessageUserSearch;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ProgressDialog load = null;
    private Profile profile = null;

    private SearchAdapter usersListViewAdapter = null;
    private ArrayList<UserSearch> users = null;

    private OnFragmentInteractionListener mListener;

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        profile = Profile.getCurrentProfile();

        users = new ArrayList<>();
        ListUsers listUsers = new ListUsers();
        listUsers.execute();

        getActivity().setTitle("Busca");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        final ListView usersListView = (ListView) view.findViewById(R.id.usersListView);
        usersListViewAdapter = new SearchAdapter(getContext(), users);
        usersListView.setAdapter(usersListViewAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserSearch user = (UserSearch) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class ListUsers extends AsyncTask<Void, Void, MessageUserSearch> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getActivity(), "Aguarde", "Buscando usuários...");
        }

        @Override
        protected MessageUserSearch doInBackground(Void... params) {
            MessageUserSearch retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com:8080/ws/rest/user/list");
                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, MessageUserSearch.class);

                content.close();
            } catch (Exception e) { }

            return retorno;
        }

        @Override
        protected void onPostExecute(MessageUserSearch retorno) {
            if (retorno != null) {
                if (retorno.getMessage().equals("true")) {
                    for (int i = 0; i < retorno.getUsers().size(); i++) {
                        final UserSearch user = new UserSearch(retorno.getUsers().get(i));

                        users.add(user);
                    }
                    usersListViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), retorno.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Não foi possível concluir a busca de usuários. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }

            load.dismiss();
        }
    }
}
