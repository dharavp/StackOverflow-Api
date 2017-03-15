package com.example.dsk221.firstapidemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.OwnerItem;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionDetailItem questionDetailItem = getItem(position);
        OwnerItem ownerItem=questionDetailItem.getOwnerItem();
        holder.textQuestionTitle.setText(Utils.convertHtmlInTxt(questionDetailItem.getTitle()));

        holder.textUserName.setText(ownerItem.getDisplayName());
        long lastActiveDate=(long)questionDetailItem.getLastActivityDate();
        holder.textLastActivityDate.setText(getDate(lastActiveDate,"dd/MM/yyyy hh:mm:ss.SSS"));
        holder.textTotalAnswer.setText(questionDetailItem.getAnswerCount());
        String score=Utils.getScoreString(questionDetailItem.getScore());
        holder.textScore.setText(score);
        return convertView;
    }
    private class ViewHolder {
        ImageView imageScore,imageAnswer;
        TextView textScore, textTotalAnswer, textQuestionTitle, textTag,
                textLastActivityDate,textUserName;
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
