package uniftec.bsocial.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.NotificationAdapter;
import uniftec.bsocial.cache.NotificationCache;
import uniftec.bsocial.entities.Notification;

public class NotificationsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<Notification> notifications = null;
    private NotificationCache notificationCache;
    private NotificationAdapter notificationsListViewAdapter = null;
    private Timer timer = null;

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() { }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        getActivity().setTitle("Convites");

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationCache = new NotificationCache(getActivity(), "convite", "");
        notificationCache.initialize();

        notifications = notificationCache.listNotifications();

        final ListView notificationsListView = (ListView) view.findViewById(R.id.notifications_listview);
        notificationsListViewAdapter = new NotificationAdapter(getContext(), notifications);
        notificationsListView.setAdapter(notificationsListViewAdapter);

        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notification notification = (Notification) adapterView.getAdapter().getItem(i);
                respond(notification);
                notificationsListViewAdapter.notifyDataSetChanged();
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
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (notificationCache.listNotifications().size() != 0) {
                                notificationsListViewAdapter.notifyDataSetChanged();
                                timer.cancel();
                                //update();
                            }
                        }
                    });
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000, 500);
    }

    /* private void update() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationCache.updateNotifications();
                            notificationsListViewAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 5000, 5000);
    } */

    private void respond(final Notification notification) {
        new AlertDialog.Builder(getContext())
                .setTitle("Solicitação de contato")
                .setMessage("Deseja registrá-lo como um contato?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notificationCache.acceptInvite(notification.getId(), "true", notification.getMessageId());
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notificationCache.acceptInvite(notification.getId(), "false", notification.getMessageId());
                    }
                })
                .setIcon(R.mipmap.ic_contacts)
                .show();
    }

    private void sendMsg(Notification notification) {
        FragmentManager manager = getFragmentManager();
        Message2Fragment message2Fragment = new Message2Fragment();

        Bundle args = new Bundle();
        args.putString(Message2Fragment.USER_ID, notification.getId());
        args.putString(Message2Fragment.USER_MSG, notification.getMessage());
        message2Fragment.setArguments(args);

        message2Fragment.show(manager, "enviar_mensagem");
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
