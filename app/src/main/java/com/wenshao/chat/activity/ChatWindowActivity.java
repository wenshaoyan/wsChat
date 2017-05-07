package com.wenshao.chat.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.wenshao.chat.R;
import com.wenshao.chat.adapter.ChatMessageAdapter;
import com.wenshao.chat.adapter.FaceVPAdapter;
import com.wenshao.chat.adapter.SmallFaceAdapter;
import com.wenshao.chat.bean.FileUploadBean;
import com.wenshao.chat.bean.FileUploadData;
import com.wenshao.chat.bean.RecentContactBean;
import com.wenshao.chat.bean.MessageBean;
import com.wenshao.chat.bean.MessageData;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.constant.SelfConstant;
import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.helper.BaseWebSocketClient;
import com.wenshao.chat.helper.BaseWebSocketHelper;
import com.wenshao.chat.helper.FaceHelper;
import com.wenshao.chat.helper.GlobalApplication;
import com.wenshao.chat.listener.WsEventListener;
import com.wenshao.chat.receiver.NewMessageReceiver;
import com.wenshao.chat.util.AudioRecordUtil;
import com.wenshao.chat.util.DisplayUtil;
import com.wenshao.chat.util.FileManageUtil;
import com.wenshao.chat.util.HttpCallback;
import com.wenshao.chat.util.HttpUtil;
import com.wenshao.chat.util.PermissionUtil;
import com.wenshao.chat.util.ToastUtil;
import com.wenshao.chat.view.CirclePlayProgress;
import com.wenshao.chat.view.CustomEditText;
import com.wenshao.chat.view.CustomRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;

import static com.wenshao.chat.R.id.ib_start_intercom;

/**
 * Created by wenshao on 2017/4/5.
 * 聊天页面
 */

public class ChatWindowActivity extends ToolBarActivity implements TextView.OnEditorActionListener, View.OnClickListener,
        CustomEditText.OnBackspacePressListener, ChatMessageAdapter.OnMessageItemClickListener, CacheListener {
    private static final String TAG = "ChatWindowActivity";
    private Context mContext;
    private FaceHelper mFaceHelper;

    private UserBean mReceiveUserBean;
    private CustomEditText et_message;
    private BaseWebSocketClient mWs;
    private NewMessageReceiver mNewMessageReceiver;
    private List<MessageBean> messageBeanList;
    private ChatMessageAdapter mChatMessageAdapter;
    private RecyclerView rv_message_list;
    private LinearLayout face_container;
    private LinearLayout record_container;
    private Dialog mRecordDialog;
    private CirclePlayProgress cpp_record_play;

    private List<View> faceViews = new ArrayList<View>();
    private ViewPager mViewPager;

    private int mLastPosition = 0;

    private static final int NETWORK_INIT_LOAD_SUC = 0x01;
    private static final int NETWORK_INIT_LOAD_ERROR = 0x02;

    private static final int LOCAL_INIT_LOAD_SUC = 0x11;
    private static final int LOCAL_INIT_LOAD_ERROR = 0x12;

    private static final int SOCKET_NEW_MSG = 0x21;


    private boolean isKeyboardShow = true;
    private AudioRecordUtil mAudioRecord;


    private Set<View> mutexView;  // 互斥的view


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NETWORK_INIT_LOAD_SUC:
                    mChatMessageAdapter.replaceAll(messageBeanList);
                    mLastPosition = mChatMessageAdapter.getItemCount() - 1;
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rv_message_list.getLayoutManager();
                    if (mLastPosition > 7) {   // 判断当前长度是否大于7 防止列表过长时  smoothScrollToPosition时间过长
                        layoutManager.setStackFromEnd(true);   //最后一个位于底部
                    } else {
                        layoutManager.setStackFromEnd(false);  // 第一个位于顶部
                    }
                    showKeyboard(et_message);
                    isKeyboardShow = true;
                    break;
                case SOCKET_NEW_MSG:

                    break;


            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        mContext = this;
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(false);  //不显示返回按钮


        initUi();
        InitFaceViewPager();
        initSocket();
        initReceiver();
        initData();

        // 图片选择多选触发事件
        getMultiListener();

        initDialog();


    }


    private void initUi() {
        et_message = (CustomEditText) findViewById(R.id.et_message);
        et_message.setOnEditorActionListener(this);
        et_message.setOnBackspacePressListener(this);

        ImageButton ib_voice = (ImageButton) findViewById(R.id.ib_voice);
        ImageButton ib_image = (ImageButton) findViewById(R.id.ib_image);
        ImageButton ib_camera = (ImageButton) findViewById(R.id.ib_camera);
        ImageButton ib_emo = (ImageButton) findViewById(R.id.ib_face);
        ImageButton ib_more = (ImageButton) findViewById(R.id.ib_more);


        ib_voice.setOnClickListener(this);
        ib_image.setOnClickListener(this);
        ib_camera.setOnClickListener(this);
        ib_emo.setOnClickListener(this);
        ib_more.setOnClickListener(this);


        face_container = (LinearLayout) findViewById(R.id.face_container);
        record_container = (LinearLayout) findViewById(R.id.record_container);
        //  添加互斥的view
        mutexView = new HashSet<>();
        mutexView.add(face_container);
        mutexView.add(record_container);


        final ImageButton ib_start_intercom = (ImageButton) findViewById(R.id.ib_start_intercom);
        ib_start_intercom.setOnClickListener(this);


        //表情下小圆点
        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);


        rv_message_list = (RecyclerView) findViewById(R.id.rv_message_list);
        //  在onCreate需要指定RecyclerView的Adapter  否则会报No adapter attached; skipping layout
        mChatMessageAdapter = new ChatMessageAdapter(mContext,getProxy());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setReverseLayout(true);  // 顺序反转
        //linearLayoutManager.setStackFromEnd(true);
        rv_message_list.setLayoutManager(linearLayoutManager);
        rv_message_list.setAdapter(mChatMessageAdapter);
        rv_message_list.setHasFixedSize(true);

        mChatMessageAdapter.setOnItemClickListener(this);


        //监听RecyclerView滚动状态
        rv_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                setScrollLocation(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        CustomRelativeLayout rl_chat_main = (CustomRelativeLayout) findViewById(R.id.rl_chat_main);
        rl_chat_main.setOnSizeChangedListener(new CustomRelativeLayout.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
                if (h > oldHeight) { // 键盘隐藏
                    hideKeyboard(et_message);
                    isKeyboardShow = false;
                } else { // 键盘显示
                    isKeyboardShow = true;
                    // 平滑滚动到指定的位置
                    if (mLastPosition > 0) rv_message_list.smoothScrollToPosition(mLastPosition);

                }

            }
        });

        et_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(v);  //显示键盘
                hiddenOutSelf(v);   // 隐藏快捷栏所有的显示
            }
        });


    }

    /**
     * 初始化表情窗口
     */
    private void InitFaceViewPager() {
        mFaceHelper = new FaceHelper(7);
        faceViews.add(smallPagerItem());
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(faceViews);
        mViewPager.setAdapter(mVpAdapter);

    }

    // 加载默认小表情
    private View smallPagerItem() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);

        List<String> subList = mFaceHelper.getAllSmallFace();
        SmallFaceAdapter smallFaceAdapter = new SmallFaceAdapter(mContext, subList);
        gridview.setAdapter(smallFaceAdapter);
        gridview.setNumColumns(mFaceHelper.getColumns());
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取表格chart_face_gv中点击所属的LinearLayout(face_vertical)下的第二个节点的text
                String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                mFaceHelper.insertFace(et_message.getText(), png);
            }
        });
        return gridview;
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void initSocket() {
        mWs = BaseWebSocketHelper.getIntent();

    }

    // 初始化广播接听事件
    private void initReceiver() {
        mNewMessageReceiver = new NewMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.wenshao.chat.newMsg");
        intentFilter.addAction("com.wenshao.chat.onReconnect");
        intentFilter.addAction("com.wenshao.chat.responseMsg");
        registerReceiver(mNewMessageReceiver, intentFilter);
        mNewMessageReceiver.setOnWsEventListener(new ChatWsEventListener());

    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

    }


    private class ChatWsEventListener extends WsEventListener {

        @Override
        public void reconnect() {
            initSocket();
        }

        @Override
        public void newMsg(MessageBean messageBean) {
            super.newMsg(messageBean);
            if (mReceiveUserBean.getUser_id().equals(messageBean.getSend_id())){
                messageBean.setUserBean(mReceiveUserBean);
                messageBean.setLocation(MessageBean.LOCATION_LEFT);
                mChatMessageAdapter.add(messageBean);
                mLastPosition = mChatMessageAdapter.getItemCount();
                rv_message_list.smoothScrollToPosition(mLastPosition);
            }

        }
        @Override
        public void responseMsg(String str) {
            Log.i(TAG, "responseMsg: "+str);
            super.responseMsg(str);
        }
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("receive_id", mReceiveUserBean.getUser_id());
        // TODO 检查本地缓存是否存在记录

        HttpUtil.syncPost(getApplication(), UrlConstant.getInstance().messageFriend(), map, new HttpCallback<MessageData>() {
            @Override
            public void onSuccess(MessageData resultType) {
                super.onSuccess(resultType);
                resultType.integration();
                messageBeanList = resultType.getMessageList();
                mHandler.sendEmptyMessage(NETWORK_INIT_LOAD_SUC);

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                mHandler.sendEmptyMessage(NETWORK_INIT_LOAD_ERROR);
            }
        });
    }

    /**
     * 多选事件都会在这里执行
     */
    public void getMultiListener() {
        //得到多选的事件
        RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {

            @Override
            public void selectedImg(Object t, boolean isChecked) {


            }

            @Override
            public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                ToastUtil.show(getBaseContext(), "你最多只能选择" + maxSize + "张图片");
            }
        });
    }

    /**
     * 初始化录音弹出窗口
     */
    private void initDialog() {
        final String audioPath = FileManageUtil.getAudioPath() + "audio.amr";
        View view = View.inflate(this, R.layout.record_dialog, null);
        mRecordDialog = new Dialog(this, R.style.DialogStyle);
        mRecordDialog.setContentView(view);


        Window window = mRecordDialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = DisplayUtil.dip2px(mContext, 250);

        window.setAttributes(lp);

        Button btn_record_cancel = (Button) view.findViewById(R.id.btn_record_cancel);
        Button btn_record_send = (Button) view.findViewById(R.id.btn_record_send);
        TextView tv_timer = (TextView) view.findViewById(R.id.tv_timer);

        cpp_record_play = (CirclePlayProgress) view.findViewById(R.id.cpp_record_play);
        btn_record_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordDialog.dismiss();
                mAudioRecord.reset();
                cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);

            }
        });
        btn_record_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploadBean fileUploadBean = new FileUploadBean();
                fileUploadBean.setOriginalPath(audioPath);
                mAudioRecord.reset();
                uploadAudio(fileUploadBean);
                mRecordDialog.dismiss();

                cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);


            }
        });
        cpp_record_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord(v);

            }
        });
        mAudioRecord = new AudioRecordUtil(audioPath, tv_timer, cpp_record_play);



        mAudioRecord.setOnPlayPressListener(new AudioRecordUtil.OnPlayEventListener() {
            @Override
            public void onPlayAutoEnd() {
                Log.i(TAG, "onPlayAutoEnd: ");
                cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

            }
        });


    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        mReceiveUserBean = (UserBean) getIntent().getSerializableExtra("userBean");

        super.onCreateCustomToolBar(toolbar);
        getLayoutInflater().inflate(R.layout.toobar_chat_window, toolbar);

        TextView tv_back = (TextView) toolbar.findViewById(R.id.tv_back);
        TextView tv_user_name = (TextView) toolbar.findViewById(R.id.tv_user_name);
        tv_user_name.setText(mReceiveUserBean.getName());
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_SEND == actionId) {  //发送按钮
            sendMessage();
        }
        return true;
    }

    /**
     * 构建消息 JSON
     */
    private void sendMessage() {
        String text = et_message.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            ToastUtil.show(mContext, "文本内容不能为空");
            return;
        }
        MessageBean messageBean = new MessageBean();

        et_message.setText("");  // 清空输入框


        // 添加到mChatMessageAdapter中
        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
        messageBean.setType(MessageBean.TYPE_TEXT);
        messageBean.setContent(text);
        messageBean.setUserBean(SelfConstant.getUserBean());
        messageBean.setSend_id(SelfConstant.getUserBean().getUser_id());
        messageBean.setReceive_id(mReceiveUserBean.getUser_id());
        messageBean.setCreate_time(new Date().getTime());


        sendMsg(messageBean);
        mChatMessageAdapter.add(messageBean);
        mLastPosition = mChatMessageAdapter.getItemCount();
        // 平滑的滚动  在键盘打开的情况下必须用smoothScrollToPosition模拟滚动 使用scrollToPosition无效
        rv_message_list.smoothScrollToPosition(mLastPosition);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNewMessageReceiver);
        GlobalApplication.getProxy(getApplicationContext()).unregisterCacheListener(this);

    }


    /**
     * 隐藏键盘
     *
     * @param v 点击的控件
     */
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isKeyboardShow) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            v.setFocusable(false);
        }

    }

    /**
     * 显示键盘 并设置焦点
     *
     * @param v 点击的控件
     */
    private void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!isKeyboardShow) {
            Log.i(TAG, "showKeyboard: ");
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.findFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置滚动的item位置
     */
    private void setScrollLocation(RecyclerView recyclerView, int newState) {
        //当前状态为停止滑动状态SCROLL_STATE_IDLE时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //et_message.setFocusable(true);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                mLastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                mLastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                mLastPosition = findMax(lastPositions);
            }

            //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了
                    /*if (mLastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                        Toast.makeText(ChatWindowActivity.this, "滑动到底了", Toast.LENGTH_SHORT).show();
                    }*/
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (!isEditText(v, ev)) {

                hideKeyboard(v);
                //hideAllShortcut();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }


    /**
     * 判断当前点击是否为输入框
     *
     * @param v     点击的控件
     * @param event 点击事件
     * @return true:点击的为输入框 false其他控件
     */
    private boolean isEditText(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_voice:
                if (record_container.getVisibility() == View.GONE) {
                    Log.i(TAG, "onClick: open");
                    record_container.setVisibility(View.VISIBLE);
                    hiddenOutSelf(record_container);
                } else {
                    record_container.setVisibility(View.GONE);
                }
                PermissionUtil.requestPermission(this, Manifest.permission.RECORD_AUDIO);
                PermissionUtil.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.ib_image:
                chooseImage();  // 选择照片
                break;
            case R.id.ib_camera:
                startActivity(new Intent(mContext, TestActivity.class));
                break;
            case R.id.ib_face:   //打开表情仓库
                if (face_container.getVisibility() == View.GONE) {
                    Log.i(TAG, "onClick: open");
                    face_container.setVisibility(View.VISIBLE);
                    hiddenOutSelf(face_container);
                } else {
                    face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.ib_more:
                break;
            case ib_start_intercom: // 对讲开始录音
                startRecord(view);


            default:
                break;
        }
    }

    // 隐藏除自己之外的控件
    private void hiddenOutSelf(View self) {
        for (View view : mutexView) {
            if (self != view) {
                view.setVisibility(View.GONE);
            }
        }

    }

    private void chooseImage() {
        RxGalleryFinal
                .with(mContext)
                .image()
                .multiple()
                .maxSize(8)
                .imageLoader(ImageLoaderType.UNIVERSAL)
                .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        //Toast.makeText(getBaseContext(), "已选择" + imageMultipleResultEvent.getResult().size() + "张图片", Toast.LENGTH_SHORT).show();
                        List<MediaBean> result = imageMultipleResultEvent.getResult();
                        List<FileUploadBean> fileList = new ArrayList<>();
                        for (MediaBean mediaBean : result) {
                            FileUploadBean fileUploadBean = new FileUploadBean();
                            fileUploadBean.setOriginalPath(mediaBean.getOriginalPath());
                            fileList.add(fileUploadBean);
                        }
                        uploadImage(fileList);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
                    }
                })
                .openGallery();
    }


    /**
     * 上传多个图片
     */
    private void uploadImage(List<FileUploadBean> list) {
        buildImage(list);
        HttpUtil.syncMultiFileUpdate(getApplication(), UrlConstant.IMAGE_UPLOAD, list, new HttpCallback<FileUploadData>() {
            @Override
            public void onSuccess(FileUploadData resultType) {

                for (FileUploadBean fileUploadBean : resultType.getMsg()) {
                    sendMultiMsg(fileUploadBean.getFilePath(), MessageBean.TYPE_IMAGE, fileUploadBean.getSendCode());
                }


            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i(TAG, "onError: " + throwable.getMessage());
            }
        });
    }

    // 上传单个语音文件
    private void uploadAudio(FileUploadBean fileUploadBean) {
        final MessageBean messageBean = buildAudio(fileUploadBean);
        HttpUtil.syncSingleFileUpdate(getApplication(), UrlConstant.AUDIO_UPLOAD, fileUploadBean, new HttpCallback<FileUploadData>() {
            @Override
            public void onSuccess(FileUploadData resultType) {
                for (FileUploadBean fileUploadBean : resultType.getMsg()) {
                    messageBean.setContent(fileUploadBean.getFilePath());
                    sendMsg(messageBean);
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i(TAG, "onError: " + throwable.getMessage());
            }
        });
    }

    private MessageBean buildAudio(FileUploadBean fileUploadBean) {

        MessageBean messageBean = new MessageBean();
        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
        messageBean.setType(MessageBean.TYPE_AUDIO);
        messageBean.setContent("file://"+fileUploadBean.getOriginalPath());
        messageBean.setUserBean(SelfConstant.getUserBean());
        messageBean.setSend_id(SelfConstant.getUserBean().getUser_id());
        messageBean.setSendCode(fileUploadBean.getSendCode());
        messageBean.setDuration(mAudioRecord.getDuration());
        messageBean.setReceive_id(mReceiveUserBean.getUser_id());
        messageBean.setCreate_time(new Date().getTime());
        //messageBean.setDuration();
        mChatMessageAdapter.add(messageBean);
        mLastPosition = mChatMessageAdapter.getItemCount();
        rv_message_list.smoothScrollToPosition(mLastPosition);
        return messageBean;
    }


    private List<MessageBean> buildImage(List<FileUploadBean> list) {
        List<MessageBean> messageList = new ArrayList<>();
        for (FileUploadBean fileUploadBean : list) {
            MessageBean messageBean = new MessageBean();
            messageBean.setLocation(MessageBean.LOCATION_RIGHT);
            messageBean.setType(MessageBean.TYPE_IMAGE);
            messageBean.setContent("file://" + fileUploadBean.getOriginalPath());
            messageBean.setUserBean(SelfConstant.getUserBean());
            messageBean.setSend_id(SelfConstant.getUserBean().getUser_id());
            messageBean.setSendCode(fileUploadBean.getSendCode());
            messageList.add(messageBean);
        }
        mChatMessageAdapter.addAll(messageList);
        mLastPosition = mChatMessageAdapter.getItemCount();
        // 添加到mChatMessageAdapter中
        // 平滑的滚动  在键盘打开的情况下必须用smoothScrollToPosition模拟滚动 使用scrollToPosition无效
        rv_message_list.smoothScrollToPosition(mLastPosition);
        return messageList;
    }

    private void sendMsg(MessageBean messageBean) {
        JSONObject jsonObject = new JSONObject();
        String Url = messageBean.getContent();
        String content;
        String type = messageBean.getType();
        if (messageBean.getCreate_time()==0){
            messageBean.setCreate_time(new Date().getTime());

        }

        RecentContactBean recentContactBean = new RecentContactBean(messageBean);
        recentContactBean.setUserBean(mReceiveUserBean);
        GlobalApplication.addRecentContact(recentContactBean);


        try {
            if ((MessageBean.TYPE_AUDIO.equals(type) || MessageBean.TYPE_IMAGE.equals(type) )&&"http".equals(Url.substring(0,4))){
                content= new URL(Url).getPath();
            }else{
                content=Url;
            }
            jsonObject.put("receive_id", messageBean.getReceive_id());
            jsonObject.put("type", messageBean.getType());
            jsonObject.put("content", content);
            jsonObject.put("sendCode", messageBean.getSendCode());
            jsonObject.put("duration", messageBean.getDuration());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mWs.postMsg(jsonObject);


    }

    private void sendMultiMsg(String content, String type, String sendCode) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("receive_id", mReceiveUserBean.getUser_id());
            jsonObject.put("type", type);
            jsonObject.put("content", content);
            jsonObject.put("sendCode", sendCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWs.postMsg(jsonObject);
    }

    private void startRecord(View view) {

        if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_RECORD_FREE) {
            mRecordDialog.show();
            mAudioRecord.startRecord();

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_RECORDING) {
            mAudioRecord.endRecord();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_PLAY_FREE) {
            mAudioRecord.startPlay();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_PLAYING) {
            mAudioRecord.endPlay();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

        }

    }

    @Override
    public void onBackspacePressed() {
        mFaceHelper.delete(et_message.getText());
        Log.i(TAG, "onBackspacePressed: " + et_message.getText().toString());
    }
    private HttpProxyCacheServer getProxy() {
        return GlobalApplication.getProxy(getApplicationContext());
    }

    @Override
    public void onMessageItemClick(View view, int position) {
        List<MessageBean> dataList = mChatMessageAdapter.getDataList();
        MessageBean messageBean = dataList.get(position);
        if (MessageBean.TYPE_AUDIO.equals(messageBean.getType())) {  // 点击了语音类型的消息
            Log.i(TAG, "onMessageItemClick: "+messageBean.getContent());
        } else if (MessageBean.TYPE_TEXT.equals(messageBean.getType())){

        } else if (MessageBean.TYPE_IMAGE.equals(messageBean.getType())){

        }
    }

}
