package com.example.dsk221.firstapidemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.QuestionItem;
import com.example.dsk221.firstapidemo.models.SiteItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsk-221 on 24/3/17.
 */

public class SiteAdapter extends BaseAdapter {
    private Context context;
    private List<SiteItem> siteItems;

    public SiteAdapter(Context context) {
        this.siteItems = new ArrayList<>();
        this.context = context;
    }

    public void addItems(List<SiteItem> items) {
        siteItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return siteItems.size();
    }

    @Override
    public SiteItem getItem(int position) {
        return siteItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_site, parent,false);
            holder = new ViewHolder();
            holder.imageSite = (ImageView) convertView.findViewById(R.id.image_site);
            holder.textsiteName = (TextView) convertView.findViewById(R.id.text_site_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SiteItem siteItem=getItem(position);

        holder.textsiteName.setText(siteItem.getName());
        Picasso.with(context)
                .load(siteItem.getIconUrl())
                .placeholder(R.drawable.image_background)
                .into(holder.imageSite);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageSite;
        TextView textsiteName;
    }
}
