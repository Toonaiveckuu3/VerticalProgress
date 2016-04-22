package com.telchina.verticalprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerticalProgress verticalProgress = (VerticalProgress) findViewById(R.id.partstepview);
        verticalProgress.setLables(mStrings)
                .invalidate();
        }
    private String[] mStrings = {"测试数据1测试数据1测试数据1测试数据1测试数据1测试数据1测试数据1测试数据1测试数据1测试数据1",
                                "测试数据2",
                                "测试数据3",
                                "测试数据4"};
}

