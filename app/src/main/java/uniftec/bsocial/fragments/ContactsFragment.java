package uniftec.bsocial.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.OtherProfileActivity;
import uniftec.bsocial.R;
import uniftec.bsocial.adapters.ContactAdapter;
import uniftec.bsocial.cache.ContactsCache;
import uniftec.bsocial.entities.UserSearch;

public class ContactsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<UserSearch> contacts = null;
    private ContactsCache contactsCache = null;
    private ContactAdapter contactsListViewAdapter = null;
    private Timer timer = null;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment() { }

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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

        getActivity().setTitle(R.string.fragment_contacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactsCache = new ContactsCache(getActivity());
        contactsCache.initialize();

        contacts = contactsCache.listContacts();

        final ListView contactsListView = (ListView) view.findViewById(R.id.contacts_listview);
        contactsListViewAdapter = new ContactAdapter(getContext(), contacts);
        contactsListView.setAdapter(contactsListViewAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserSearch user = (UserSearch) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        contactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserSearch user = (UserSearch) adapterView.getAdapter().getItem(i);
                delete(user.getId());
                contactsListViewAdapter.notifyDataSetChanged();
                return false;
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

    private void delete(final String user) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_contact)
                .setMessage(R.string.delete_contact_msg)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contactsCache.deleteContact(user);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .setIcon(R.mipmap.ic_delete)
                .show();
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
                            if (contactsCache.listContacts().size() != 0) {
                                contactsListViewAdapter.notifyDataSetChanged();
                                timer.cancel();
                            }
                        }
                    });
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000, 1000);
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
