package com.yzc.lovehuali.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yzc.lovehuali.New_Student_Activity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.adapter.ToolKitListViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToolKitFragment extends Fragment {


    private ListView lvTool;
    private ToolKitListViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tool_kit, container, false);

        lvTool = (ListView) rootView.findViewById(R.id.lvTool);
        adapter = new ToolKitListViewAdapter(getActivity(),R.layout.listview_tool_cell);
        lvTool.setAdapter(adapter);

        lvTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.setClass(getActivity(), com.zjm.library.LibrarySearchActivity.class);
                        //intent.setClass(getActivity(), LibrarySearchActivity.class);
                        startActivity(intent);
                        break;

                    case 4:
                        intent.setClass(getActivity(), New_Student_Activity.class);
                        startActivity(intent);
                        break;
                    default:
                        return;
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


}
