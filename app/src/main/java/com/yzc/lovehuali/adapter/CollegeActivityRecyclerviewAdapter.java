package com.yzc.lovehuali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.getLogoResourceFromName;
import com.yzc.lovehuali.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/4/4 0004.
 */
public class CollegeActivityRecyclerviewAdapter extends RecyclerView.Adapter<CollegeActivityRecyclerviewAdapter.ViewHolder> {
    public List<JSONObject> datas = null;
    private getLogoResourceFromName getLogo;

    public int[] textViewColorId = new int[]{R.color.card01,R.color.card02,R.color.card03,R.color.card04,R.color.card05,R.color.card06,R.color.card07,R.color.card08,R.color.card09};
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
            String title = datas.get(position).getString("title");
            viewHolder.tvActivityTitle.setText(title);
            viewHolder.tvClubName.setText(publishUser);
            viewHolder.tvPublicDate.setText("线上发布时间：" + datas.get(position).getString("publishDate"));
            viewHolder.tvActivityContext.setText(datas.get(position).getString("context"));
            viewHolder.ivClubLogo.setImageResource(getLogo.getLogoResourceFromName(publishUser));
            viewHolder.llNameBar.setBackgroundResource(textViewColorId[((title.toCharArray()[1]+title.toCharArray()[2])%9)]);
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
        public CircleImageView ivClubLogo;
        public LinearLayout llNameBar;
        public ViewHolder(View view){
            super(view);
            tvActivityTitle = (TextView) view.findViewById(R.id.tvActivityTitle);
            tvClubName = (TextView) view.findViewById(R.id.tvClubName);
            tvPublicDate = (TextView) view.findViewById(R.id.tvPublishDate);
            tvActivityContext = (TextView) view.findViewById(R.id.tvActivityContext);
            ivClubLogo = (CircleImageView) view.findViewById(R.id.ivClubLogo);
            llNameBar = (LinearLayout) view.findViewById(R.id.llNameBar);
        }
    }
}
