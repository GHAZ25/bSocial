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
import uniftec.bsocial.entities.Like;
import uniftec.bsocial.fragments.LikeChooserFragment;
import uniftec.bsocial.fragments.ProfileFragment;

/**
 * Created by mauri on 26/08/2016.
 */

public class LikeAdapter extends BaseAdapter {
    Like[] result;
    Context context;
    private static LayoutInflater inflater;

    public LikeAdapter(Context context, Like[] likes) {
        result = likes;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LikeAdapter(Context context, ArrayList<Like> likes) {
        result = new Like[likes.size()];

        for (int i = 0; i < likes.size(); i++) {
            result[i] = new Like(likes.get(i));
        }

        this.context = context;
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
        ImageView likePic;
        TextView likeName;
        TextView likeId;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
            rowView = inflater.inflate(R.layout.likes_list_view, null);
            holder.likeId = (TextView) rowView.findViewById(R.id.likeId);
            holder.likeName = (TextView) rowView.findViewById(R.id.likeName);
            holder.likePic = (ImageView) rowView.findViewById(R.id.likePic);
            holder.likeId.setText(result[i].getId());
            holder.likeName.setText(result[i].getName());
            holder.likePic.setTag(result[i].getPictureUrl());
            Picasso.with(context).load(result[i].getPictureUrl()).into(holder.likePic);

        return rowView;
    }

    @Override
    public int getCount() {
        return result.length;
    }
}
