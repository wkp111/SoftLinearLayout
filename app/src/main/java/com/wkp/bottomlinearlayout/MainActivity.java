package com.wkp.bottomlinearlayout;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jaeger.library.StatusBarUtil;
import com.wkp.softlinearlayout.view.SoftLinearLayout;
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    private SoftLinearLayout mSll;
    private String[] mStrings = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("\\B");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置状态栏颜色，有状态栏app:wkp_hasStatusBar="true"
//        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary));
        //设置状态栏半透明，没有状态栏app:wkp_hasStatusBar="false"
        StatusBarUtil.setTranslucent(this);

        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStrings));
        mSll = findViewById(R.id.sll);
        //没有状态栏
        mSll.hasStatusBar(false);
        //设置开关改变监听
        mSll.setOnToggleChangedListener(new SoftLinearLayout.OnToggleChangedListener() {
            @Override
            public void onToggleChanged(boolean isToggle) {
                Log.d("MainActivity", "isToggle:" + isToggle);
            }
        });
    }

    //点击开/关
    public void sure(View view) {
        mSll.toggle();
    }
}
