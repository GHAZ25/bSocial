package uniftec.bsocial.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.OtherUserMessageActivity;
import uniftec.bsocial.R;
import uniftec.bsocial.adapters.MessageAdapter;
import uniftec.bsocial.cache.MessageCache;
import uniftec.bsocial.entities.Message;

public class MessagesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<Message> messages = null;
    private MessageCache messageCache;
    private Timer timer = null;
    private MessageAdapter messagesListViewAdapter;

    private OnFragmentInteractionListener mListener;

    public MessagesFragment() { }

    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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

        getActivity().setTitle("Mensagens");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        timer = new Timer();

        messageCache = new MessageCache(getActivity());
        messageCache.initialize();

        messages = messageCache.listMessages();

        ListView messagesListView = (ListView) view.findViewById(R.id.messages_listview);
        messagesListViewAdapter = new MessageAdapter(getContext(), messages);
        messagesListView.setAdapter(messagesListViewAdapter);

        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = (Message) adapterView.getAdapter().getItem(i);
                String userId = message.getSentUserId();
                String userName = message.getSentUserName();
                Intent intent = new Intent(getActivity(), OtherUserMessageActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

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
                            if (messageCache.listMessages().size() != 0) {
                                messagesListViewAdapter.notifyDataSetChanged();
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
