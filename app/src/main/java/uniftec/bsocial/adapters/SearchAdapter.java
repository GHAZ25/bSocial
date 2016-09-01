package uniftec.bsocial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.entities.User;
import uniftec.bsocial.fragments.SearchFragment;

/**
 * Created by mauri on 26/08/2016.
 */

public class SearchAdapter extends BaseAdapter {
    ArrayList<User> result;
    Context context;
    private static LayoutInflater inflater;

    public SearchAdapter(SearchFragment searchFragment, ArrayList<User> users) {
        result = users;
        context = searchFragment.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
        ImageView userPic;
        TextView userName;
        TextView userId;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
            rowView = inflater.inflate(R.layout.search_list_view, null);
            holder.userId = (TextView) rowView.findViewById(R.id.userId);
            holder.userName = (TextView) rowView.findViewById(R.id.userName);
            holder.userPic = (ImageView) rowView.findViewById(R.id.userPic);
            holder.userId.setText(result.get(i).getId());
            holder.userName.setText(result.get(i).getName());
            holder.userPic.setTag(result.get(i).getPictureUrl());
            Picasso.with(context).load(result.get(i).getPictureUrl()).into(holder.userPic);

        return rowView;
    }

    @Override
    public int getCount() {
        return result.size();
    }
}
