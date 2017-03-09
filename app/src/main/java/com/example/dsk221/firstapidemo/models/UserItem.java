package com.example.dsk221.firstapidemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserItem implements Parcelable {
    public UserItem(String displayName, String profileImage, int reputation, BuzzItem badgeCounts) {
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.reputation = reputation;
        this.badgeCounts = badgeCounts;
    }

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("profile_image")
    private String profileImage;

    private int reputation;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("badge_counts")
    private BuzzItem badgeCounts;

    @SerializedName("view_count")
    private int viewCount;

    @SerializedName("answer_count")
    private int answerCount;

    @SerializedName("question_count")
    private int questionCount;

    private String location;

    @SerializedName("website_url")
    private String websiteUrl;

    @SerializedName("about_me")
    private String aboutMe;

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BuzzItem getBadgeCounts() {
        return badgeCounts;
    }

    public void setBadgeCounts(BuzzItem badgeCounts) {
        this.badgeCounts = badgeCounts;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.profileImage);
        dest.writeInt(this.reputation);
        dest.writeInt(this.userId);
        dest.writeParcelable(this.badgeCounts, flags);

    }

    protected UserItem(Parcel in) {
        this.displayName = in.readString();
        this.profileImage = in.readString();
        this.reputation = in.readInt();
        this.userId=in.readInt();
        this.badgeCounts = in.readParcelable(BuzzItem.class.getClassLoader());
    }

    public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel source) {
            return new UserItem(source);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };
}
