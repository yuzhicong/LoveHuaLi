package com.yzc.lovehuali.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yzc.lovehuali.FragmentContainerActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.SchoolNoticeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {

    private ListView lvNewsList;
    public NewsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        lvNewsList = (ListView) rootView.findViewById(R.id.lvNewsList);
        NewsListAdapter adapter = new NewsListAdapter(getActivity(),R.layout.listview_tool_cell);
        lvNewsList.setAdapter(adapter);

        lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.setClass(getActivity(), SchoolNoticeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(), FragmentContainerActivity.class);
                        intent.putExtra("barTitle", "社团新闻");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(),FragmentContainerActivity.class);
                        intent.putExtra("barTitle","学院新闻");
                        startActivity(intent);
                        break;
                }
            }
        });

        return rootView;
    }
    class NewsListAdapter extends ArrayAdapter {
        private Context context;
        private int listViewCellId;
        private String NewsListName[] = {"校园通知","社团新闻","学院新闻"};
        private String NewsListDescription[] = {"放假，校园活动信息在这","社团协会的发展和活动情况","学院取得的成就展示"};
        private int NewsListIcon[] = {R.drawable.ic_new_notice,R.drawable.ic_new_association,R.drawable.ic_new_academy};
        public NewsListAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
            this.listViewCellId = resource;
        }

        @Override
        public int getCount() {
            return NewsListName.length;
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

            icon.setImageResource(NewsListIcon[position]);
            name.setText(NewsListName[position]);
            description.setText(NewsListDescription[position]);


            return convertView;
        }
    }

}
