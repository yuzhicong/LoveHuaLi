package com.yzc.lovehuali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.getLogoResourceFromName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/2/1 0001.
 */
public class IntegrateNewsListviewAdapter extends ArrayAdapter<JSONObject>{

    private int resourceId;
    private List<JSONObject> objectsList;
    private Context context;
    private getLogoResourceFromName getLogo;
    public IntegrateNewsListviewAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.objectsList = objects;
        getLogo = new getLogoResourceFromName();
    }

    @Override
    public int getCount() {
        return objectsList.size();
    }

    @Override
    public void add(JSONObject object) {
        objectsList.add(object);
    }
    @Override
    public JSONObject getItem(int position) {

        return objectsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvClubName = null;
        TextView tvNewsTitle = null;
        TextView tvNewsDate = null;
        ImageView ivClublogo;

        LinearLayout ll = null;

        if(convertView !=null){
            ll = (LinearLayout)convertView;

        }else{
            ll = (LinearLayout) LayoutInflater.from(context).inflate(resourceId, null);
        }//回收convertView机制

        JSONObject joNewsItem = getItem(position);

        tvClubName = (TextView) ll.findViewById(R.id.tvClubName);
        tvNewsTitle = (TextView) ll.findViewById(R.id.tvNewsTitle);
        tvNewsDate = (TextView) ll.findViewById(R.id.tvNewsDate);
        ivClublogo = (ImageView) ll.findViewById(R.id.ivClubLogo);

        try {
            String publishUser = joNewsItem.getString("publishUser");
            tvClubName.setText(publishUser);
            tvNewsTitle.setText(joNewsItem.getString("title"));
            tvNewsDate.setText(joNewsItem.getString("publishDate").substring(0,10));

            ivClublogo.setImageResource(getLogo.getLogoResourceFromName(publishUser));//根据出版作者名称，匹配logo

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return ll;
    }
}
