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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.LikeAdapter;
import uniftec.bsocial.cache.LikesCache;
import uniftec.bsocial.cache.LikesChosenCache;
import uniftec.bsocial.entities.Like;

public class LikeChooserFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private LikesCache likesCache = null;
    private LikesChosenCache likesChosenCache = null;

    private ArrayList<String> preferencesTemp = null;

    private Like[] likeEntities;
    private ArrayList<Like> chosenLikeEntities;

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

        chosenLikeEntities = new ArrayList<Like>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Gostos Principais");

        View view = inflater.inflate(R.layout.fragment_like_chooser, container, false);

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

        ListView preferredLikesListView = (ListView) getView().findViewById(R.id.preferred_likes_listview);

        preferredLikesListView.setAdapter(new LikeAdapter(getContext(), likeEntities));
        final LikeAdapter likesChosenListViewAdapter = new LikeAdapter(getContext(), likeEntities);
        preferredLikesListView.setAdapter(likesChosenListViewAdapter);

        while (preferencesTemp.size() > 0) {
            cont = 0;
            found = false;

            while (cont < likeEntities.length) {
                if (preferencesTemp.get(0).equals(likeEntities[cont].getId().toString())) {
                    chosenLikeEntities.add(new Like(likeEntities[cont]));
                    found = true;

                    preferencesTemp.remove(0);

                    cont = likeEntities.length;
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

        preferredLikesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.preferred_like_checkbox);
                checkBox.callOnClick();
                TextView likeId = (TextView) view.findViewById(R.id.likeId);
                likesChosenCache.listPreferences().add(likeId.getText().toString());
            }
        });

        save();
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
