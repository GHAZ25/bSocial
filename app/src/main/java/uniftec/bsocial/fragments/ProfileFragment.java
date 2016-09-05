package uniftec.bsocial.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import uniftec.bsocial.R;
import uniftec.bsocial.cache.LikesChosenCache;
import uniftec.bsocial.cache.LikesCache;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private LikesCache likesCache = null;
    private LikesChosenCache likesChosenCache = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String profile = "Profile";
    SharedPreferences sharedpreferences;

    private OnFragmentInteractionListener mListener;

    private JSONObject jsonObject;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        likesCache = new LikesCache(getActivity());
        likesCache.update();

        likesChosenCache = new LikesChosenCache(getActivity());
        /* String d1 = "01/01/2000";
        String d2 = "01/02/1999";

        try {
            Date date1 = format.parse(d1);
            Date date2 = format.parse(d2);

            if (date1.after(date2)) {
                Toast.makeText(getActivity(), "Atualiza",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Não atualiza",Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
        }

        sharedpreferences = getActivity().getSharedPreferences(profile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        //editor.putString(Name, "Nome 2");
        //editor.putString(Phone, "Fone 2");
        //editor.putString(Email, "Mail 2");

        if ((sharedpreferences.getAll().size() == 0) || (!sharedpreferences.getString("atualizacao", "").equals("erro"))) {

        }

        editor.commit(); */

        /* SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPrefsFile", 0).edit();
        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.commit();

        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefsFile", 0);
        String restoredText = prefs.getString("text", null);

        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.
            Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Arquivo inexistente", Toast.LENGTH_LONG).show();
        } */

        getActivity().setTitle("Perfil");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        /*GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        jsonObjectLikes = object;
                        getLikes(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes.fields(id,name,picture.type(large)).limit(9)");
        request.setParameters(parameters);
        request.executeAsync();*/

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        jsonObject = object;
                        setNameAgeLocation();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,age_range.fields(min),hometown");
        request.setParameters(parameters);
        request.executeAsync();

        getProfilePic();
        sendMsg();
        return view;
    }

    private void setNameAgeLocation() {

        String name = jsonObject.optString("name");

        JSONObject object = jsonObject.optJSONObject("age_range");
        String age = object.optString("min");

        String nameAge = name + ", " + age;

        TextView nameText = (TextView) getView().findViewById(R.id.nameAgeText);
        nameText.setText(nameAge);

        TextView hometown = (TextView) getView().findViewById(R.id.locationText);
        object = jsonObject.optJSONObject("hometown");
        hometown.setText(object.optString("name"));
    }

    public void getProfilePic() {
        Profile profile = Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePic);
        profilePictureView.setProfileId(profile.getId());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getLikes(JSONObject object) {
        JSONObject jsonObject2 = object.optJSONObject("likes");
        JSONArray jsonArray = jsonObject2.optJSONArray("data");
    }

    public void sendMsg() {
        Button sendMsgBtn = (Button) view.findViewById(R.id.sendMsg);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                MessageFragment messageFragment = new MessageFragment();
                messageFragment.show(fragmentManager, "Enviar mensagem");
            }
        });
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
}
