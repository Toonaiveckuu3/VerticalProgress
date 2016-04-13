package com.telchina.verticalprogresstool;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ZH on 2016/4/13.
 */
public class VerticalProgress extends ListView{
    private Context mContext;
    private ListAdapter mAdapter;

    public VerticalProgress(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setAdapter(ListAdapter adapter){
        this.mAdapter = adapter;
        super.setAdapter(mAdapter);
    }
}
