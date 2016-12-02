package uniftec.bsocial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.entities.Message;

/**
 * Created by mauri on 26/08/2016.
 */

public class MessageAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    ArrayList<Message> result;
    Context context;

    public MessageAdapter(Context context, ArrayList<Message> userEntities) {
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
        rowView = inflater.inflate(R.layout.messages_list_view, null);
        holder.userId = (TextView) rowView.findViewById(R.id.userId);
        holder.userName = (TextView) rowView.findViewById(R.id.userName);
        holder.userPic = (ProfilePictureView) rowView.findViewById(R.id.userPic);
        holder.userMessage = (TextView) rowView.findViewById(R.id.user_message);

        holder.userId.setText(result.get(i).getSentUserId());
        holder.userName.setText(result.get(i).getSentUserName());
        //holder.userPic.setTag(result.get(i).getSentUserPicUrl());
        holder.userPic.setProfileId(result.get(i).getSentUserId());

        String msg = result.get(i).getMessage();
        msg = msg.replace(result.get(i).getSentUserName() + ": ", "");
        holder.userMessage.setText(msg);
        //Picasso.with(context).load(R.mipmap.ic_profile).into(holder.userPic);

        return rowView;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    public class Holder {
        ProfilePictureView userPic;
        TextView userName;
        TextView userId;
        TextView userMessage;
    }
}
