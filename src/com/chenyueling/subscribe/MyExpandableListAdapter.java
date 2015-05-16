package com.chenyueling.subscribe;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import com.chenyueling.subscribe.entity.Server;

import java.util.ArrayList;
import java.util.zip.Inflater;

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

    private int nowOpen = 0;
    public MyExpandableListAdapter(ArrayList<Server> publicServers, ArrayList<Server> privateServers, ArrayList<Server> subscribeServers, Context context) {
        initGroup();
        System.out.println("my size public" + publicServers.size());
        this.publicServers = publicServers;
        this.privateServers = privateServers;
        this.subscribeServers = subscribeServers;
        this.mInflater = LayoutInflater.from(context);
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
        Server server;
        TextView developer;
        TextView serverName;

        switch (groupPosition){
            case 0:
                server = publicServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_public_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());
                System.out.println(server.getName() + server.getUserName());

                break;
            case 1:
                server = privateServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_public_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());
                System.out.println(server.getName() + server.getUserName());
                break;
            case 2:
                server = subscribeServers.get(childPosition);
                convertView = mInflater.inflate(R.layout.expandable_list_child_public_server, null);
                developer = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_developer);
                serverName = (TextView) convertView.findViewById(R.id.expandable_list_child_public_server_name);
                developer.setText(server.getUserName());
                serverName.setText(server.getName());
                System.out.println(server.getName() + server.getUserName());
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
