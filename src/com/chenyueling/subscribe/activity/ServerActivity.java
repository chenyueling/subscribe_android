package com.chenyueling.subscribe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.chenyueling.subscribe.ArticleListAdapter;
import com.chenyueling.subscribe.MyExpandableListAdapter;
import com.chenyueling.subscribe.R;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.entity.Article;
import com.chenyueling.subscribe.entity.Server;
import com.chenyueling.subscribe.service.UserManager;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.JsonUtil;
import com.chenyueling.subscribe.utils.NativeHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by chenyueling on 2015/5/15.
 */
public class ServerActivity extends Activity {
    private Context context;
    private MyExpandableListAdapter myExpandableListAdapter;
    private ExpandableListView expandableListView;
    private ArrayList<Server> publicServers = new ArrayList<Server>();
    private ArrayList<Server> privateServers = new ArrayList<Server>();
    private ArrayList<Server> subscribeServers = new ArrayList<Server>();
    private ArrayList<Server> serversTemp;
    private LayoutInflater mInflater;
    private ArrayList<String> groups;


    private final int PUBLIC_SERVER = 0;
    private final int PRIVATE_SERVER = 1;
    private final int SUBSCRIBE_SERVER = 2;


    private final int PUBLIC_SERVER_SUCCESS = 11;
    private final int PUBLIC_SERVER_ERROR = 10;

    private final int PRIVATE_SERVER_SUCCESS = 21;
    private final int PRIVATE_SERVER_ERROR = 20;


    private final int SUBSCRIBE_SERVER_SUCCESS = 31;
    private final int SUBSCRIBE_SERVER_ERROR = 30;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == PUBLIC_SERVER_SUCCESS) {
                expandableListView.collapseGroup(PUBLIC_SERVER);
                expandableListView.expandGroup(PUBLIC_SERVER);
            }

            if(msg.what == PRIVATE_SERVER_SUCCESS) {
            }

            if(msg.what == SUBSCRIBE_SERVER_SUCCESS) {
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        myExpandableListAdapter = new MyExpandableListAdapter(publicServers,privateServers,subscribeServers,context);
        expandableListView.setAdapter(myExpandableListAdapter);

        ImageView back = (ImageView) findViewById(R.id.server_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ServerActivity.this,MyActivity.class);
                startActivity(intent);
                ((Activity)context).finish();
            }
        });

        publicServerData();
        privateServerData();
        subscribeServerData();


    }


    private void publicServerData(){
       /* Server server;
        publicServers = new ArrayList<Server>();
        for (int i = 0; i <10; i++) {
            server = new Server();
            server.setName("pubname" +i);
            server.setUserName("developer" + i);
            publicServers.add(server);
        }*/


        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                String dc = UserManager.getInstance(context).getUserDeviceCode();
                String url = ConfigHelper.publicServer + "?deviceCode=" + dc;

                try {
                    String json = NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    serversTemp = gson.fromJson(json, new TypeToken<ArrayList<Server>>() {
                    }.getType());

                    publicServers.addAll(serversTemp);

                    handler.sendEmptyMessage(PUBLIC_SERVER_SUCCESS);
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }


    private void privateServerData(){
        /*Server server;
        privateServers = new ArrayList<Server>();
        for (int i = 0; i <10; i++) {
            server = new Server();
            server.setName("prname" +i);
            server.setUserName("developer" + i);
            privateServers.add(server);
        }*/


        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                String dc = UserManager.getInstance(context).getUserDeviceCode();
                String url = ConfigHelper.privateServer + "?deviceCode=" + dc;

                try {
                    String json = NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    serversTemp = gson.fromJson(json, new TypeToken<ArrayList<Server>>() {
                    }.getType());

                    privateServers.addAll(serversTemp);
                    handler.sendEmptyMessage(PRIVATE_SERVER_SUCCESS);
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }


            }
        };

        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }


    private void subscribeServerData(){
        /*Server server;
        subscribeServers = new ArrayList<Server>();
        for (int i = 0; i <10; i++) {
            server = new Server();
            server.setName("subname" +i);
            server.setUserName("developer" + i);
            subscribeServers.add(server);
        }*/

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                String dc = UserManager.getInstance(context).getUserDeviceCode();
                String url = ConfigHelper.subscribeServer + "?deviceCode=" + dc;

                try {
                    String json = NativeHttpClient.get(url);
                    Gson gson = new Gson();
                    serversTemp = gson.fromJson(json, new TypeToken<ArrayList<Server>>() {
                    }.getType());

                    subscribeServers.addAll(serversTemp);
                    handler.sendEmptyMessage(SUBSCRIBE_SERVER_SUCCESS);
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }


            }
        };

        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }
}
