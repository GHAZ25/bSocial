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
import uniftec.bsocial.cache.UserCache;

public class SettingsFragment extends Fragment implements LikeChooserFragment.OnFragmentInteractionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Profile profile = null;
    private UserCache userCache = null;

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

        getActivity().setTitle(R.string.fragment_configs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userCache = new UserCache(getActivity());
        userCache.initialize();
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
                    userCache.getUser().setOculto(true);
                } else {
                    userCache.getUser().setOculto(false);
                }

                if (chkNotifica.isChecked()) {
                    userCache.getUser().setNotifica(true);
                } else {
                    userCache.getUser().setNotifica(false);
                }

                userCache.update();
            }
        });

        Button cancelPreference = (Button) view.findViewById(R.id.cancelPreference);
        cancelPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (userCache.getUser() != null) {
            if (userCache.getUser().isOculto()) {
                chkOculto.setChecked(true);
            }

            if (userCache.getUser().isNotifica()) {
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
                            if (userCache.getUser() != null) {
                                if (userCache.getUser().isOculto()) {
                                    chkOculto.setChecked(true);
                                }

                                if (userCache.getUser().isNotifica()) {
                                    chkNotifica.setChecked(true);
                                }
                            } else {
                                Toast.makeText(getActivity(), R.string.fragment_configs_error, Toast.LENGTH_LONG).show();
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
