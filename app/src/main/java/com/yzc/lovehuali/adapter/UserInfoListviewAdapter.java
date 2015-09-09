package com.yzc.lovehuali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.UserInformationActivity;
import com.yzc.lovehuali.bmob.StudentUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/1/30 0030.
 */
public class UserInfoListviewAdapter extends ArrayAdapter {

    private Context context;
    private int listViewCellId;
    private String stringUserInfolist[] = {"姓名","年级","班级","学部","专业"};
    public UserInfoListviewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.listViewCellId = resource;

    }

    @Override
    public int getCount() {
        return stringUserInfolist.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvUserInfoKey;
        TextView tvUserInfoValue;

        convertView = LayoutInflater.from(context).inflate(listViewCellId,null);
        tvUserInfoKey = (TextView) convertView.findViewById(R.id.tvUserInfoKey);
        tvUserInfoKey.setText(stringUserInfolist[position]);

        tvUserInfoValue = (TextView) convertView.findViewById(R.id.tvUserInfoValue);
        StudentUser user = BmobUser.getCurrentUser(getContext(),StudentUser.class);
        switch (position){
            case 1:
                tvUserInfoValue.setText(user.getGrade());
                System.out.println("年级" + user.getGrade());
            break;
            case 2:
                tvUserInfoValue.setText(user.getStuClass());
            break;
            case 3:
                tvUserInfoValue.setText(user.getDepartment());
            break;
            case 4:
                tvUserInfoValue.setText(user.getMajor());
            break;
            default:
                tvUserInfoValue.setText("暂无");
            break;
        }

        return convertView;
    }
}
