package com.example.dsk221.firstapidemo.models;

import java.util.ArrayList;

/**
 * Created by dsk-221 on 28/2/17.
 */

public class ListResponse<T> {

   private ArrayList<T> items;

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public ArrayList<T> getItems() {
        return items;
    }
}