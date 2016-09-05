package uniftec.bsocial.fragments;

import android.content.Context;
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

import org.json.JSONObject;

import uniftec.bsocial.R;
import uniftec.bsocial.cache.LikesChosenCache;
import uniftec.bsocial.cache.LikesCache;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private LikesCache likesCache = null;
    private LikesChosenCache likesChosenCache = null;

    private OnFragmentInteractionListener mListener;

    private JSONObject jsonObject;

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

        getActivity().setTitle("Perfil");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

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
}
