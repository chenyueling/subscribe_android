package com.chenyueling.subscribe.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.chenyueling.subscribe.ArticleListAdapter;
import com.chenyueling.subscribe.MyListView;
import com.chenyueling.subscribe.R;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.entity.Article;
import com.chenyueling.subscribe.service.UserManager;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.NativeHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MyActivity extends Activity implements AdapterView.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */

    private MyActivity context = null;
    MyListView listView = null;
    private static ArrayList<Article> articles = new ArrayList<Article>();
    private static ArrayList<Article> articlesTemp = new ArrayList<Article>();
    private static ArrayList<Article> moreList = null;
    private static ArticleListAdapter articleListAdapter;
    private static final int FINISH = 1;
    private static final int  SCROLL= 2;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == FINISH) {
                articleListAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(context);

            }

            if(msg.what == SCROLL) {
                Toast.makeText(context,"refresh finish",Toast.LENGTH_LONG).show();
                articleListAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
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
        listView = (MyListView) findViewById(R.id.listView);
        //init UserManager Context
        articleListAdapter = new ArticleListAdapter(articles, context);
        listView.setAdapter(articleListAdapter);
        String deviceCode = UserManager.getInstance(this).getUserDeviceCode();
        Toast.makeText(context,"you deviceCode: " + deviceCode,Toast.LENGTH_LONG).show();
        if(deviceCode == null || "".equals(deviceCode)){
            UserManager.getInstance(this).register();
        }




        if ((deviceCode != null || "".equals(deviceCode)) && (articles == null || articles.size() == 0)){
            Toast.makeText(this,"Article NULL",Toast.LENGTH_SHORT).show();
            getArticles(deviceCode);
        }
        findViewById(R.id.titleBarAddBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyActivity.this ,ServerActivity.class);
                startActivity(intent);
                ((MyActivity)context).finish();
            }
        });

        listView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String dc = UserManager.getInstance(context).getUserDeviceCode();
                refreshArticles(dc);
            }
        });
        ;

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemClick", "onItemClick" + articles.get(i).getId());
    }



    private void refreshArticles(final String deviceCode) {
        Thread thread = new Thread() {
            @Override
            public void run(){
                super.run();
                String url = ConfigHelper.subscribeArticles+"?sort=createTime&order=desc&p=1&r=1000&deviceCode=" + deviceCode;
                try {
                    String json =  NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    articlesTemp = gson.fromJson(json, new TypeToken<ArrayList<Article>>() {}.getType());
                    articles.clear();
                    articles.addAll(articlesTemp);
                    handler.sendEmptyMessage(SCROLL);
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }


    private void getArticles(final String deviceCode){

        Thread thread = new Thread() {
            @Override
            public void run(){
                super.run();
                String url = ConfigHelper.subscribeArticles+"?sort=createTime&order=desc&p=1&r=1000&deviceCode=" + deviceCode;
                try {
                    String json =  NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    articlesTemp = gson.fromJson(json, new TypeToken<ArrayList<Article>>() {}.getType());
                    articles.addAll(articlesTemp);
                    handler.sendEmptyMessage(FINISH);
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();

    }


}
