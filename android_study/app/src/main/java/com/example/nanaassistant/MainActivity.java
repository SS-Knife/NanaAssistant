package com.example.nanaassistant;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.nanaassistant.acount.Bill;
import com.example.nanaassistant.memorandum.Incident;
import com.example.nanaassistant.ui.main.AcountFragment;
import com.example.nanaassistant.ui.main.MemorandumFragment;
import com.example.nanaassistant.ui.main.PagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private PopupWindow popupWindow_incident;
    private PopupWindow popupWindow_bill;
    public MySQLiteOpenHelper incidentSQLiteOpenHelper = new MySQLiteOpenHelper(this,"bill.db",null,1);
    public MySQLiteOpenHelper billSQLiteOpenHelper = new MySQLiteOpenHelper(this,"incident.db",null,1);
    public static SQLiteDatabase incidentdb;
    public static SQLiteDatabase billdb;
    public static int flag=0;
    private PagerAdapter pagerAdapter;
    private FloatingActionButton fab;
    final String[] antstr={"常规收支","花呗(借/还款)"};
    final String[] iostr={"+","-"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        billdb=billSQLiteOpenHelper.getWritableDatabase();
        incidentdb = incidentSQLiteOpenHelper.getWritableDatabase();
        setStatusBarFullTransparent();
        setContentView(R.layout.activity_main);
        ArrayList<Fragment> fragments=new ArrayList<>();

        fragments.add(MemorandumFragment.newInstance(0));
        fragments.add(AcountFragment.newInstance(1));

        // TODO: 2020/4/15  账本fragement
        fragments.add(new Fragment());

        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager(),fragments);
        final AppBarLayout barLayout = findViewById(R.id.bar);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                flag=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
        iniPopupWindow_incident();
        iniPopupWindow_bill();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0) {
                    if (popupWindow_incident.isShowing()) {
                        popupWindow_incident.dismiss();
                    } else {
                        popupWindow_incident.showAsDropDown(fab);
                    }
                }else{
                    if (popupWindow_bill.isShowing()) {
                        popupWindow_bill.dismiss();
                    } else {
                        popupWindow_bill.showAsDropDown(fab);
                    }
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


    private void iniPopupWindow_incident() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_addincident, null);
        LinearLayout linearLayout = layout.findViewById(R.id.liner_pop);
        Button okbutton = layout.findViewById(R.id.ok);
        final EditText title = layout.findViewById(R.id.titleedit);
        final EditText time = layout.findViewById(R.id.timeedit);
        final EditText detail = layout.findViewById(R.id.detailedit);
        popupWindow_incident = new PopupWindow(layout);
        popupWindow_incident.setFocusable(true);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Log.d(TAG, "onClick: 88888888888888888888888888888");
                        popupWindow_incident.showAsDropDown(fab);
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
                popupWindow_incident.dismiss();
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
                long i = incidentdb.insert("incident", null, cv);


                //实验语句 更新incident
                MemorandumFragment fragment=(MemorandumFragment) pagerAdapter.getItem(0);
                Incident incident=new Incident(time.getText().toString(),"2000","1000",title.getText().toString(),detail.getText().toString(),false,false);
                fragment.addIncident(incident);

                title.setText(null);
                time.setText(null);
                detail.setText(null);
                popupWindow_incident.dismiss();
            }
        });

        // 控制popupwindow的宽度和高度自适应
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        popupWindow_incident.setWidth(linearLayout.getMeasuredWidth() + 500);
        popupWindow_incident.setHeight(linearLayout.getMeasuredHeight() + 150);


        // 控制popupwindow点击屏幕其他地方消失
        popupWindow_incident.setBackgroundDrawable(this.getResources().getDrawable(
                R.mipmap.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        popupWindow_incident.setOutsideTouchable(true);


    }

    // TODO: 2020/5/4 这里还要写
    private void iniPopupWindow_bill() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_addbill, null);
        RelativeLayout relativeLayout = layout.findViewById(R.id.liner_pop);
        final int[] ioflag={0};
        final int[] antflag = {0};
        Button okbutton = layout.findViewById(R.id.ok);
        final Button ant=layout.findViewById(R.id.ant);
        final Button io=layout.findViewById(R.id.io);
        final EditText title = layout.findViewById(R.id.titleedit);
        final EditText money = layout.findViewById(R.id.moneyedit);
        final EditText detail = layout.findViewById(R.id.detailedit);
        popupWindow_bill = new PopupWindow(layout);
        popupWindow_bill.setFocusable(true);
        ant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antflag[0] =1-antflag[0];
                ant.setText(antstr[antflag[0]]);
            }
        });
        io.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ioflag[0] =1-ioflag[0];
                io.setText(iostr[ioflag[0]]);
            }
        });
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据库
                ContentValues cv = new ContentValues();
                Calendar cd = Calendar.getInstance();
                String[] month=new String[1];
                String[] month1=new String[1];
                month[0]=(cd.get(Calendar.MONTH)+1)+"";
                month1[0]=(cd.get(Calendar.MONTH))+"";
                Date date = new Date(System.currentTimeMillis());
                cv.put("ant",antstr[antflag[0]]);
                cv.put("io",iostr[ioflag[0]]);
                cv.put("title", title.getText().toString());
                cv.put("time", getDateStr(date,null));
                cv.put("money", money.getText().toString());
                cv.put("detail", detail.getText().toString());
                cv.put("month",(month[0]));
                long i = billdb.insert("bill", null, cv);
                Log.d(TAG, "onCreateView:11111111111111111111111111 ");
                //实验语句 更新
                Double normalmoney=0.0;
                Double antmoney=0.0;

                Cursor c = billdb.rawQuery("select * from monthcheck where month='"+month[0]+"'",null);
                if(c!=null){
                    c.moveToFirst();
                    if(!c.isAfterLast()) {
                        normalmoney = Double.parseDouble(c.getString(c.getColumnIndex("normal"))) + Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * (1 - antflag[0]);
                        antmoney = Double.parseDouble(c.getString(c.getColumnIndex("ant"))) + Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * antflag[0];
                        ContentValues cv1 = new ContentValues();
                        cv1.put("month", month[0]);
                        cv1.put("normal", normalmoney + "");
                        cv1.put("ant", antmoney + "");
                        billdb.update("monthcheck", cv1, "month=?", month);

                    }else {
                        Cursor d = billdb.rawQuery("select * from monthcheck where month='"+month1[0]+"'",null);
                        if(d!=null) {
                            d.moveToFirst();
                            if (!d.isAfterLast()) {
                                normalmoney = Double.parseDouble(d.getString(d.getColumnIndex("normal"))) + Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * (1 - antflag[0]);
                                antmoney = Double.parseDouble(d.getString(d.getColumnIndex("ant"))) + Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * antflag[0];
                                ContentValues cv1 = new ContentValues();
                                cv1.put("month", month[0]);
                                cv1.put("normal", normalmoney + "");
                                cv1.put("ant", antmoney + "");
                                billdb.insert("monthcheck", null, cv1);
                            }else{
                                normalmoney = Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * (1 - antflag[0]);
                                antmoney = Double.parseDouble(money.getText().toString()) * Math.pow(-1, ioflag[0]) * antflag[0];
                                ContentValues cv1 = new ContentValues();
                                cv1.put("month", month[0]);
                                cv1.put("normal", normalmoney + "");
                                cv1.put("ant", antmoney + "");
                                billdb.insert("monthcheck", null, cv1);
                            }
                        }
                    }
                }
                AcountFragment fragment=(AcountFragment) pagerAdapter.getItem(1);
                Bill bill=new Bill(title.getText().toString(),detail.getText().toString(),
                        getDateStr(date,null),month[0],
                        Double.parseDouble(money.getText().toString()),iostr[ioflag[0]],antstr[antflag[0]]);
                fragment.addBill(bill);
                fragment.check(antmoney,normalmoney);
                title.setText(null);
                money.setText(null);
                detail.setText(null);
                ant.setText(antstr[0]);
                io.setText(iostr[0]);
                popupWindow_bill.dismiss();
            }
        });

        // 控制popupwindow的宽度和高度自适应
        relativeLayout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        popupWindow_bill.setWidth(relativeLayout.getMeasuredWidth() + 500);
        popupWindow_bill.setHeight(relativeLayout.getMeasuredHeight() + 150);


        // 控制popupwindow点击屏幕其他地方消失
        popupWindow_bill.setBackgroundDrawable(this.getResources().getDrawable(
                R.mipmap.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        popupWindow_bill.setOutsideTouchable(true);


    }

    public static String getDateStr(Date date,String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }









}