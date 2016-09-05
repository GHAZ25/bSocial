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
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.R;
import uniftec.bsocial.cache.PreferencesCache;

public class SettingsFragment extends Fragment implements LikeChooserFragment.OnFragmentInteractionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    private Profile profile = null;
    private PreferencesCache preferencesCache = null;

    private CheckBox chkOculto = null;
    private CheckBox chkNotifica = null;

    private OnFragmentInteractionListener mListener;

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        getActivity().setTitle("Configurações");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        preferencesCache = new PreferencesCache(getActivity());
        profile = Profile.getCurrentProfile();

        chkOculto = (CheckBox) view.findViewById(R.id.visibility);
        chkNotifica = (CheckBox) view.findViewById(R.id.notify);

        Button editPreferredLikes = (Button) view.findViewById(R.id.editPreferredLikes);
        editPreferredLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeChooserFragment likeChooserFragment = new LikeChooserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                likeChooserFragment.show(fragmentManager, "Gostos principais");
            }
        });

        Button editIgnoredCategories = (Button) view.findViewById(R.id.editIgnoredCategories);
        editIgnoredCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryChooserFragment categoryChooserFragment = new CategoryChooserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                categoryChooserFragment.show(fragmentManager, "Ignorar categorias");
            }
        });

        Button savePreference = (Button) view.findViewById(R.id.savePreference);
        savePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkOculto.isChecked()) {
                    preferencesCache.getUser().setOculto(true);
                } else {
                    preferencesCache.getUser().setOculto(false);
                }

                if (chkNotifica.isChecked()) {
                    preferencesCache.getUser().setNotifica(true);
                } else {
                    preferencesCache.getUser().setNotifica(false);
                }

                preferencesCache.update();
            }
        });

        Button cancelPreference = (Button) view.findViewById(R.id.cancelPreference);
        cancelPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (preferencesCache.getUser() != null) {
            if (preferencesCache.getUser().isOculto()) {
                chkOculto.setChecked(true);
            }

            if (preferencesCache.getUser().isNotifica()) {
                chkNotifica.setChecked(true);
            }
        } else {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            if (preferencesCache.getUser() != null) {
                                if (preferencesCache.getUser().isOculto()) {
                                    chkOculto.setChecked(true);
                                }

                                if (preferencesCache.getUser().isNotifica()) {
                                    chkNotifica.setChecked(true);
                                }
                            } else {
                                Toast.makeText(getActivity(), "É necessario estar conectado a internet para atualizar suas preferênias. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                            }

                            timer.cancel();
                            timer.purge();
                        }
                    });
                }
            }, 2000, 1000);
        }

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

    @Override
    public void onFragmentInteraction(Uri uri) { }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
