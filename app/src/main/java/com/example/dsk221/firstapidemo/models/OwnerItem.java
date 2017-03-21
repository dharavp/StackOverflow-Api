package com.example.dsk221.firstapidemo.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class OwnerItem {
    @SerializedName("display_name")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
