package com.wenshao.chat.fragment;

import android.app.Fragment;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wenshao.chat.R;
import com.wenshao.chat.adapter.MessageAdapter;
import com.wenshao.chat.bean.RecentContactBean;
import com.wenshao.chat.constant.HandlerCode;
import com.wenshao.chat.helper.GlobalApplication;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 2017/3/31.
 * 消息页面
 */

public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = "MessageFragment";
    private View rootView;// 缓存Fragment view
    private int status;

    private int INIT_LOAD_FINISH = 1;
    private int REFRESH = 2;

    private ListView lv_message;
    private List<RecentContactBean> recentContactBeanList;
    private Context mContext;
    private SwipeRefreshLayout spl_refresh;


    private int DOWN_REFRESH_SUC = 20;
    private int DOWN_REFRESH_ERROR = 21;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HandlerCode.LOCAL_QUERY_SUC) {
                MessageAdapter messageAdapter = new MessageAdapter(mContext, recentContactBeanList, spl_refresh);
                GlobalApplication.setRecentContactsAdapter(messageAdapter);
                lv_message.setAdapter(messageAdapter);
            } else if (msg.what == DOWN_REFRESH_SUC) {
                spl_refresh.setRefreshing(false);
            }


        }
    };

    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("params", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
            status = INIT_LOAD_FINISH;
            mContext = getActivity();
        } else {
            status = REFRESH;
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (status == INIT_LOAD_FINISH) {
            initUi();
            localData();
        }


    }

    private void initUi() {
        lv_message = (ListView) rootView.findViewById(R.id.lv_message);
        spl_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.spl_refresh);
        spl_refresh.setOnRefreshListener(this);
        spl_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }


    /**
     * 获取本地数据
     */
    private void localData(){
        recentContactBeanList = GlobalApplication.getDaoInstant().getRecentContactBeanDao().queryBuilder().list();

        mHandler.sendEmptyMessage(HandlerCode.LOCAL_QUERY_SUC);

    }

    /**
     * 获取网络数据数据
     */
    private void networkData() {
    }


    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                mHandler.sendEmptyMessage(DOWN_REFRESH_SUC);
            }
        }.start();

        Log.i(TAG, "onRefresh: ");
    }
}
