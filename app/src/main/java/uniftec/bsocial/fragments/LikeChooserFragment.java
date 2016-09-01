package uniftec.bsocial.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.domain.Preference;
import uniftec.bsocial.entities.Like;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LikeChooserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikeChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeChooserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Preference[] retorno = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Profile profile = null;

    private JSONObject objectTemp;

    private ArrayList<Like> likes;
    private ArrayList<Like> chosenLikes;

    private ProgressDialog load;

    private OnFragmentInteractionListener mListener;

    private String[] id = new String[9];

    private TextView likeSelected1Name = null;
    private TextView likeSelected1Id = null;
    private ImageView likeSelected1Pic = null;

    private TextView likeSelected2Name = null;
    private TextView likeSelected2Id = null;
    private ImageView likeSelected2Pic = null;

    private TextView likeSelected3Name = null;
    private TextView likeSelected3Id = null;
    private ImageView likeSelected3Pic = null;

    private TextView likeSelected4Name = null;
    private TextView likeSelected4Id = null;
    private ImageView likeSelected4Pic = null;

    private TextView likeSelected5Name = null;
    private TextView likeSelected5Id = null;
    private ImageView likeSelected5Pic = null;

    private TextView likeSelected6Name = null;
    private TextView likeSelected6Id = null;
    private ImageView likeSelected6Pic = null;

    private TextView likeSelected7Name = null;
    private TextView likeSelected7Id = null;
    private ImageView likeSelected7Pic = null;

    private TextView likeSelected8Name = null;
    private TextView likeSelected8Id = null;
    private ImageView likeSelected8Pic = null;

    private TextView likeSelected9Name = null;
    private TextView likeSelected9Id = null;
    private ImageView likeSelected9Pic = null;

    public LikeChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikeChooserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikeChooserFragment newInstance(String param1, String param2) {
        LikeChooserFragment fragment = new LikeChooserFragment();
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

        chosenLikes = new ArrayList<Like>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        createLikeList(object);

                        /*GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                        if(nextRequest != null){
                            nextRequest.setCallback(nextRequest.getCallback());
                            nextRequest.executeAndWait();
                        }*/
                    }
                });

        profile = Profile.getCurrentProfile();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes.fields(id,name,picture.type(large))");
        request.setParameters(parameters);
        request.executeAsync();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_chooser, container, false);
    }

    private void createLikeList(JSONObject object) {
        likes = new ArrayList<>();

        JSONObject jsonObject2 = object.optJSONObject("likes");
        JSONArray jsonArray = jsonObject2.optJSONArray("data");

        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject jsonObject3 = jsonArray.optJSONObject(i);
            String id = jsonObject3.optString("id");
            String name = jsonObject3.optString("name");

            jsonObject3 = jsonObject3.optJSONObject("picture");
            jsonObject3 = jsonObject3.optJSONObject("data");
            String pictureUrl = jsonObject3.optString("url");

            Like like = new Like();
            like.setId(id);
            like.setName(name);
            like.setPictureUrl(pictureUrl);

            likes.add(like);
        }

        /*likeSelected1Name = (TextView) getView().findViewById(R.id.likeSelected1Text);
        likeSelected1Id = (TextView) getView().findViewById(R.id.likeSelected1Id);
        likeSelected1Pic = (ImageView) getView().findViewById(R.id.likeSelected1Pic);

        likeSelected2Name = (TextView) getView().findViewById(R.id.likeSelected2Text);
        likeSelected2Id = (TextView) getView().findViewById(R.id.likeSelected2Id);
        likeSelected2Pic = (ImageView) getView().findViewById(R.id.likeSelected2Pic);

        likeSelected3Name = (TextView) getView().findViewById(R.id.likeSelected3Text);
        likeSelected3Id = (TextView) getView().findViewById(R.id.likeSelected3Id);
        likeSelected3Pic = (ImageView) getView().findViewById(R.id.likeSelected3Pic);

        likeSelected4Name = (TextView) getView().findViewById(R.id.likeSelected4Text);
        likeSelected4Id = (TextView) getView().findViewById(R.id.likeSelected4Id);
        likeSelected4Pic = (ImageView) getView().findViewById(R.id.likeSelected4Pic);

        likeSelected5Name = (TextView) getView().findViewById(R.id.likeSelected5Text);
        likeSelected5Id = (TextView) getView().findViewById(R.id.likeSelected5Id);
        likeSelected5Pic = (ImageView) getView().findViewById(R.id.likeSelected5Pic);

        likeSelected6Name = (TextView) getView().findViewById(R.id.likeSelected6Text);
        likeSelected6Id = (TextView) getView().findViewById(R.id.likeSelected6Id);
        likeSelected6Pic = (ImageView) getView().findViewById(R.id.likeSelected6Pic);

        likeSelected7Name = (TextView) getView().findViewById(R.id.likeSelected7Text);
        likeSelected7Id = (TextView) getView().findViewById(R.id.likeSelected7Id);
        likeSelected7Pic = (ImageView) getView().findViewById(R.id.likeSelected7Pic);

        likeSelected8Name = (TextView) getView().findViewById(R.id.likeSelected8Text);
        likeSelected8Id = (TextView) getView().findViewById(R.id.likeSelected8Id);
        likeSelected8Pic = (ImageView) getView().findViewById(R.id.likeSelected8Pic);

        likeSelected9Name = (TextView) getView().findViewById(R.id.likeSelected9Text);
        likeSelected9Id = (TextView) getView().findViewById(R.id.likeSelected9Id);
        likeSelected9Pic = (ImageView) getView().findViewById(R.id.likeSelected9Pic);*/


        ListPreferences list = new ListPreferences();
        list.execute();

        ListView likesChosenListView = (ListView) getView().findViewById(R.id.likesChosenListView);
        final LikeAdapter likesChosenListViewAdapter = new LikeAdapter(this, chosenLikes);
        likesChosenListView.setAdapter(likesChosenListViewAdapter);

        ListView likesListView = (ListView) getView().findViewById(R.id.likesListView);
        likesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ImageView likePic = (ImageView) view.findViewById(R.id.likePic);
                TextView likeName = (TextView) view.findViewById(R.id.likeName);
                TextView likeId = (TextView) view.findViewById(R.id.likeId);

                Like like = new Like();
                like.setId(likeId.getText().toString());
                like.setName(likeName.getText().toString());
                like.setPictureUrl(likePic.getTag().toString());

                boolean add = true;
                for (Like like1: chosenLikes) {
                    if (like1.getId().equals(like.getId()))
                        add = false;
                }

                if (add) {
                    chosenLikes.add(like);
                } else {
                    Toast.makeText(getContext(), "Item já adicionado!", Toast.LENGTH_SHORT).show();
                }

                likesChosenListViewAdapter.notifyDataSetChanged();
            }
        });

        save();
        likesListView.setAdapter(new LikeAdapter(this, likes));

    }


    private void save() {
        Button btnSave = (Button) getView().findViewById(R.id.btnSaveLikes);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SavePreferences save = new SavePreferences();
                save.execute();
            }
        });
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

    private class SavePreferences extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getView().getContext(), "Aguarde", "Alterando preferências...");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                for (int i = 0; i < 9; i++) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost request = null;

                    List<NameValuePair> values = new ArrayList<>(2);

                    if (id[i].equals("")) {
                        request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/remove");
                    } else {
                        request = new HttpPost("http://ec2-54-213-36-149.us-west-2.compute.amazonaws.com:8080/ws/rest/preference/update");
                        values.add(new BasicNameValuePair("id_preferencia", id[i]));
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
                        Toast.makeText(getView().getContext(), "Preferências alteradas com sucesso.", Toast.LENGTH_LONG).show();
                    break;
                    default:
                        Toast.makeText(getView().getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
            load.dismiss();
        }
    }

    private class ListPreferences extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getView().getContext(), "Aguarde", "Buscando preferências...");
        }

        @Override
        protected String doInBackground(Void... params) {
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
                        load.dismiss();

                        if (retorno.length > 0) {
                            LoadPreference preference = new LoadPreference();
                            preference.execute(0);
                        }
                    break;
                    default:
                        load.dismiss();
                        Toast.makeText(getView().getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class LoadPreference extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getView().getContext(), "Aguarde", "Carregando...");
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            final int temp = params[0];
            final String idTemp = retorno[params[0]].getId();

            GraphRequest request1 = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), retorno[params[0]].getId() + "?fields=id,name,picture", new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    JSONObject object = graphResponse.getJSONObject();

                    objectTemp = object.optJSONObject("picture");
                    objectTemp = objectTemp.optJSONObject("data");
                }
            });

            request1.executeAsync();

            return params[0];
        }

        @Override
        protected void onPostExecute(Integer message){
            if (message != null) {
                load.dismiss();

                if (message < (retorno.length - 1)) {
                    LoadPreference preference = new LoadPreference();
                    preference.execute((message + 1));
                }
            } else {
                load.dismiss();
                Toast.makeText(getView().getContext(), "Ocorreu um erro ao carreagar as preferências. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
