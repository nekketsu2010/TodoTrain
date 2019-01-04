package com.todotrain.nekketsu.todotrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleListAdapter extends ArrayAdapter<Schedule> {
    private int mResource;
    private List<Schedule> mItems;
    private LayoutInflater mInflater;

    public ScheduleListAdapter(Context context, int resource, List<Schedule> items) {
        super(context, resource, items);
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
        view = convertView;
        }
        else {
        view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        Schedule item = mItems.get(position);

        // タイトルを設定
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        TextView title = (TextView)view.findViewById(R.id.taskName);
        title.setText(item.taskName + " " + item.railWay.jp_name + " " + sdf.format(item.calendar.getTime()));

        return view;
    }
}
