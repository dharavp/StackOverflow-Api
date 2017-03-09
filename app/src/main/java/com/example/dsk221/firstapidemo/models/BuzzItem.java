package com.example.dsk221.firstapidemo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dsk-221 on 27/2/17.
 */

public class BuzzItem implements Parcelable{
    private int silver,gold,bronze;

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.silver);
        dest.writeInt(this.gold);
        dest.writeInt(this.bronze);
    }

    public BuzzItem() {
    }

    protected BuzzItem(Parcel in) {
        this.silver = in.readInt();
        this.gold = in.readInt();
        this.bronze = in.readInt();
    }

    public static final Creator<BuzzItem> CREATOR = new Creator<BuzzItem>() {
        @Override
        public BuzzItem createFromParcel(Parcel source) {
            return new BuzzItem(source);
        }

        @Override
        public BuzzItem[] newArray(int size) {
            return new BuzzItem[size];
        }
    };
}
