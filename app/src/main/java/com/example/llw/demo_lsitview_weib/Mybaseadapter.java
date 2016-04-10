package com.example.llw.demo_lsitview_weib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by llw on 2016/4/10.
 */
public class Mybaseadapter extends BaseAdapter {
    private List<Myget_item> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public Mybaseadapter(List<Myget_item> list, MainActivity mainActivity) {
        this.list = list;
        this.context = mainActivity;
        layoutInflater = LayoutInflater.from(context);
    }

    public void OnDateChanget(List<Myget_item> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_show);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.textView.setText(list.get(position).srt);
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
    }
}
