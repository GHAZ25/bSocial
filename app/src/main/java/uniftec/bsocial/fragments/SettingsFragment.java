package uniftec.bsocial.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import uniftec.bsocial.R;
import uniftec.bsocial.domain.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements LikeChooserFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressDialog load;

    private Profile profile = null;

    private CheckBox chkOculto = null;
    private CheckBox chkNotifica = null;

    private String oculto = "false";
    private String notifica = "false";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        getActivity().setTitle("Configurações");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        profile = Profile.getCurrentProfile();

        chkOculto = (CheckBox) v.findViewById(R.id.visibility);
        chkNotifica = (CheckBox) v.findViewById(R.id.notify);

        ListPreferences preferences = new ListPreferences();
        preferences.execute();

        Button editPreferredLikes = (Button) v.findViewById(R.id.editPreferredLikes);
        editPreferredLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeChooserFragment likeChooserFragment = new LikeChooserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                likeChooserFragment.show(fragmentManager, "Gostos principais");
//                fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, likeChooserFragment,
//                likeChooserFragment.getTag()).commit();
            }
        });

        Button editIgnoredCategories = (Button) v.findViewById(R.id.editIgnoredCategories);
        editIgnoredCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryChooserFragment categoryChooserFragment = new CategoryChooserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                categoryChooserFragment.show(fragmentManager, "Ignorar categorias");
            }
        });

        Button savePreference = (Button) v.findViewById(R.id.savePreference);
        savePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkOculto.isChecked()) {
                    oculto = "true";
                } else {
                    oculto = "false";
                }

                if (chkNotifica.isChecked()) {
                    notifica = "true";
                } else {
                    notifica = "false";
                }

                UpdatePreferences update = new UpdatePreferences();
                update.execute();
            }
        });

        Button cancelPreference = (Button) v.findViewById(R.id.cancelPreference);
        cancelPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class UpdatePreferences extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getView().getContext(), "Aguarde", "Alterando preferências...");
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/user/update");

                values.add(new BasicNameValuePair("oculto", oculto));
                values.add(new BasicNameValuePair("notifica", notifica));
                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, HashMap.class);

                content.close();

                return "true";
            }catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                switch (message) {
                    case "true":
                        Toast.makeText(getView().getContext(), "Preferências alteradas com sucesso.", Toast.LENGTH_LONG).show();
                    break;
                    default:
                        Toast.makeText(getView().getContext(), "Ocorreu um erro ao alterar as preferências. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }

    private class ListPreferences extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){ }

        @Override
        protected String doInBackground(Void... params) {
            User retorno = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = null;

                List<NameValuePair> values = new ArrayList<>(2);

                request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/user/preference");
                values.add(new BasicNameValuePair("id", profile.getId()));
                request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));

                HttpResponse response = httpclient.execute(request);
                InputStream content = response.getEntity().getContent();
                Reader reader = new InputStreamReader(content);

                Gson gson = new Gson();
                retorno = gson.fromJson(reader, User.class);

                content.close();

                if (retorno.isOculto()) {
                    oculto = "true";
                }

                if (retorno.isNotifica()) {
                    notifica = "true";
                }
                return "true";
            }catch (Exception e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message){
            if (message != null) {
                if(!message.equals("true")) {
                    Toast.makeText(getView().getContext(), "Ocorreu um erro ao carregar suas preferências.", Toast.LENGTH_LONG).show();
                } else {
                    if (oculto.equals("true")) {
                        chkOculto.setChecked(true);
                    }

                    if (notifica.equals("true")) {
                        chkNotifica.setChecked(true);
                    }
                }
            }
        }
    }
}
