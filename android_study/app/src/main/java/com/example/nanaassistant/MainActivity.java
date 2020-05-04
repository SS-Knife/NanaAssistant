package com.example.nanaassistant;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nanaassistant.R;
import com.example.nanaassistant.memorandum.Incident;
import com.example.nanaassistant.ui.main.PlaceholderFragment;
import com.example.nanaassistant.ui.main.PagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private PopupWindow popupWindow;
    public MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"incident_db",null,1);
    public static SQLiteDatabase db;
    private PagerAdapter pagerAdapter;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = mySQLiteOpenHelper.getWritableDatabase();
        setStatusBarFullTransparent();
        setContentView(R.layout.activity_main);
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(PlaceholderFragment.newInstance(0));

        // TODO: 2020/4/15  账本fragement
        fragments.add(new Fragment());

        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager(),fragments);
        final AppBarLayout barLayout = findViewById(R.id.bar);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
        iniPopupWindow();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(fab);
                }
            }
        });
    }

    //透明状态栏
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    private void iniPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_addincident, null);
        LinearLayout linearLayout = layout.findViewById(R.id.liner_pop);
        Button okbutton = layout.findViewById(R.id.ok);
        final EditText title = layout.findViewById(R.id.titleedit);
        final EditText time = layout.findViewById(R.id.timeedit);
        final EditText detail = layout.findViewById(R.id.detailedit);
        popupWindow = new PopupWindow(layout);
        popupWindow.setFocusable(true);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Log.d(TAG, "onClick: 88888888888888888888888888888");
                        popupWindow.showAsDropDown(fab);
                        time.setText(getDateStr(date,null));
                    }
                })
                        .setCancelText("取消")
                        .setSubmitText("确定")
                        .setSubmitColor(R.color.pink)
                        .setCancelColor(R.color.pink)
                        .setType(new boolean[]{true, true, true, true, true,false})
                        .setLabel("年","月","日","时","分","秒")
                        .build();
                popupWindow.dismiss();
                pvTime.show();
            }
        });
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //更新数据库
                ContentValues cv = new ContentValues();
                cv.put("title", title.getText().toString());
                cv.put("time", time.getText().toString());
                cv.put("detail", detail.getText().toString());
                Log.d(TAG, "onCreateView:11111111111111111111111111 ");
                long i = db.insert("incident", null, cv);


                //实验语句 更新incident
                PlaceholderFragment fragment=(PlaceholderFragment) pagerAdapter.getItem(0);
                Incident incident=new Incident(time.getText().toString(),"2000","1000",title.getText().toString(),detail.getText().toString(),false,false);
                fragment.addIncident(incident);

                title.setText(null);
                time.setText(null);
                detail.setText(null);
                popupWindow.dismiss();
            }
        });

        // 控制popupwindow的宽度和高度自适应
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        popupWindow.setWidth(linearLayout.getMeasuredWidth() + 500);
        popupWindow.setHeight(linearLayout.getMeasuredHeight() + 150);


        // 控制popupwindow点击屏幕其他地方消失
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.mipmap.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        popupWindow.setOutsideTouchable(true);


    }


    public static String getDateStr(Date date,String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }









}