package com.example.dsk221.firstapidemo.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class QuestionItem {
    private String title;
    private String link;
    private String body;

    @SerializedName("post_type")
    private String postType;

    @SerializedName("creation_date")
    private String creationDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }


}
