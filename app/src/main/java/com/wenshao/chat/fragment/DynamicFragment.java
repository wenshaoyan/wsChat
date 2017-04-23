package com.wenshao.chat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenshao.chat.R;

/**
 * Created by wenshao on 2017/3/31.
 * 动态
 */

public class DynamicFragment extends Fragment {

    public static DynamicFragment newInstance(String param1) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString("params", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public DynamicFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        Bundle bundle = getArguments();
        String params = bundle.getString("params");
        TextView tv = (TextView)view.findViewById(R.id.tv_location);
        tv.setText(params);
        return view;
    }

}
