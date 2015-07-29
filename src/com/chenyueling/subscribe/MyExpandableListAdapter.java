package com.chenyueling.subscribe;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.common.Result;
import com.chenyueling.subscribe.entity.Server;
import com.chenyueling.subscribe.service.UserManager;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.JsonUtil;
import com.chenyueling.subscribe.utils.NativeHttpClient;
import com.chenyueling.subscribe.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyueling on 2015/5/15.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Server> publicServers;
    private ArrayList<Server> privateServers;
    private ArrayList<Server> subscribeServers;
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<String> groups;

    private final int CLICK_BTN = 0;
    private final int SUBSCRIBE_SUCCESS = 1;
    private final int SUBSCRIBE_ERROR = 2;
    private final int CANCEL_SUCCESS = 3;
    private final int CANCEL_ERROR = 4;
    private int nowOpen = 0;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if(msg.what == CLICK_BTN){

            }else if (msg.what == SUBSCRIBE_SUCCESS){
                Toast.makeText(context,"subscribe success!",Toast.LENGTH_SHORT).show();
            }else if (msg.what == SUBSCRIBE_ERROR){
               Toast.makeText(context,msg.obj+"",Toast.LENGTH_SHORT).show();
           }else if(msg.what == CANCEL_SUCCESS){
               Toast.makeText(context,"cancel success!",Toast.LENGTH_SHORT).show();
           }else if(msg.what == CANCEL_ERROR){
               Toast.makeText(context,msg.obj+"",Toast.LENGTH_SHORT).show();
           }


        }
    };

    public MyExpandableListAdapter(final ArrayList<Server> publicServers,final ArrayList<Server> privateServers,final ArrayList<Server> subscribeServers, Context context) {
        initGroup();
        System.out.println("my size public" + publicServers.size());
        this.publicServers = publicServers;
        this.privateServers = privateServers;
        this.subscribeServers = subscribeServers;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }





    public MyExpandableListAdapter(ArrayList<String> groups, ArrayList<Server> publicServers, ArrayList<Server> privateServers, ArrayList<Server> subscribeServers, Context context) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        System.out.println("group:" + groupPosition);

        switch(groupPosition){
        case 0:
            count = publicServers.size();
            break;
        case 1:
            count = privateServers.size();
            break;
        case 2:
            count = subscribeServers.size();
            break;
        }
        System.out.println("getChildrenCount" + count);
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.expandable_list_group_item, null);
            TextView groupName = (TextView) convertView.findViewById(R.id.server_group_name);
            groupName.setText(groups.get(groupPosition));

        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Server server;
        TextView developer;
        TextView serverName;
        final ImageView subscribeBtn;
        final ImageView delBtn;
        switch (groupPosition){
            case 0:
                server = publicServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_public_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                subscribeBtn = (ImageView)convertView.findViewById(R.id.expandable_list_child_public_server_subscribe_btn);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());

                if(server.isSubscribeStatus()){
                    subscribeBtn.setImageResource(R.drawable.added);
                }else {
                    subscribeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subscribeBtn.setImageResource(R.drawable.loading);
                            Thread subScribeThread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    String url = ConfigHelper.doSubscribe;
                                    Map<String, String> pramater = new HashMap<String, String>();
                                    pramater.put("deviceCode", PreferencesUtil.getString(context, ConfigHelper.KYE_DEVICE_CODE));
                                    pramater.put("serverId", server.getId());
                                    String p = JsonUtil.toJson(pramater);
                                    try {
                                        String json = NativeHttpClient.post(url, p);
                                        System.out.println(json);
                                        Result result = JsonUtil.format(json, Result.class);
                                        if (result.getStatus().equals("success")) {
                                            handler.sendEmptyMessage(SUBSCRIBE_SUCCESS);
                                        } else {
                                            Message msg = Message.obtain();
                                            msg.what = SUBSCRIBE_ERROR;
                                            msg.obj = result.getData();
                                            handler.sendMessage(msg);
                                        }
                                    } catch (HttpRequestException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            subScribeThread.setContextClassLoader(getClass().getClassLoader());
                            subScribeThread.start();
                        }
                    });
                }
                break;
            case 1:
                server = privateServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_public_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                subscribeBtn = (ImageView)convertView.findViewById(R.id.expandable_list_child_public_server_subscribe_btn);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());

                if(server.isSubscribeStatus()){
                    subscribeBtn.setImageResource(R.drawable.added);
                }else {
                    subscribeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subscribeBtn.setImageResource(R.drawable.loading);
                            Thread subScribeThread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    String url = ConfigHelper.doSubscribe;
                                    Map<String, String> pramater = new HashMap<String, String>();
                                    pramater.put("deviceCode", PreferencesUtil.getString(context, ConfigHelper.KYE_DEVICE_CODE));
                                    pramater.put("serverId", server.getId());
                                    String p = JsonUtil.toJson(pramater);
                                    try {
                                        String json = NativeHttpClient.post(url, p);
                                        System.out.println(json);
                                        Result result = JsonUtil.format(json, Result.class);
                                        if (result.getStatus().equals("success")) {
                                            handler.sendEmptyMessage(SUBSCRIBE_SUCCESS);
                                        } else {
                                            Message msg = Message.obtain();
                                            msg.what = SUBSCRIBE_ERROR;
                                            msg.obj = result.getData();
                                            handler.sendMessage(msg);
                                        }
                                    } catch (HttpRequestException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            subScribeThread.setContextClassLoader(getClass().getClassLoader());
                            subScribeThread.start();
                        }
                    });
                }
                break;
            case 2:
                server = subscribeServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_subscribe_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());

                delBtn = (ImageView)convertView.findViewById(R.id.expandable_list_child_public_server_del_btn);
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread cancelThread = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                String dc = UserManager.getInstance(context).getUserDeviceCode();
                                String url = ConfigHelper.doCancel +"?deviceCode=" + dc + "&serverId=" + server.getId();
                                Map<String,String> param = new HashMap<String, String>();
                                try {
                                    String json =  NativeHttpClient.delete(url, JsonUtil.toJson(param));
                                    Result result = JsonUtil.format(json, Result.class);
                                    if("success".equals(result.getStatus())){
                                        handler.sendEmptyMessage(CANCEL_SUCCESS);
                                    }else{
                                        Message msg = Message.obtain();
                                        msg.what = SUBSCRIBE_ERROR;
                                        msg.obj = result.getData();
                                        handler.sendMessage(msg);
                                    }
                                } catch (HttpRequestException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        cancelThread.setContextClassLoader(getClass().getClassLoader());
                        cancelThread.start();
                    }
                });

                break;
            default:
                break;
            }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private void initGroup(){
        groups = new ArrayList<String>();
        groups.add("公有服务");
        groups.add("私有服务");
        groups.add("已订阅");
    }


}
