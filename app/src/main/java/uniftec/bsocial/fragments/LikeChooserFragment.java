package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.cache.LikesChosenCache;
import uniftec.bsocial.cache.LikesCache;
import uniftec.bsocial.entities.LikeEntity;

public class LikeChooserFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private LikesCache likesCache = null;
    private LikesChosenCache likesChosenCache = null;

    private ArrayList<String> preferencesTemp = null;

    private ArrayList<LikeEntity> likeEntities;
    private ArrayList<LikeEntity> chosenLikeEntities;

    private OnFragmentInteractionListener mListener;

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

        likesCache = new LikesCache(getActivity());
        likesCache.initialize();

        likesChosenCache = new LikesChosenCache(getActivity());
        likesChosenCache.initialize();

        chosenLikeEntities = new ArrayList<LikeEntity>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Gostos Principais");

        View view = inflater.inflate(R.layout.fragment_like_chooser, container, false);

        /*GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        createLikeList(object);

                        //GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                        //if(nextRequest != null){
                            //nextRequest.setCallback(nextRequest.getCallback());
                            //nextRequest.executeAndWait();
                        //}
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likeEntities.fields(id,name,picture.type(large))");
        request.setParameters(parameters);
        request.executeAsync();*/

        view.post(new Runnable() {
            @Override
            public void run() {
                createLikeList();
            }
        });

        return view;
    }

    private void createLikeList() {
        likeEntities = likesCache.listLikes();
        preferencesTemp = new ArrayList<String>(likesChosenCache.listPreferences());

        Integer cont = null;
        Boolean found = null;
        boolean update = false;

        ListView likesChosenListView = (ListView) getView().findViewById(R.id.likesChosenListView);
        final LikeAdapter likesChosenListViewAdapter = new LikeAdapter(this, chosenLikeEntities);
        likesChosenListView.setAdapter(likesChosenListViewAdapter);

        while (preferencesTemp.size() > 0) {
            cont = 0;
            found = false;

            while (cont < likeEntities.size()) {
                if (preferencesTemp.get(0).equals(likeEntities.get(cont).getId().toString())) {
                    chosenLikeEntities.add(new LikeEntity(likeEntities.get(cont)));
                    found = true;

                    preferencesTemp.remove(0);

                    cont = likeEntities.size();
                } else {
                    cont++;
                }
            }

            cont = 0;

            while (!found) {
                update = true;

                if (likesChosenCache.listPreferences().get(cont).equals(preferencesTemp.get(0))) {
                    likesChosenCache.listPreferences().remove(cont);
                    preferencesTemp.remove(0);

                    found = true;
                } else {
                    cont++;
                }
            }
        }

        preferencesTemp.clear();

        if (update) {
            likesChosenCache.update();
        }

        likesChosenListViewAdapter.notifyDataSetChanged();

        ListView likesListView = (ListView) getView().findViewById(R.id.likesListView);
        likesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ImageView likePic = (ImageView) view.findViewById(R.id.likePic);
                TextView likeName = (TextView) view.findViewById(R.id.likeName);
                TextView likeId = (TextView) view.findViewById(R.id.likeId);

                LikeEntity likeEntity = new LikeEntity(likeId.getText().toString(), likeName.getText().toString(), likePic.getTag().toString(), null);

                boolean add = true;
                for (LikeEntity likeEntity1 : chosenLikeEntities) {
                    if (likeEntity1.getId().equals(likeEntity.getId()))
                        add = false;
                }

                if (add) {
                    chosenLikeEntities.add(likeEntity);
                    likesChosenCache.listPreferences().add(likeEntity.getId());
                } else {
                    Toast.makeText(getContext(), "Item já adicionado!", Toast.LENGTH_SHORT).show();
                }

                likesChosenListViewAdapter.notifyDataSetChanged();
            }
        });

        likesChosenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int cont = 0;

                while (cont < chosenLikeEntities.size()) {
                    TextView likeId = (TextView) view.findViewById(R.id.likeId);

                    if (chosenLikeEntities.get(cont).getId().toString().equals(likeId.getText().toString())) {
                        chosenLikeEntities.remove(cont);
                        likesChosenCache.listPreferences().remove(cont);

                        cont = chosenLikeEntities.size();
                    } else {
                        cont++;
                    }
                }

                likesChosenListViewAdapter.notifyDataSetChanged();
            }
        });

        save();
        likesListView.setAdapter(new LikeAdapter(this, likeEntities));

    }

    private void save() {
        Button btnSave = (Button) getView().findViewById(R.id.btnSaveLikes);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesChosenCache.update();
            }
        });
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
}
