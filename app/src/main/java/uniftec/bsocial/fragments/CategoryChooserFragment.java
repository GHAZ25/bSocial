package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uniftec.bsocial.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryChooserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryChooserFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoryChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryChooserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryChooserFragment newInstance(String param1, String param2) {
        CategoryChooserFragment fragment = new CategoryChooserFragment();
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

        getDialog().setTitle("Ignorar Categorias");

        View view = inflater.inflate(R.layout.fragment_category_chooser, container, false);

        String[] categories = {"Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9", "Categoria1", "Categoria2", "Categoria3", "Categoria4", "Categoria5",
                "Categoria6", "Categoria7", "Categoria8", "Categoria9", "Categoria1", "Categoria2",
                "Categoria3", "Categoria4", "Categoria5", "Categoria6", "Categoria7", "Categoria8",
                "Categoria9"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.categories_list_view, categories);
        ListView categoriesListView = (ListView) view.findViewById(R.id.categoryChooserListView);
        categoriesListView.setItemsCanFocus(false);
        categoriesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        categoriesListView.setAdapter(arrayAdapter);

        // Inflate the layout for this fragment
        return view;
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
