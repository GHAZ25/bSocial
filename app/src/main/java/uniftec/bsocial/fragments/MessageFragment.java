package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uniftec.bsocial.R;
import uniftec.bsocial.cache.NotificationCache;
import uniftec.bsocial.cache.UserCache;

public class MessageFragment extends DialogFragment implements View.OnClickListener {
    public static final String USER_ID = "USER_ID";
    public static final String TYPE = "TYPE";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public View view;

    private String mParam1;
    private String mParam2;
    private String userId;
    private Button sendBtn;
    private Button inviteBtn;
    private EditText texto;

    private NotificationCache notificationCache = null;
    private UserCache userCache = null;
    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.fragment_message);

        userId = getArguments().getString(USER_ID);

        notificationCache = new NotificationCache(getActivity());
        notificationCache.initialize();

        userCache = new UserCache(getActivity());
        userCache.initialize();

        view = inflater.inflate(R.layout.fragment_message, container, false);

        sendBtn = (Button) view.findViewById(R.id.message_send);
        sendBtn.setOnClickListener(this);

        texto = (EditText) view.findViewById(R.id.message_msg);

        inviteBtn = (Button) view.findViewById(R.id.message_invite);
        inviteBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_send:
                send();
            break;
            case R.id.message_invite:
                invite();
            break;
        }
    }

    private void send() {
        if(texto.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), R.string.blank_text, Toast.LENGTH_SHORT).show();
        else {
            notificationCache.sendNotification(texto.getText().toString(), userCache.getUser().getNome(), userId);
            dismiss();
        }
    }

    private void invite() {
        if(texto.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), R.string.blank_text, Toast.LENGTH_SHORT).show();
        else {
            notificationCache.inviteNotification(texto.getText().toString(), userCache.getUser().getNome(), userId);
            dismiss();
        }
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
