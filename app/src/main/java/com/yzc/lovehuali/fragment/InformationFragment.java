package com.yzc.lovehuali.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.ACache;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {


    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ACache mCache = ACache.get(getActivity());
        if (mCache.getAsString("courseJson") != null) {
            System.out.println(mCache.getAsString("courseJson"));
        }
        return inflater.inflate(R.layout.fragment_information, container, false);
    }


}
