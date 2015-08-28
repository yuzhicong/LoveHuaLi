package com.yzc.lovehuali.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzc.lovehuali.R;

/**
 * Created by 镜界 on 2015/8/28 0028.
 */
public class RepairQueryFragment extends Fragment {



//    public RepairQueryFragment(){
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.repair_query,container,false);
        return view;
    }
}
