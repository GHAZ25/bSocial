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
import uniftec.bsocial.entities.Notification;
import uniftec.bsocial.entities.UserSearch;

/**
 * Created by mauri on 26/08/2016.
 */

public class NotificationAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    ArrayList<Notification> result;
    Context context;

    public NotificationAdapter(Context context, ArrayList<Notification> userEntities) {
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
        rowView = inflater.inflate(R.layout.notification_list_view, null);
        holder.notificationId = (TextView) rowView.findViewById(R.id.notification_id);
        holder.notificationText = (TextView) rowView.findViewById(R.id.notification_message);
        holder.notificationIcon = (ImageView) rowView.findViewById(R.id.notification_icon);
        holder.notificationId.setText(result.get(i).getId());
        holder.notificationText.setText(result.get(i).getMessage());
        if (result.get(i).getType().equals("convite"))
            Picasso.with(context).load(R.mipmap.ic_contacts).into(holder.notificationIcon);
        else
            Picasso.with(context).load(R.mipmap.ic_notification).into(holder.notificationIcon);

        return rowView;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    public class Holder {
        ImageView notificationIcon;
        TextView notificationText;
        TextView notificationId;
    }
}
