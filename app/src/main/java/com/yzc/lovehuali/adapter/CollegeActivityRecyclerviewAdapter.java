package com.yzc.lovehuali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.getLogoResourceFromName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/4/4 0004.
 */
public class CollegeActivityRecyclerviewAdapter extends RecyclerView.Adapter<CollegeActivityRecyclerviewAdapter.ViewHolder> {
    public List<JSONObject> datas = null;
    private getLogoResourceFromName getLogo;
    public CollegeActivityRecyclerviewAdapter(List<JSONObject> datas) {
        this.datas = datas;
        getLogo = new getLogoResourceFromName();
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_college_activity_cell,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        try {
            String publishUser = datas.get(position).getString("publishUser");
            viewHolder.tvActivityTitle.setText(datas.get(position).getString("title"));
            viewHolder.tvClubName.setText(publishUser);
            viewHolder.tvPublicDate.setText("线上发布时间：" + datas.get(position).getString("publishDate"));
            viewHolder.tvActivityContext.setText(datas.get(position).getString("context"));
            viewHolder.ivClubLogo.setImageResource(getLogo.getLogoResourceFromName(publishUser));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvActivityTitle,tvClubName,tvPublicDate,tvActivityContext;
        public ImageView ivClubLogo;
        public ViewHolder(View view){
            super(view);
            tvActivityTitle = (TextView) view.findViewById(R.id.tvActivityTitle);
            tvClubName = (TextView) view.findViewById(R.id.tvClubName);
            tvPublicDate = (TextView) view.findViewById(R.id.tvPublishDate);
            tvActivityContext = (TextView) view.findViewById(R.id.tvActivityContext);
            ivClubLogo = (ImageView) view.findViewById(R.id.ivClubLogo);
        }
    }
}
