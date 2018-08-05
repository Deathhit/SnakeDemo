package com.deathhit.utility.adapter;

import android.view.View;

import java.util.List;

public class PagerViewAdapter extends PagerObjectAdapter<View> {

    public PagerViewAdapter(){
        super();
    }

    @Override
    public View onCreateView(List<View> items, int position) {
        return items.get(position);
    }

    @Override
    public void onBindView(List<View> items, View view, int position) {

    }
}
