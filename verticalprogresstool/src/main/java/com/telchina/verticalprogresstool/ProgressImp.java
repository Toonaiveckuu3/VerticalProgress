package com.telchina.verticalprogresstool;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.telchina.verticalprogresstool.partstepview.PartStepView;

/**
 * 添加垂直进度接口实现
 * <p>
 * Created by ZH on 2016/4/13.
 */
public class ProgressImp implements IProgress {
    private Context mContext;
    private PartStepView mPartStep;

    public ProgressImp(Context context) {
        this.mContext = context;
        mPartStep = new PartStepView(mContext);
    }

    @Override
    public View addPartStep(View convertView) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(mPartStep, params);
        linearLayout.addView(convertView, params);

        return linearLayout;
    }
}
