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

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends BaseAdapter {
    Context context;
    private String mpostType;
    public List<QuestionItem> questionItems;

    public QuestionAdapter(Context context) {
        this.context = context;
        this.questionItems = new ArrayList<>();
    }

    public void addItems(List<QuestionItem> items) {
        questionItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return questionItems.size();
    }

    @Override
    public QuestionItem getItem(int position) {
        return questionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_question, null);
            holder = new ViewHolder();
            holder.imagePostType = (ImageView) convertView.findViewById(R.id.image_post_type);
            holder.textPostTitle = (TextView) convertView.findViewById(R.id.text_post_title);
            holder.textPostType = (TextView) convertView.findViewById(R.id.text_post_type);
            holder.textPostDetail = (TextView) convertView.findViewById(R.id.text_post_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionItem questionItem = getItem(position);

        holder.textPostTitle.setText(questionItem.getTitle());
        mpostType = questionItem.getPostType();

        if(mpostType.equalsIgnoreCase("answer")){
            holder.textPostType.setText("Answer");
        }
        else {
            holder.textPostType.setText("Question");
        }

        holder.textPostDetail.setText(questionItem.getBody());

        return convertView;
    }

    private class ViewHolder {
        ImageView imagePostType;
        TextView textPostType, textPostTitle, textPostDetail;
    }
}
