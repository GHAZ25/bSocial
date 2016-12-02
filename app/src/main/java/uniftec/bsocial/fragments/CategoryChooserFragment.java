package uniftec.bsocial.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import uniftec.bsocial.R;
import uniftec.bsocial.adapters.CategoryAdapter;
import uniftec.bsocial.cache.CategoriesCache;

public class CategoryChooserFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CategoriesCache categoriesCache = null;

    private OnFragmentInteractionListener mListener;

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

        categoriesCache = new CategoriesCache(getActivity());
        categoriesCache.initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.fragment_ignore_categories);

        View view = inflater.inflate(R.layout.fragment_category_chooser, container, false);

        ListView categoriesListView = (ListView) view.findViewById(R.id.categoryChooserListView);
        final CategoryAdapter categoriesListViewAdapter = new CategoryAdapter(getContext(), categoriesCache.listCategories());
        categoriesListView.setAdapter(categoriesListViewAdapter);

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (categoriesCache.listCategories().get(i).isSelecionada()) {
                    categoriesCache.listCategories().get(i).setSelecionada(false);
                } else {
                    categoriesCache.listCategories().get(i).setSelecionada(true);
                }
                categoriesListViewAdapter.notifyDataSetChanged();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveCategories);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesCache.update();
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
