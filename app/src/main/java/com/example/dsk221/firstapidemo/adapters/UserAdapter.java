package com.example.dsk221.firstapidemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.BuzzItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends BaseAdapter implements Filterable {
    private Context context;
    public List<UserItem> userItems;
    private List<UserItem> tempUserItems;
    private ValueFilter valueFilter;

    public UserAdapter(Context context) {
        this.context = context;
        this.userItems = new ArrayList<>();
        this.tempUserItems = new ArrayList<>();
    }

    public void addItems(List<UserItem> items) {
        userItems.addAll(items);
        tempUserItems.addAll(items);
        notifyDataSetChanged();
    }
    public void removeItems(){
        userItems.clear();
        tempUserItems.clear();
    }

    @Override
    public int getCount() {
        return userItems.size();
    }

    @Override
    public UserItem getItem(int position) {
        return userItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user_detail, null);

            holder = new ViewHolder();
            holder.imageUser = (ImageView)convertView.findViewById(R.id.image_user);
            holder.textName = (TextView) convertView.findViewById(R.id.text_name);
            holder.textReputation = (TextView)convertView.findViewById(R.id.text_reputation);
            holder.textBronze = (TextView) convertView.findViewById(R.id.text_bronze);
            holder.textSilver = (TextView) convertView.findViewById(R.id.text_silver);
            holder.textGold = (TextView)convertView.findViewById(R.id.text_gold);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserItem userDetail = getItem(position);

        BuzzItem buzzDetail = userDetail.getBadgeCounts();

        Picasso.with(context)
                .load(userDetail.getProfileImage())
                .placeholder(R.drawable.image_background)
                .into(holder.imageUser);
        holder.textName.setText(userDetail.getDisplayName());
        holder.textReputation.setText(getRepString(userDetail.getReputation()));
        holder.textBronze.setText(String.valueOf(buzzDetail.getBronze()));
        holder.textSilver.setText(String.valueOf(buzzDetail.getSilver()));
        holder.textGold.setText(String.valueOf(buzzDetail.getGold()));

        return convertView;
    }

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<UserItem> filterList = new ArrayList<UserItem>();
                constraint = constraint.toString().toLowerCase();

                for (int i = 0; i < tempUserItems.size(); i++) {
                    UserItem userDetail = tempUserItems.get(i);
                    if ((userDetail.getDisplayName().toLowerCase()).contains(constraint.toString())) {
                        filterList.add(userDetail);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
                Log.e("VALUES", results.values.toString());
            } else {

                results.count = tempUserItems.size();
                results.values = tempUserItems;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userItems = (ArrayList<UserItem>) results.values;
            notifyDataSetChanged();
        }
    }


    private class ViewHolder {
        ImageView imageUser;
        TextView textName, textReputation, textBronze, textSilver, textGold;
    }

    public static String getRepString(int rep) {
        String repString;
        String r = String.valueOf(rep);

        if (rep < 1000) {
            repString = r;
        } else if (rep < 10000) {
            repString = r.charAt(0) + ',' + r.substring(1);
        } else {
            repString = (Math.round((rep / 1000) * 10) / 10) + "k";
        }

        return repString;
    }
}
