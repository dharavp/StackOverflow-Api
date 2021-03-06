package com.example.dsk221.firstapidemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.QuestionItem;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.HtmlImageGetter;
import com.example.dsk221.firstapidemo.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends BaseAdapter {
    private Context context;
    private List<QuestionItem> questionItems;


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

        final ViewHolder holder;
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

        Spanned spanned = Utils.convertHtmlInTxt(questionItem.getTitle());
        holder.textPostTitle.setText(spanned);
        String mPostType = questionItem.getPostType();

        if (mPostType.equalsIgnoreCase(Constants.POST_TYPE_ANSWER)) {
            holder.textPostType.setText(R.string.post_type_answer_text);
            holder.imagePostType.setImageResource(R.drawable.ic_answer_grey);
        } else if (mPostType.equalsIgnoreCase(Constants.POST_TYPE_QUESTION)) {
            holder.textPostType.setText(R.string.post_type_question_text);
            holder.imagePostType.setImageResource(R.drawable.ic_question_grey);
        }

        HtmlImageGetter imageGetter = new HtmlImageGetter(context, R.drawable.image_background) {
            @Override
            public void onTextUpdate() {
                CharSequence sequence = holder.textPostDetail.getText();
                holder.textPostDetail.setText(sequence);
            }
        };
        Spanned spannedBody = Utils.convertHtmlInTxt(questionItem.getBody(), imageGetter);
        holder.textPostDetail.setText(spannedBody);

        return convertView;
    }

    private class ViewHolder {
        ImageView imagePostType;
        TextView textPostType, textPostTitle, textPostDetail;
    }
}
