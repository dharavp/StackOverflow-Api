package com.example.dsk221.firstapidemo.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class QuestionDetailItem {

    @SerializedName("is_answered")
    private boolean isAnswered;

    private String title;

    private int score;

    private String link;

    private ArrayList<String> tags;

    @SerializedName("answer_count")
    private int answerCount;

    @SerializedName("last_activity_date")
    private int lastActivityDate;

    @SerializedName("owner")
    private OwnerItem ownerItem;

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(int lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public OwnerItem getOwnerItem() {
        return ownerItem;
    }

    public void setOwnerItem(OwnerItem ownerItem) {
        this.ownerItem = ownerItem;
    }
    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
