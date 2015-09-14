package com.yzc.lovehuali.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yzc.lovehuali.New_Student_Activity;
import com.yzc.lovehuali.OnlineRepairActivity;
import com.yzc.lovehuali.QueryStudentScoreActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.StaticMap;
import com.yzc.lovehuali.YellowPagerActivity;
import com.yzc.lovehuali.adapter.ToolKitListViewAdapter;
import com.yzc.lovehuali.tool.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolKitFragment extends Fragment {


    private ListView lvTool;
    private ToolKitListViewAdapter adapter;
    private int clickNum=0;
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
                String noOnlineToolTalk[] = {"攻城狮正在玩命建设中……","我还没上线哦~稍等~","人家还没做好了啦！","别点了，点了也没用^_^","还点，你是傻瓜么！","点点点，就知道点！","在点，我哭给你看！","~~~~(>_<)~~~~"};
                switch (position){
                    case 0:
                        intent.setClass(getActivity(), com.zjm.library.LibrarySearchActivity.class);
                        //intent.setClass(getActivity(), LibrarySearchActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(), QueryStudentScoreActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(),YellowPagerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(getActivity(), OnlineRepairActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(getActivity(), New_Student_Activity.class);
                        startActivity(intent);
                        break;
                    case 6:

                    case 7:
                        ToastUtil.showMessage(getActivity(),noOnlineToolTalk[clickNum%8]);
                        System.out.println("点击不上线的按钮+"+ clickNum);
                        clickNum++;
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
