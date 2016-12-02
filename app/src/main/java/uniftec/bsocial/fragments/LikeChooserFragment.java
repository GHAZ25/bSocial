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
import android.widget.Toast;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.LikeChosenAdapter;
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

    private ArrayList<Like> likeEntities;

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

        likeEntities = likesCache.listLikes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.fragment_preferred_likes);

        View view = inflater.inflate(R.layout.fragment_like_chooser, container, false);

        ListView preferredLikesListView = (ListView) view.findViewById(R.id.preferred_likes_listview);
        final LikeChosenAdapter likesListViewAdapter = new LikeChosenAdapter(getContext(), likeEntities);
        preferredLikesListView.setAdapter(likesListViewAdapter);

        preferredLikesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (likeEntities.get(i).isSelecionada()) {
                    likesChosenCache.remove(likesCache.listLikes().get(i).getId());
                    likeEntities.get(i).setSelecionada(false);
                } else {
                    likesChosenCache.listPreferences().add(likesCache.listLikes().get(i).getId());
                    likeEntities.get(i).setSelecionada(true);
                }

                likesListViewAdapter.notifyDataSetChanged();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveLikes);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesChosenCache.update();
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
}
