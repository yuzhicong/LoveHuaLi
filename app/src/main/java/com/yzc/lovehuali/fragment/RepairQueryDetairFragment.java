package com.yzc.lovehuali.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yzc.lovehuali.R;

import java.util.HashMap;

/**
 * Created by 镜界 on 2015/9/7 0007.
 */
public class RepairQueryDetairFragment extends Fragment {
    HashMap<String, String> frgItemInfoHashMap;
    TextView Number;
    TextView Depart;
    TextView Address;
    TextView Person;
    TextView Time;
    TextView Phone;
    TextView Content;
    TextView Describe;
    TextView timeFinish;
    Button btnBack;

    public RepairQueryDetairFragment(HashMap hashMap){
        frgItemInfoHashMap=hashMap;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.repair_detail,container,false);

        Number=(TextView)view.findViewById(R.id.number);
        Depart=(TextView)view.findViewById(R.id.depart);
        Address=(TextView)view.findViewById(R.id.address);
        Person=(TextView)view.findViewById(R.id.person);
        Time=(TextView)view.findViewById(R.id.time);
        Phone=(TextView)view.findViewById(R.id.phone);
        Content=(TextView)view.findViewById(R.id.content);
        Describe=(TextView)view.findViewById(R.id.describe);
        timeFinish=(TextView)view.findViewById(R.id.time_finish);
        btnBack=(Button)view.findViewById(R.id.btn_back);

        Number.setText(frgItemInfoHashMap.get("lblRepairID"));
        Depart.setText(frgItemInfoHashMap.get("lblReportDep"));
        Address.setText(frgItemInfoHashMap.get("lblRepariAddress"));
        Person.setText(frgItemInfoHashMap.get("lblReportPerson"));
        Time.setText(frgItemInfoHashMap.get("lblReportTime"));
        Phone.setText(frgItemInfoHashMap.get("lblPhone"));
        Content.setText(frgItemInfoHashMap.get("lblRepairContentID"));
        Describe.setText(frgItemInfoHashMap.get("lblCompleteDesribe"));
        timeFinish.setText(frgItemInfoHashMap.get("lblCompleteTime"));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里退出当前碎片
                Log.e("onclick", "popBackStack");
                getFragmentManager().popBackStack();

            }
        });
        return view;
    }
}
