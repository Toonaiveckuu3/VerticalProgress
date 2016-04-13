package com.telchina.verticalprogresstool;

import android.view.View;

/**
 * Created by ZH on 2016/4/13.
 */
public interface IProgress {
    /**
     * 增加进度条
     * @return
     */
    View addPartStep(View convertView);
}
