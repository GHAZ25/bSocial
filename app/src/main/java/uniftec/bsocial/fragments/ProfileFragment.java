package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.cache.CategoriesCache;
import uniftec.bsocial.cache.LikesCache;
import uniftec.bsocial.cache.LikesChosenCache;
import uniftec.bsocial.cache.UserCache;
import uniftec.bsocial.entities.Like;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String[] INITIAL_PERMS = {
        android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    private String mParam1;
    private String mParam2;
    private View view;
    private LikesCache likesCache = null;
    private LikesChosenCache likesChosenCache = null;
    private CategoriesCache categoriesCache = null;
    private UserCache userCache = null;
    private ArrayList<Like> likes = null;
    private Timer timer = null;
    private LikeAdapter likeAdapter = null;

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

        requestPermissions(INITIAL_PERMS, 1337);

        getActivity().setTitle(R.string.fragment_profile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        timer = new Timer();

        likesCache = new LikesCache(getActivity());
        likesCache.verify();

        likesChosenCache = new LikesChosenCache(getActivity());
        likesChosenCache.initialize();

        categoriesCache = new CategoriesCache(getActivity());
        categoriesCache.verify();

        userCache = new UserCache(getActivity());
        userCache.initialize();

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        jsonObject = object;
                        setNameAgeLocation();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,birthday,hometown");
        parameters.putString("locale", "pt_BR");
        request.setParameters(parameters);
        request.executeAsync();

        getProfilePic();

        likes = likesCache.listLikes();

        ListView likesListView = (ListView) view.findViewById(R.id.likesListView);
        likeAdapter = new LikeAdapter(getContext(), likes);
        likesListView.setAdapter(likeAdapter);

        view.post(new Runnable() {
            @Override
            public void run() {
                testList();
            }
        });

        return view;
    }

    private void testList() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (likesCache.listLikes().size() != 0) {
                                likeAdapter.notifyDataSetChanged();
                                timer.cancel();
                            }
                        }
                    });
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000, 4000);
    }

    private void setNameAgeLocation() {
        String name = "";
        if (userCache.getUser() != null)
            name = userCache.getUser().getNome();
        else {
            name = jsonObject.optString("name");
        }

        String age = "-";

        if (jsonObject.optString("birthday") != null) {
            age = jsonObject.optString("birthday");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            int d = 4, m = 10, y = 1994;
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(format.parse(age));
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);
            } catch (Exception e) {

            }

            age = String.valueOf(getAge(y, m, d));
        }

        String nameAge = name + ", " + age;

        TextView nameText = (TextView) getView().findViewById(R.id.nameAgeText);
        nameText.setText(nameAge);

        TextView hometown = (TextView) getView().findViewById(R.id.locationText);

        if (jsonObject.optJSONObject("hometown") != null) {
            JSONObject object = jsonObject.optJSONObject("hometown");
            hometown.setText(object.optString("name"));
        } else {
            hometown.setText("-");
        }
    }

    public void getProfilePic() {
        Profile profile = Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePic);
        profilePictureView.setProfileId(profile.getId());
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

    public Integer getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d;
        Integer a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
