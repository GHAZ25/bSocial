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

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.cache.NotificationCache;
import uniftec.bsocial.entities.Notification;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NotificationCache notificationCache;

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Notificações");

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationCache = new NotificationCache(getActivity());
        notificationCache.initialize();

        ArrayList<Notification> notifications = notificationCache.getNotifications();
//        ArrayList<Notification> notifs = new ArrayList<>();
//        Notification notif1 = new Notification("1", "Guilherme enviou uma mensagem", "msg");
//        Notification notif2 = new Notification("2", "Guilherme enviou uma mensagem", "msg");
//        Notification notif3 = new Notification("3", "Guilherme enviou uma mensagem", "msg");
//        notifs.add(notif1);
//        notifs.add(notif2);
//        notifs.add(notif3);
//
//        ListView notificationsListView = (ListView) view.findViewById(R.id.notifications_listview);
//        NotificationAdapter notificationsListViewAdapter = new NotificationAdapter(getContext(), notifs);
//        notificationsListView.setAdapter(notificationsListViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void respond() {
        new AlertDialog.Builder(getContext())
                .setTitle("Solicitação de contato")
                .setMessage("Deseja registrá-lo como um contato?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(R.mipmap.ic_contacts)
                .show();
    }

    private void sendMsg(FragmentManager manager) {
        Message2Fragment message2Fragment = new Message2Fragment();

        //Bundle args = new Bundle();
        //args.putString(Message2Fragment.USER_ID, user.getId());
        //message2Fragment.setArguments(args);

        message2Fragment.show(manager, "enviar_mensagem");
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
