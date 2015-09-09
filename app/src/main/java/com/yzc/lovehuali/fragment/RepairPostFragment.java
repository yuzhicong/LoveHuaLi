package com.yzc.lovehuali.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.R;

/**
 * Created by 镜界 on 2015/8/28 0028.
 *
 * spinner联动
 * 用什么组织用户输入的报修信息
 */
public class RepairPostFragment extends Fragment {

    EditText etPerson;
    EditText etPhone;
    Spinner spDepart;
    Spinner spAddress1;
    Spinner spAddress2;
    EditText etAddress3;
    Spinner spType1;
    Spinner spType2;
    EditText etContent;
    Button btnSubmit;

    String[] waterelec;
    String[] drinkwater;
    String[] sunwater;
    String[] homeelec;
    String[] wood;
    String[] mud;


    ArrayAdapter<String> adpType2 = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repair_post, container, false);
        Log.e("Post", "3-onCreateView");

        //1实例化控件
        etPerson = (EditText) view.findViewById(R.id.et_person);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        spDepart = (Spinner) view.findViewById(R.id.sp_depart);
        spAddress1 = (Spinner) view.findViewById(R.id.sp_address1);
        spAddress2 = (Spinner) view.findViewById(R.id.sp_address2);
        etAddress3 = (EditText) view.findViewById(R.id.et_address3);
        spType1 = (Spinner) view.findViewById(R.id.sp_type1);
        spType2 = (Spinner) view.findViewById(R.id.sp_type2);
        etContent = (EditText) view.findViewById(R.id.et_content);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);

        waterelec = getResources().getStringArray(R.array.type2_1water_elec);
        drinkwater = getResources().getStringArray(R.array.type2_3drink_water);
        sunwater = getResources().getStringArray(R.array.type2_4sun_water);
        homeelec = getResources().getStringArray(R.array.type2_5home_elec);
        wood = getResources().getStringArray(R.array.type2_6wood);
        mud = getResources().getStringArray(R.array.type2_7mud);

        //        adpType2 = ArrayAdapter.createFromResource(getActivity(),R.array.type2_1water_elec, android.R.layout.simple_spinner_dropdown_item);
        adpType2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, waterelec);

        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                    postInfo();
                return true;
            }
        });
        //报修单位监听事件
        spDepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {

                    case 0://本科学生
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 1://职院
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 2://技师
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 3://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 4://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 5://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 6://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 7://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 8://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 9://
                        Log.e("spDepart", "position=" + position);
                        break;
                    case 10://
                        Log.e("spDepart", "position=" + position);
                        break;
                }
                Log.e("onItemSelected", "选择了case");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //报修地址1监听事件
        spAddress1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0://宿舍楼
                        spAddress2.setVisibility(View.VISIBLE);
                        spAddress2.setClickable(true);
                        Log.e("spAddress1", "position=" + position);
                        break;
                    case 1://暂时不支持加载行政楼的二级地址
                        //		        	spAddress2.setClickable(false);
                        spAddress2.setVisibility(View.INVISIBLE);
                        Log.e("spAddress1", "position=" + position);
                        Toast.makeText(getActivity(), "暂不支持", Toast.LENGTH_SHORT).show();
                        break;
                    case 2://公共区域
                        spAddress2.setVisibility(View.INVISIBLE);
                        Log.e("spAddress1", "position=" + position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //报修地址2点击事件
        spAddress2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0://c1
                        Log.e("spAddress2", "position=" + position);
                        break;
                    case 1://c2
                        Log.e("spAddress2", "position=" + position);
                        break;
                    case 2://c3
                        Log.e("spAddress2", "position=" + position);
                        break;
                }
                //下面还好多宿舍我就先不写了
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //报修类型1点击事件
        spType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    //在每次在case里面createFromResource感觉不太对啊
                    case 0://水电
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_1water_elec, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, waterelec);
                        break;
                    case 1://电卡
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);

                        break;
                    case 2://直饮水
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_3drink_water, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, drinkwater);
                        break;
                    case 3://太阳能热水
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_4sun_water, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sunwater);

                        break;
                    case 4://家电
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_5home_elec, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, homeelec);

                        break;
                    case 5://木
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_6wood, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, wood);

                        break;
                    case 6://泥水
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.VISIBLE);
                        spType2.setClickable(true);
                        //						adpType2 = ArrayAdapter.createFromResource(MyApplication.getContext(),R.array.type2_7mud, android.R.layout.simple_spinner_dropdown_item);
                        adpType2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mud);

                        break;
                    case 7://电梯
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 8://电话
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 9://网络
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 10://白蚁
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 11://摄像头
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 12://清洁绿化
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                    case 13://其他
                        Log.e("spType1", "position=" + position);
                        spType2.setVisibility(View.INVISIBLE);
                        break;
                }
                spType2.setAdapter(adpType2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //报修类型2点击事件
        spType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0://初始值
                        Log.e("spType2", "position=" + position);
                        break;
                    case 1://暂时不支持加载行政楼的二级地址，并赋值
                        Log.e("spType2", "position=" + position);
                        break;
                    case 2://二级地址不可用，只剩赋值
                        Log.e("spType2", "position=" + position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //按钮点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "触发点击事件");
                if (TextUtils.isEmpty(etPerson.getText().toString().trim())) {
                    etPerson.setError("敢问阁下尊姓大名？");
                } else if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    etPhone.setError("亲,联系方式呢~");
                } else if (TextUtils.isEmpty(etAddress3.getText().toString().trim())) {
                    etAddress3.setError("咱又不查水表#_#");
                } else if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    etContent.setError("are you ok?");
                } else {
                    //把9个属性信息都发送post网络请求，哪怕有的不可用，也要交给MultipartEntityBuilder判断处理

                }
            }
        });


        return view;
    }

    public void postInfo() {
        //这里把相关信息post出去
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Post", "4-onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Post", "9-onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Post", "11-onDetach");
    }
}