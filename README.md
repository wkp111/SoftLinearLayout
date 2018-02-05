# SoftLinearLayout [ ![Download](https://api.bintray.com/packages/wkp/maven/SoftLinearLayout/images/download.svg) ](https://bintray.com/wkp/maven/SoftLinearLayout/_latestVersion)
底部控件随输入法高度变化而变化，比QQ聊天界面更完美
## 演示图
无状态栏示例图:<br/>
![SoftLinearLayout1](https://github.com/wkp111/SoftLinearLayout/blob/master/SoftLinearLayout1.gif "演示图")<br/>
有状态栏示例图:<br/>
![SoftLinearLayout](https://github.com/wkp111/SoftLinearLayout/blob/master/SoftLinearLayout.gif "演示图")
## Gradle集成
```groovy
dependencies{
      compile 'com.wkp:SoftLinearLayout:1.0.3'
      //Android Studio3.0+可用以下方式
      //implementation 'com.wkp:SoftLinearLayout:1.0.3'
}

//如集成失败或不愿意等待，请加上我的maven仓库地址
maven { url "https://dl.bintray.com/wkp/maven" }
```
Note：可能存在Jcenter还在审核阶段，这时会集成失败！
## 使用详解
> 属性讲解
```xml
  <!--可变高度的极限小高度-->
  <attr name="wkp_minHeight" format="integer"/>
  <!--可变高度的极限大高度-->
  <attr name="wkp_maxHeight" format="integer"/>
  <!--显示软键盘时的动画时长-->
  <attr name="wkp_showSoftDuration" format="integer"/>
  <!--开关底部布局时的动画时长-->
  <attr name="wkp_toggleDuration" format="integer"/>
  <!--告诉控件是否有沉浸式状态栏（一般状态栏透明或半透明均为false）-->
  <attr name="wkp_hasStatusBar" format="boolean"/>
```
Note：每个属性都有对应的java设置代码！
> 布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.wkp.softlinearlayout.view.SoftLinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/sll"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:id="@+id/ll_top"
        android:orientation="vertical"
        android:background="@android:color/holo_blue_bright"
        android:focusable="true"
        android:focusableInTouchMode="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <RelativeLayout
            android:layout_weight="1"
            android:layout_width="match_parent"
            android:layout_height="0dp">

            <ListView
                android:id="@+id/lv"
                android:stackFromBottom="true"
                android:transcriptMode="normal"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

            </ListView>

        </RelativeLayout>

        <EditText
            android:hint="你好"
            android:id="@+id/et"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

        <Button
            android:text="确定"
            android:onClick="sure"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll_bottom"
        android:orientation="vertical"
        android:gravity="center"
        android:background="@android:color/holo_green_dark"
        android:layout_width="match_parent"
        android:layout_height="100dp">

        <TextView
            android:gravity="center"
            android:text="底部"
            android:textColor="@android:color/white"
            android:textSize="16sp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

    </LinearLayout>

</com.wkp.softlinearlayout.view.SoftLinearLayout>
```
Note：SoftLinearLayout一般为根布局，它的子布局最多2个，否则会异常！
> 代码示例
```java
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

```
Note：还有其他API请根据需要自行参考！
## 感谢
沉浸式状态栏设置库（Github地址）：<a href="https://github.com/laobie/StatusBarUtil">laobie/StatusBarUtil</a>
## 寄语
控件支持直接代码创建，还有更多API请观看<a href="https://github.com/wkp111/SoftLinearLayout/blob/master/softlinearlayout-lib/src/main/java/com/wkp/softlinearlayout/view/SoftLinearLayout.java">SoftLinearLayout.java</a>内的注释说明。<br/>
欢迎大家使用，感觉好用请给个Star鼓励一下，谢谢！<br/>
大家如果有更好的意见或建议以及好的灵感，请邮箱作者，谢谢！<br/>
QQ邮箱：1535514884@qq.com<br/>
163邮箱：15889686524@163.com<br/>
Gmail邮箱：wkp15889686524@gmail.com<br/>

## 版本更新
* v1.0.3<br/>
修复当沉浸式状态栏为透明或半透明时所引起的布局BUG（需设置属性wkp_hasStatusBar或代码设置）.<br/>
感谢<a href="https://github.com/HappyJess">HappyJess</a>提供的建议.<br/><br/>
* v1.0.2<br/>
新增动画功能，可设置动画时长，详情参考属性讲解.<br/><br/>
* v1.0.1<br/>
新创建随软键盘高度变化而变化控件库.
## License

   Copyright 2017 wkp

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

