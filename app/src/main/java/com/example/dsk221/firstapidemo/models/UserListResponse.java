package com.example.dsk221.firstapidemo.models;

import java.util.ArrayList;

/**
 * Created by dsk-221 on 28/2/17.
 */

public class UserListResponse {
   private ArrayList<UserItem> items;

    public void setItems(ArrayList<UserItem> items) {
        this.items = items;
    }

    public ArrayList<UserItem> getItems() {
        return items;
    }
}
