package com.example.dsk221.firstapidemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.OwnerItem;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public void addItems(List<QuestionDetailItem> items) {
        questionDetailItems.addAll(items);
        notifyDataSetChanged();
    }
    public void removeItems(){
        questionDetailItems.clear();
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
            convertView = mInflater.inflate(R.layout.item_question_detail, null);

            holder = new ViewHolder();
            holder.imageScore = (ImageView)convertView.findViewById(R.id.image_score);
            holder.imageAnswer = (ImageView)convertView.findViewById(R.id.image_answer);
            holder.textQuestionTitle=(TextView)convertView.findViewById(R.id.text_question_title);
            holder.textLastActivityDate=(TextView)convertView.findViewById(R.id.text_last_activity_date);
            holder.textTotalAnswer=(TextView)convertView.findViewById(R.id.text_total_answer);
            holder.textUserName=(TextView)convertView.findViewById(R.id.text_user_name);
            holder.textScore=(TextView)convertView.findViewById(R.id.text_score);
            holder.linearLayoutScore=(LinearLayout)convertView.findViewById(R.id.linearlayout_score);
            holder.textTag=(TextView)convertView.findViewById(R.id.text_tag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionDetailItem questionDetailItem = getItem(position);
        OwnerItem ownerItem=questionDetailItem.getOwnerItem();

        ArrayList<String> listTag=questionDetailItem.getTags();
        holder.textTag.setText(Utils.arrayToString(listTag));

        int conditionColor;
        if(questionDetailItem.isAnswered()){
            conditionColor=Color.parseColor("#E5F9ED");
            holder.imageAnswer.setImageResource(R.drawable.ic_answer_green);
            holder.textTotalAnswer.setTextColor(Color.parseColor("#498E69"));
        }
        else{
            conditionColor=Color.parseColor("#F2F4F7");
            holder.imageAnswer.setImageResource(R.drawable.ic_answer_grey);
            holder.textTotalAnswer.setTextColor(Color.parseColor("#767F8E"));
        }
        holder.linearLayoutScore.setBackgroundColor(conditionColor);
        holder.textQuestionTitle.setText(Utils.convertHtmlInTxt(questionDetailItem.getTitle()));
        holder.textUserName.setText(ownerItem.getDisplayName());

        long lastActiveDate=(long)(questionDetailItem.getLastActivityDate());
        String lastActivity=Utils.getDate(lastActiveDate, Constants.DATE_FORMAT_LAST_ACTIVITY);
        holder.textLastActivityDate.setText(lastActivity);

        holder.textTotalAnswer.setText(String.valueOf(questionDetailItem.getAnswerCount()));

        String score=Utils.getScoreString(questionDetailItem.getScore());
        holder.textScore.setText(score);
        return convertView;
    }
    private class ViewHolder {
        ImageView imageScore,imageAnswer;
        TextView textScore, textTotalAnswer, textQuestionTitle, textTag,
                textLastActivityDate,textUserName;
        LinearLayout linearLayoutScore;
    }


}
