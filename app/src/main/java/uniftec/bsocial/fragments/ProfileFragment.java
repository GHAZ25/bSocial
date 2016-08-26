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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import uniftec.bsocial.R;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private JSONObject jsonObjectLikes;
    private JSONObject jsonObjectInfo;

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

        getActivity().setTitle("Perfil");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        GraphRequest request = GraphRequest.newMeRequest(
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
        request.executeAsync();

        GraphRequest request2 = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        jsonObjectInfo = object;
                        setNameAgeLocation(object);
                    }
                });
        Bundle parameters2 = new Bundle();
        parameters2.putString("fields", "id,name,age_range.fields(min),hometown");
        request2.setParameters(parameters2);
        request2.executeAsync();

        getProfilePic();
        sendMsg();

        return view;
    }

    private void setNameAgeLocation(JSONObject object) {

        String name = object.optString("name");

        JSONObject object1 = object.optJSONObject("age_range");
        String age = object1.optString("min");

        String nameAge = name + ", " + age;

        TextView nameText = (TextView) getView().findViewById(R.id.nameAgeText);
        nameText.setText(nameAge);

        TextView hometown = (TextView) getView().findViewById(R.id.locationText);
        object1 = object.optJSONObject("hometown");
        hometown.setText(object1.optString("name"));
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

        jsonObject2 = jsonArray.optJSONObject(0);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like1 = (TextView) getView().findViewById(R.id.like1);
        like1.setText(jsonArray.optJSONObject(0).optString("name"));
        ImageView like1pic = (ImageView) getView().findViewById(R.id.like1pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like1pic);

        jsonObject2 = jsonArray.optJSONObject(1);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like2 = (TextView) getView().findViewById(R.id.like2);
        like2.setText(jsonArray.optJSONObject(1).optString("name"));
        ImageView like2pic = (ImageView) getView().findViewById(R.id.like2pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like2pic);

        jsonObject2 = jsonArray.optJSONObject(2);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like3 = (TextView) getView().findViewById(R.id.like3);
        like3.setText(jsonArray.optJSONObject(2).optString("name"));
        ImageView like3pic = (ImageView) getView().findViewById(R.id.like3pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like3pic);

        jsonObject2 = jsonArray.optJSONObject(3);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like4 = (TextView) getView().findViewById(R.id.like4);
        like4.setText(jsonArray.optJSONObject(3).optString("name"));
        ImageView like4pic = (ImageView) getView().findViewById(R.id.like4pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like4pic);

        jsonObject2 = jsonArray.optJSONObject(4);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like5 = (TextView) getView().findViewById(R.id.like5);
        like5.setText(jsonArray.optJSONObject(4).optString("name"));
        ImageView like5pic = (ImageView) getView().findViewById(R.id.like5pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like5pic);

        jsonObject2 = jsonArray.optJSONObject(5);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like6 = (TextView) getView().findViewById(R.id.like6);
        like6.setText(jsonArray.optJSONObject(5).optString("name"));
        ImageView like6pic = (ImageView) getView().findViewById(R.id.like6pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like6pic);

        jsonObject2 = jsonArray.optJSONObject(6);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like7 = (TextView) getView().findViewById(R.id.like7);
        like7.setText(jsonArray.optJSONObject(6).optString("name"));
        ImageView like7pic = (ImageView) getView().findViewById(R.id.like7pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like7pic);

        jsonObject2 = jsonArray.optJSONObject(7);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like8 = (TextView) getView().findViewById(R.id.like8);
        like8.setText(jsonArray.optJSONObject(7).optString("name"));
        ImageView like8pic = (ImageView) getView().findViewById(R.id.like8pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like8pic);

        jsonObject2 = jsonArray.optJSONObject(8);
        jsonObject2 = jsonObject2.optJSONObject("picture");
        jsonObject2 = jsonObject2.optJSONObject("data");
        TextView like9 = (TextView) getView().findViewById(R.id.like9);
        like9.setText(jsonArray.optJSONObject(8).optString("name"));
        ImageView like9pic = (ImageView) getView().findViewById(R.id.like9pic);
        Picasso.with(getContext()).load(jsonObject2.optString("url")).into(like9pic);

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

    /*public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }*/
}
