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

    //private ArrayList<String> preferencesTemp = null;

    private ArrayList<Like> likeEntities;
    //private ArrayList<Like> chosenLikeEntities;

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
        //chosenLikeEntities = new ArrayList<Like>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Gostos Principais");

        View view = inflater.inflate(R.layout.fragment_like_chooser, container, false);

        ListView preferredLikesListView = (ListView) view.findViewById(R.id.preferred_likes_listview);
        final LikeChosenAdapter likesListViewAdapter = new LikeChosenAdapter(getContext(), likeEntities);
        preferredLikesListView.setAdapter(likesListViewAdapter);

        preferredLikesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView likeId = (TextView) view.findViewById(R.id.likeId);
                likesChosenCache.listPreferences().add(likeId.getText().toString());
                if (likesCache.listLikes().get(i).isSelecionada()) {
                    likesCache.listLikes().get(i).setSelecionada(false);
                    likesChosenCache.remove(likesCache.listLikes().get(i).getId());
                    Toast.makeText(getContext(), "Desmarcou", Toast.LENGTH_SHORT).show();
                } else {
                    likesCache.listLikes().get(i).setSelecionada(true);
                    likesChosenCache.listPreferences().add(likesCache.listLikes().get(i).getId());
                    Toast.makeText(getContext(), "Marcou", Toast.LENGTH_SHORT).show();
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

    private void createLikeList(View view) {
        //preferencesTemp = new ArrayList<String>(likesChosenCache.listPreferences());

        //Integer cont = null;
        //Boolean found = null;
        //boolean update = false;

        ListView preferredLikesListView = (ListView) view.findViewById(R.id.preferred_likes_listview);
        final LikeChosenAdapter likesListViewAdapter = new LikeChosenAdapter(getContext(), likeEntities);
        preferredLikesListView.setAdapter(likesListViewAdapter);

        preferredLikesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.preferred_like_checkbox);
                checkBox.setChecked(!checkBox.isChecked());
                TextView likeId = (TextView) view.findViewById(R.id.likeId);
                likesChosenCache.listPreferences().add(likeId.getText().toString());
                if (likesCache.listLikes().get(i).isSelecionada()) {
                    likesCache.listLikes().get(i).setSelecionada(false);
                    likesChosenCache.remove(likesCache.listLikes().get(i).getId());
                    Toast.makeText(getContext(), "Marcou", Toast.LENGTH_SHORT).show();
                } else {
                    likesCache.listLikes().get(i).setSelecionada(true);
                    likesChosenCache.listPreferences().add(likesCache.listLikes().get(i).getId());
                    Toast.makeText(getContext(), "Desmarcou", Toast.LENGTH_SHORT).show();
                }

                likesListViewAdapter.notifyDataSetChanged();
            }
        });

        /* final LikeAdapter likesChosenListViewAdapter = new LikeAdapter(getContext(), likeEntities);
        preferredLikesListView.setAdapter(likesChosenListViewAdapter);

        while (preferencesTemp.size() > 0) {
            cont = 0;
            found = false;

            while (cont < likeEntities.size()) {
                if (preferencesTemp.get(0).equals(likeEntities.get(cont).getId().toString())) {
                    chosenLikeEntities.add(new Like(likeEntities.get(cont)));
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

        likesChosenListViewAdapter.notifyDataSetChanged(); */
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
