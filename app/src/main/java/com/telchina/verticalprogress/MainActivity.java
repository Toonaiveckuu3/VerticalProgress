package com.telchina.verticalprogress;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telchina.verticalprogresstool.ProgressImp;
import com.telchina.verticalprogresstool.partstepview.PartStepView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params;
        TextView textView = new TextView(this);
        textView.setPadding(10, 10, 10, 10);
        textView.setText("Hello Word!");
        Resources r = this.getResources();
        textView.setTextColor(r.getColor(R.color.colorPrimary));
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 10, 30, 0);

        PartStepView partStepView = new PartStepView(this);

        TextView textView1 = new TextView(this);
        textView1.setPadding(10, 10, 10, 10);
        textView1.setText("Hello Word!");
        textView1.setTextColor(r.getColor(R.color.colorPrimary));

        linearLayout.addView(textView,params);
        linearLayout.addView(partStepView,params);

        setContentView(linearLayout);
    }
}

