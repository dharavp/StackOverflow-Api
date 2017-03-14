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
import com.example.dsk221.firstapidemo.models.BuzzItem;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class QuestionDetailAdapter extends BaseAdapter {

    private Context context;
    private List<QuestionDetailItem> questionDetailItems;

    public QuestionDetailAdapter(Context context) {
        this.context = context;
        this.questionDetailItems = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return questionDetailItems.size();
    }

    @Override
    public QuestionDetailItem getItem(int position) {
        return questionDetailItems.get(position);
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
            holder.imageScore = (ImageView)convertView.findViewById(R.id.image_score);
            holder.imageAnswer = (ImageView)convertView.findViewById(R.id.image_answer);
            holder.textQuestionTitle=(TextView)convertView.findViewById(R.id.text_question_title);
            holder.textLastActivityDate=(TextView)convertView.findViewById(R.id.text_last_activity_date);
            holder.textTotalAnswer=(TextView)convertView.findViewById(R.id.text_total_answer);
            holder.textUserName=(TextView)convertView.findViewById(R.id.text_user_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textQuestionTitle.setText("gdjgh jfsdfgdf fkggsf");
        holder.textLastActivityDate.setText("dh fsfsjf ");
        holder.textUserName.setText("fdfdf dkf");
        holder.textTotalAnswer.setText(100+"");
        holder.textScore.setText(1+"");

        return convertView;
    }
    private class ViewHolder {
        ImageView imageScore,imageAnswer;
        TextView textScore, textTotalAnswer, textQuestionTitle, textTag,
                textLastActivityDate,textUserName;
    }

}
