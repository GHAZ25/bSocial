package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uniftec.bsocial.Like;
import uniftec.bsocial.LikeAdapter;
import uniftec.bsocial.R;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Like> likes;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        ListView likesListView = (ListView) getView().findViewById(R.id.likesListView);
        likesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView likePic = (ImageView) view.findViewById(R.id.likePic);
                TextView likeName = (TextView) view.findViewById(R.id.likeName);
                TextView likeId = (TextView) view.findViewById(R.id.likeId);

                TextView likeSelected1Name = (TextView) getView().findViewById(R.id.likeSelected1Text);
                TextView likeSelected1Id = (TextView) getView().findViewById(R.id.likeSelected1Id);

                TextView likeSelected2Name = (TextView) getView().findViewById(R.id.likeSelected2Text);
                TextView likeSelected2Id = (TextView) getView().findViewById(R.id.likeSelected2Id);

                TextView likeSelected3Name = (TextView) getView().findViewById(R.id.likeSelected3Text);
                TextView likeSelected3Id = (TextView) getView().findViewById(R.id.likeSelected3Id);

                TextView likeSelected4Name = (TextView) getView().findViewById(R.id.likeSelected4Text);
                TextView likeSelected4Id = (TextView) getView().findViewById(R.id.likeSelected4Id);

                TextView likeSelected5Name = (TextView) getView().findViewById(R.id.likeSelected5Text);
                TextView likeSelected5Id = (TextView) getView().findViewById(R.id.likeSelected5Id);

                TextView likeSelected6Name = (TextView) getView().findViewById(R.id.likeSelected6Text);
                TextView likeSelected6Id = (TextView) getView().findViewById(R.id.likeSelected6Id);

                TextView likeSelected7Name = (TextView) getView().findViewById(R.id.likeSelected7Text);
                TextView likeSelected7Id = (TextView) getView().findViewById(R.id.likeSelected7Id);

                TextView likeSelected8Name = (TextView) getView().findViewById(R.id.likeSelected8Text);
                TextView likeSelected8Id = (TextView) getView().findViewById(R.id.likeSelected8Id);

                TextView likeSelected9Name = (TextView) getView().findViewById(R.id.likeSelected9Text);
                TextView likeSelected9Id = (TextView) getView().findViewById(R.id.likeSelected9Id);

                int picId = 0;
                int nameId = 0;
                int idId = 0;
                int x = 0;

                if ((likeSelected1Id.getText().equals(likeId.getText())) || (likeSelected2Id.getText().equals(likeId.getText()))
                        || (likeSelected3Id.getText().equals(likeId.getText())) || (likeSelected7Id.getText().equals(likeId.getText()))
                        || (likeSelected4Id.getText().equals(likeId.getText())) || (likeSelected8Id.getText().equals(likeId.getText()))
                        || (likeSelected5Id.getText().equals(likeId.getText())) || (likeSelected9Id.getText().equals(likeId.getText()))
                        || (likeSelected6Id.getText().equals(likeId.getText()))) {
                    Toast toast = Toast.makeText(getContext(), "Você já adicionou essa preferência", Toast.LENGTH_SHORT);
                    toast.show();
                    x = 1;
                } else {

                    if (likeSelected1Name.getText().equals("Vazio")) {
                        picId = R.id.likeSelected1Pic;
                        nameId = R.id.likeSelected1Text;
                        idId = R.id.likeSelected1Id;
                    } else {
                        if (likeSelected2Name.getText().equals("Vazio")) {
                            picId = R.id.likeSelected2Pic;
                            nameId = R.id.likeSelected2Text;
                            idId = R.id.likeSelected2Id;
                        } else {
                            if (likeSelected3Name.getText().equals("Vazio")) {
                                picId = R.id.likeSelected3Pic;
                                nameId = R.id.likeSelected3Text;
                                idId = R.id.likeSelected3Id;
                            } else {
                                if (likeSelected4Name.getText().equals("Vazio")) {
                                    picId = R.id.likeSelected4Pic;
                                    nameId = R.id.likeSelected4Text;
                                    idId = R.id.likeSelected4Id;
                                } else {
                                    if (likeSelected5Name.getText().equals("Vazio")) {
                                        picId = R.id.likeSelected5Pic;
                                        nameId = R.id.likeSelected5Text;
                                        idId = R.id.likeSelected5Id;
                                    } else {
                                        if (likeSelected6Name.getText().equals("Vazio")) {
                                            picId = R.id.likeSelected6Pic;
                                            nameId = R.id.likeSelected6Text;
                                            idId = R.id.likeSelected6Id;
                                        } else {
                                            if (likeSelected7Name.getText().equals("Vazio")) {
                                                picId = R.id.likeSelected7Pic;
                                                nameId = R.id.likeSelected7Text;
                                                idId = R.id.likeSelected7Id;
                                            } else {
                                                if (likeSelected8Name.getText().equals("Vazio")) {
                                                    picId = R.id.likeSelected8Pic;
                                                    nameId = R.id.likeSelected8Text;
                                                    idId = R.id.likeSelected8Id;
                                                } else {
                                                    if (likeSelected9Name.getText().equals("Vazio")) {
                                                        picId = R.id.likeSelected9Pic;
                                                        nameId = R.id.likeSelected9Text;
                                                        idId = R.id.likeSelected9Id;
                                                    } else {
                                                        x = 1;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                if (x == 0) {
                    ImageView likeSelectedPic = (ImageView) getView().findViewById(picId);
                    TextView likeSelectedName = (TextView) getView().findViewById(nameId);
                    TextView likeSelectedId = (TextView) getView().findViewById(idId);

                    likeSelectedPic.setImageDrawable(likePic.getDrawable());
                    likeSelectedName.setText(likeName.getText());
                    likeSelectedId.setText(likeId.getText());
                }
            }
        });
        likesListView.setAdapter(new LikeAdapter(this, likes));
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

       /* SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_like_chooser, settingsFragment,
                settingsFragment.getTag()).commit();*/
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
