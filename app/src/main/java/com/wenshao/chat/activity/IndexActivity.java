package com.wenshao.chat.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.MessageBean;
import com.wenshao.chat.bean.RecentContactBean;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.fragment.ContactFragment;
import com.wenshao.chat.fragment.DynamicFragment;
import com.wenshao.chat.fragment.MessageFragment;
import com.wenshao.chat.helper.GlobalApplication;
import com.wenshao.chat.listener.WsEventListener;
import com.wenshao.chat.receiver.NewMessageReceiver;
import com.wenshao.chat.util.ToastUtil;

import static com.wenshao.chat.R.id.rv_message_list;

/**
 * Created by wenshao on 2017/3/17.
 * 首页
 */

public class IndexActivity extends ToolBarActivity implements BottomNavigationBar.OnTabSelectedListener {
    private static final String TAG = "IndexActivity";
    private Context mContext;
    private BottomNavigationBar tn_navigation_bar;
    private MessageFragment mMessageFragment;
    private ContactFragment mContactFragment;
    private DynamicFragment mDynamicFragment;
    private Toolbar mToolbar;
    private NewMessageReceiver mNewMessageReceiver;

    private UserBean mUserBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserBean = (UserBean) getIntent().getSerializableExtra("userBean");
        setContentView(R.layout.activity_index);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);  //不显示返回按钮

        initUi();
        initFragment();
        initReceiver();

    }




    private void initUi() {
        tn_navigation_bar = (BottomNavigationBar) findViewById(R.id.tn_navigation_bar);
        tn_navigation_bar.setMode(BottomNavigationBar.MODE_DEFAULT);
        tn_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        tn_navigation_bar.setActiveColor(R.color.navActive);
        tn_navigation_bar.setInActiveColor(R.color.navInActive);
        tn_navigation_bar.setBarBackgroundColor(R.color.white);

        tn_navigation_bar
                .addItem(new BottomNavigationItem(R.mipmap.ic_action_monolog, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_action_user, "联系人"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_action_star_10, "动态"))
                .initialise();
        tn_navigation_bar.setTabSelectedListener(this);
        setDefaultFragment();
    }


    // 初始化fragment
    private void initFragment() {


    }

    // 初始化广播接听事件
    private void initReceiver() {
        mNewMessageReceiver = new NewMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.wenshao.chat.newMsg");
        intentFilter.addAction("com.wenshao.chat.onReconnect");
        intentFilter.addAction("com.wenshao.chat.responseMsg");
        registerReceiver(mNewMessageReceiver, intentFilter);
        mNewMessageReceiver.setOnWsEventListener(new IndexActivity.ChatWsEventListener());

    }


    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        mToolbar = toolbar;
        toolbar.showOverflowMenu();
        showToolbarMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNewMessageReceiver);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mMessageFragment = MessageFragment.newInstance("消息");
        transaction.replace(R.id.fl_show, mMessageFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.newInstance("消息");
                }
                showToolbarMessage();
                transaction.replace(R.id.fl_show, mMessageFragment);
                break;
            case 1:
                if (mContactFragment == null) {
                    mContactFragment = ContactFragment.newInstance("联系人");
                }
                showToolbarContact();
                transaction.replace(R.id.fl_show, mContactFragment);
                break;
            case 2:
                if (mDynamicFragment == null) {
                    mDynamicFragment = DynamicFragment.newInstance("动态");
                }
                showToolbarDynamic();
                transaction.replace(R.id.fl_show, mDynamicFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    // 消息的toolbar
    private void showToolbarMessage() {
        mToolbar.removeAllViews();  //清除原有的toolbar
        getLayoutInflater().inflate(R.layout.toobar_message, mToolbar);

        //setSupportActionBar(mToolbar);
        ImageView ib_search = (ImageView) mToolbar.findViewById(R.id.ib_search);
        RoundedImageView rw_current_user_head = (RoundedImageView) mToolbar.findViewById(R.id.rw_current_user_head);
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mUserBean.getHead(),rw_current_user_head,options);

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mContext, "aaa");
            }
        });
    }

    // 联系人的toolbar
    private void showToolbarContact() {
        mToolbar.removeAllViews();
        getLayoutInflater().inflate(R.layout.toobar_contact, mToolbar);
        TextView tv_add = (TextView) mToolbar.findViewById(R.id.tv_add);
        if (tv_add!=null){
            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext,AppendFriendsActivity.class));
                }
            });
        }


    }

    // 动态的toolbar
    private void showToolbarDynamic() {
        mToolbar.removeAllViews();

        getLayoutInflater().inflate(R.layout.toobar_dynamic, mToolbar);
        //onCreateCustomToolBar(mToolbar);

        //setSupportActionBar(mToolbar);

        /*ImageView ib_search = (ImageView) mToolbar.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mContext, "ccc");
                Log.i(TAG, "onClick: ccc");
            }
        });*/
    }

    public class ChatWsEventListener extends WsEventListener {
        @Override
        public void reconnect() {

        }
        @Override
        public void newMsg(MessageBean messageBean) {
            super.newMsg(messageBean);
            UserBean userBean = GlobalApplication.getAllUsers().get(messageBean.getSend_id());
            if (userBean!=null){
                RecentContactBean recentContactBean = new RecentContactBean(messageBean,1);
                recentContactBean.setUserBean(userBean);
                GlobalApplication.addRecentContact(recentContactBean);
            }

        }
    }
}
