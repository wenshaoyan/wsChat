package com.wenshao.chat.fragment;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.wenshao.chat.R;
import com.wenshao.chat.activity.ChatWindowActivity;
import com.wenshao.chat.adapter.ContactAdapter;
import com.wenshao.chat.bean.FriendBean;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.bean.UserFriendsData;
import com.wenshao.chat.constant.HandlerCode;
import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.helper.GlobalApplication;
import com.wenshao.chat.util.HttpCallback;
import com.wenshao.chat.util.HttpUtil;

import java.util.List;

/**
 * Created by wenshao on 2017/3/31.
 * 联系人
 */

public class ContactFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "ContactFragment";
    private Context mContext;
    private Application mApplication;

    private View rootView;// 缓存Fragment view
    private ExpandableListView elv_user_list;
    private SwipeRefreshLayout spl_refresh;
    private List<FriendBean> mFriends;




    private int DOWN_REFRESH_SUC=30;  // 网络刷新成功


    private int status = 0;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==HandlerCode.LOCAL_QUERY_SUC){
                ContactAdapter contactAdapter = new ContactAdapter(mContext, mFriends);
                elv_user_list.setAdapter(contactAdapter);
            }else if (msg.what==DOWN_REFRESH_SUC){
                spl_refresh.setRefreshing(false);

            }
        }
    };


    public static ContactFragment newInstance(String param1) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString("params", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        }

        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (status == 0) {   // 第一次初始化
            mContext = getActivity();
            mApplication = getActivity().getApplication();

            initUi();
            localData();
            status = 1;

        }

    }


    private void initUi() {
        elv_user_list = (ExpandableListView) rootView.findViewById(R.id.elv_user_list);
        spl_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.spl_refresh);
        spl_refresh.setOnRefreshListener(this);
        spl_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        elv_user_list.setGroupIndicator(null);
        elv_user_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(mContext, ChatWindowActivity.class);
                UserBean userBean = mFriends.get(groupPosition).getFriend_info_list().get(childPosition);
                Bundle bundle=new Bundle();
                bundle.putSerializable("userBean",userBean);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
//        elv_user_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                ImageView iv_triangle = (ImageView) v.findViewById(R.id.iv_triangle);
//                iv_triangle.setImageResource(R.mipmap.ic_down_triangle);
//                Log.i(TAG, "onGroupClick: "+id);
//                return false;
//            }
//        });
    }

    /**
     * 获取本地数据
     */
    private void localData(){
        mFriends = GlobalApplication.getDaoInstant().getFriendBeanDao().queryBuilder().list();

        mHandler.sendEmptyMessage(HandlerCode.LOCAL_QUERY_SUC);

    }
    // 加载网络数据
    private void networkData() {
        HttpUtil.syncPost(mApplication, UrlConstant.getInstance().userInfo(), null, new HttpCallback<UserFriendsData>() {
            @Override
            public void onSuccess(UserFriendsData resultType) {
                super.onSuccess(resultType);
                Message message = new Message();
                message.what= HandlerCode.HTTP_REQUEST_SUC;
                message.obj=resultType;
                mHandler.sendMessage(message);


            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    @Override
    public void onRefresh() {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(2000);
                mHandler.sendEmptyMessage(DOWN_REFRESH_SUC);
            }
        }.start();

        Log.i(TAG, "onRefresh: ");
    }
}
