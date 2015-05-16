package com.chenyueling.subscribe.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.chenyueling.subscribe.ArticleListAdapter;
import com.chenyueling.subscribe.R;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.entity.Article;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.NativeHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MyActivity extends Activity implements AdapterView.OnItemClickListener ,AbsListView.OnScrollListener{
    /**
     * Called when the activity is first created.
     */

    private MyActivity context = null;
    ListView listView = null;
    private ArrayList<Article> articles = new ArrayList<Article>();
    private ArrayList<Article> moreList = null;
    private ArticleListAdapter articleListAdapter;
    private int FINISH = 1;
    private int SCROLL = 2;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == FINISH) {
                articleListAdapter = new ArticleListAdapter(articles, context);
                listView.setAdapter(articleListAdapter);
                listView.setOnItemClickListener(context);
                listView.setOnScrollListener(context);
            }

            if(msg.what == SCROLL) {
                articleListAdapter.notifyDataSetChanged();
                System.out.println(articles.size());
                //listView.deferNotifyDataSetChanged();
            }
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        listView = (ListView) findViewById(R.id.listView);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        SharedPreferences sharedPreferences =  this.getSharedPreferences(ConfigHelper.SUBSCRIBE, Context.MODE_PRIVATE);
        String deviceCode = sharedPreferences.getString(ConfigHelper.SUBSCRIBE,ConfigHelper.KYE_DEVICE_CODE);
        if(deviceCode == null || "".equals(deviceCode)){
            //UserManager.getInstance().register(this);
        }

        Toast.makeText(this, imei, Toast.LENGTH_LONG).show();
        Thread thread = new Thread() {
            @Override
            public void run(){
                super.run();
                String url = ConfigHelper.host +":"+ConfigHelper.port+"/jersey-jetty/rest/device/articles/subscribe_list?sort=createTime&order=desc&p=1&r=1000&deviceCode=3";
                try {
                   String json =  NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    articles = gson.fromJson(json, new TypeToken<ArrayList<Article>>() {}.getType());
                    handler.sendEmptyMessage(FINISH);
                } catch (HttpRequestException e) {
                    e.printStackTrace();

                }
            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();


        findViewById(R.id.titleBarAddBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyActivity.this ,ServerActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemClick", "onItemClick" + articles.get(i).getId());
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        /*Log.d("onScrollStateChanged","scrolling" + i);
        System.out.println("scrolling" + i);*/
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        /*Log.d("onScroll","scrolling" + i);
        System.out.println("onScroll" + i);

        Thread thread = new Thread() {
            @Override
            public void run(){
                super.run();
                String url = "http://192.168.1.219:8080/jersey-jetty/rest/device/articles/subscribe_list?sort=createTime&order=desc&p=1&r=3&deviceCode=3";
                try {
                    String json =  NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    moreList = gson.fromJson(json, new TypeToken<ArrayList<Article>>() {}.getType());
                    articles.addAll(moreList);
                    handler.sendEmptyMessage(SCROLL);
                } catch (HttpRequestException e) {
                    e.printStackTrace();

                }
            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();*/

    }
}
