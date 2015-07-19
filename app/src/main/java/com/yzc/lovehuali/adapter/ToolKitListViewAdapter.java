package com.yzc.lovehuali.adapter;

import android.content.Context;
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
    private String toolKitListName[] = {"图书查询","失物招领","校园黄页","在线报修","新生指南","校园地图"};
    private String toolKitListDescription[] = {"书中自有颜如玉","寻你所失，归你所拾","黄页在手，联系不愁",
            "报修、查单样样都有","既入深宫，悉其条规","洞悉地理方位方行天下"};
    private int tookKitListIcon[] = {R.drawable.ic_tool_library,R.drawable.ic_tool_laf,R.drawable.ic_tool_yellowpage,
                                        R.drawable.ic_tool_online_repair,R.drawable.ic_tool_entrance,R.drawable.ic_tool_map};

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
}
