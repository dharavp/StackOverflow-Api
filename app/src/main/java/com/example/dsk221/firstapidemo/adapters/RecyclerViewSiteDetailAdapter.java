package com.example.dsk221.firstapidemo.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.SiteItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsk-221 on 28/3/17.
 */

public class RecyclerViewSiteDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private View view;
    private RecyclerView.ViewHolder viewHolder;
    private List<SiteItem> siteItems;
    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public interface OnLoadMoreListener {
        void loadItems();
    }

    public RecyclerViewSiteDetailAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;
        siteItems = new ArrayList<>();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (mOnLoadMoreListener != null) {
                                    mOnLoadMoreListener.loadItems();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return siteItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;

    }

    public void addItems(List<SiteItem> items) {
        siteItems.addAll(items);
        notifyDataSetChanged();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textLoading;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.more_progress);
            textLoading = (TextView) itemView.findViewById(R.id.text_loading);
        }
    }

    static class SiteViewHolder extends RecyclerView.ViewHolder {
        public TextView textNameSite, textAudience;
        public ImageView imageSiteList;

        public SiteViewHolder(View itemView) {
            super(itemView);
            textNameSite = (TextView) itemView.findViewById(R.id.text_name_site);
            textAudience = (TextView) itemView.findViewById(R.id.text_audience);
            imageSiteList = (ImageView) itemView.findViewById(R.id.image_site_list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        view = LayoutInflater.from(context).inflate(R.layout.item_site_detail,parent,false);
//        viewHolder = new ViewHolder(view);
//        return viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_site_detail, parent, false);
            return new SiteViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(context).inflate(R.layout.footer_layout, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SiteViewHolder) {
            SiteItem siteItem = siteItems.get(position);
            ((SiteViewHolder) holder).textNameSite.setText(siteItem.getName());
            ((SiteViewHolder) holder).textAudience.setText(siteItem.getAudience());
            Picasso.with(context)
                    .load(siteItem.getIconUrl())
                    .placeholder(R.drawable.image_background)
                    .into(((SiteViewHolder) holder).imageSiteList);
        } else {
            ((LoadingViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            ((LoadingViewHolder) holder).textLoading.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return siteItems.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void showProcessItem() {
        siteItems.add(null);
        notifyItemInserted(siteItems.size() - 1);
    }

    public void removeProcessItem() {
        //Remove loading item
        siteItems.remove(siteItems.size() - 1);
        notifyItemRemoved(siteItems.size());
    }
}
