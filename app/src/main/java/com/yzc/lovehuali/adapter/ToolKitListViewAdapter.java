package com.yzc.lovehuali.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzc.lovehuali.R;

/**
 * Created by Administrator on 2015/2/16 0016.
 */
public class ToolKitListViewAdapter extends ArrayAdapter {

    private Context context;
    private int listViewCellId;
    private String toolKitListName[] = {"图书查询","成绩查询","校园黄页","在线报修","新生指南","    即将上线 ↓","失物招领","学习资料"};
    private String toolKitListDescription[] = {"书中自有颜如玉","绩点高能拿奖学金","黄页在手，联系不愁",
            "报修、查单样样都有","快速成为老油条","","寻你所失，归你所拾","挂科什么的都是浮云"};
    private int tookKitListIcon[] = {R.drawable.ic_tool_library,R.drawable.ic_tool_score,R.drawable.ic_tool_yellowpage,
                                        R.drawable.ic_tool_online_repair,R.drawable.ic_tool_entrance,0,R.drawable.ic_tool_laf,R.drawable.ic_tool_share};

    public ToolKitListViewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.listViewCellId = resource;
    }

    @Override
    public int getCount() {
        return toolKitListName.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 5) {
            TextView textView = new TextView(getContext());
            textView.setText(toolKitListName[position]);
            textView.setTextSize(16);
            textView.setBackgroundColor(Color.parseColor("#F0F0F0"));
            return textView;
        }
        ImageView icon = null;
        TextView name = null;
        TextView description = null;

        convertView = LayoutInflater.from(context).inflate(listViewCellId,null);

        icon = (ImageView) convertView.findViewById(R.id.ivToolItem);
        name = (TextView) convertView.findViewById(R.id.tvToolItemName);
        description = (TextView) convertView.findViewById(R.id.tvToolItemDescription);

        icon.setImageResource(tookKitListIcon[position]);
        name.setText(toolKitListName[position]);
        description.setText(toolKitListDescription[position]);


        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }//不是所有选项都可以选择

    @Override
    public boolean isEnabled(int position) {
        if (position == 5) {

            return false;

        } else {

            return true;

        }
    }//对某些选项设置不可点击
}
