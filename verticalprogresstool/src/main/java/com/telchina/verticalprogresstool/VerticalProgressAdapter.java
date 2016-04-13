package com.telchina.verticalprogresstool;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * Created by ZH on 2016/4/13.
 */
public class VerticalProgressAdapter extends BaseAdapter {
    private Context mContext;
    private ListAdapter mAdapter;
    /***/
    private IProgress mProgress;

    public VerticalProgressAdapter(Context context,ListAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
        mProgress = new ProgressImp(mContext);
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mAdapter.getView(position,convertView,parent);
        return mProgress.addPartStep(view);
    }
}
