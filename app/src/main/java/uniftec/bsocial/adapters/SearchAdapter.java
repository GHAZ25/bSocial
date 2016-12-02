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
import uniftec.bsocial.entities.UserSearch;

/**
 * Created by mauri on 26/08/2016.
 */

public class SearchAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    ArrayList<UserSearch> result;
    Context context;

    public SearchAdapter(Context context, ArrayList<UserSearch> userEntities) {
        result = userEntities;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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

    public class Holder {
        ImageView userPic;
        TextView userName;
        TextView userId;
    }
}
