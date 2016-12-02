package uniftec.bsocial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uniftec.bsocial.R;
import uniftec.bsocial.entities.Category;
import uniftec.bsocial.entities.Like;

public class CategoryAdapter extends BaseAdapter {
    ArrayList<Category> result;
    Context context;
    private static LayoutInflater inflater;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        result = categories;
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
        CheckedTextView likeName;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        Holder holder = new Holder();

        View rowView;
        rowView = inflater.inflate(R.layout.categories_list_view, null);
        holder.likeName = (CheckedTextView) rowView.findViewById(R.id.categoryIgnore);
        holder.likeName.setText(result.get(i).getNome());
        holder.likeName.setChecked(result.get(i).isSelecionada());

        return rowView;
    }

    @Override
    public int getCount() {
        return result.size();
    }
}
