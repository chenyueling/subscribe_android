package com.chenyueling.subscribe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.chenyueling.subscribe.entity.Article;

import java.util.ArrayList;

/**
 * Created by chenyueling on 2015/5/12.
 */
public class ArticleListAdapter extends BaseAdapter {
    ArrayList<Article> articles = null;
    private LayoutInflater mInflater;



    public ArticleListAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        this.mInflater = LayoutInflater.from(context);
    }

    public ArticleListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            /*viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item,null);
            viewHolder.title  = (TextView) view.findViewById(R.id.list_item_Title);
            viewHolder.time = (TextView) view.findViewById(R.id.list_item_time);
            view.setTag(viewHolder);*/
            view = mInflater.inflate(R.layout.item,null);
            TextView title = (TextView) view.findViewById(R.id.list_item_Title);
            title.setText(articles.get(i).getTitle());

            TextView time = (TextView) view.findViewById(R.id.list_item_time);
            time.setText(articles.get(i).getCreateTime());

        }else{
            /*viewHolder = (ViewHolder) view.getTag();
            viewHolder.title.setText(articles.get(i).getTitle());
            viewHolder.time.setText(articles.get(i).getCreateTime());*/
            TextView title = (TextView) view.findViewById(R.id.list_item_Title);
            title.setText(articles.get(i).getTitle());

            TextView time = (TextView) view.findViewById(R.id.list_item_time);
            time.setText(articles.get(i).getCreateTime());
        }
        return view;
    }


    private static class ViewHolder{
        public TextView title;
        public TextView time;
    }
}
