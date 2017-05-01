package com.wenshao.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.MessageBean;
import com.wenshao.chat.helper.FaceHelper;
import com.wenshao.chat.view.VoicePlayingView;

import java.util.ArrayList;
import java.util.List;

import android.view.ViewGroup.LayoutParams;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static com.wenshao.chat.R.attr.url;

/**
 * Created by wenshao on 2017/4/10.
 * 聊天室聊天记录适配器
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BaseAdapter> {
    private List<MessageBean> dataList = new ArrayList<>();
    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader;
    private Drawable soundDrawable;
    private OnMessageItemClickListener mOnMessageItemClickListener;
    private Context mContext;
    private HttpProxyCacheServer mProxy;
    private final static String fileRegex = "^file://.*";


    public ChatMessageAdapter(Context context ,HttpProxyCacheServer proxyCacheServer) {
        this.mContext=context;
        soundDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_action_sound);
        soundDrawable.setBounds(0,0,soundDrawable.getMinimumWidth(),soundDrawable.getMinimumHeight());
        //显示图片的配置
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.load_ing)
                .showImageOnFail(R.mipmap.load_fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mImageLoader = ImageLoader.getInstance();
        mProxy=proxyCacheServer;

    }

    public void replaceAll(List<MessageBean> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<MessageBean> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
        notifyDataSetChanged();
    }

    public void add(MessageBean messageBean) {
        if (messageBean != null) {
            dataList.add(messageBean);
            notifyItemRangeChanged(dataList.size(), 1);
        }
        notifyDataSetChanged();
    }

    public List<MessageBean> getDataList() {
        return dataList;
    }

    @Override
    public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MessageBean.LOCATION_LEFT:
                return new ChatLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_left, parent, false),mOnMessageItemClickListener);
            case MessageBean.LOCATION_RIGHT:
                return new ChatRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_right, parent, false),mOnMessageItemClickListener);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseAdapter holder, int position) {
        holder.setData(dataList.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getLocation();
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(MessageBean message) {

        }


    }

    private class ChatLeftViewHolder extends BaseAdapter implements View.OnClickListener {
        private RoundedImageView rw_head;
        private TextView tv_content;
        private RoundedImageView rw_content;
        private VoicePlayingView vpv_audio;

        private OnMessageItemClickListener mListener;


        public ChatLeftViewHolder(View view,OnMessageItemClickListener listener) {
            super(view);
            rw_head = (RoundedImageView) view.findViewById(R.id.rw_head);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            rw_content = (RoundedImageView) view.findViewById(R.id.rw_content);
            vpv_audio = (VoicePlayingView) view.findViewById(R.id.vpv_audio);
            mListener=listener;
            view.setOnClickListener(this);

        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String imageUrl = "http://123.207.55.204:8081/image/20111130111800760.jpg";
            mImageLoader.displayImage(imageUrl, rw_head, mOptions);
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)) {
                SpannableStringBuilder sb = FaceHelper.imageToGif(tv_content, messageBean.getContent());

                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText(sb);
            } else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)) {
                tv_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);

                rw_content.setVisibility(View.VISIBLE);

                mImageLoader.displayImage(messageBean.getContent(), rw_content, mOptions);
            } else if (messageBean.getType().equals(MessageBean.TYPE_AUDIO)) {
                tv_content.setVisibility(View.GONE);
                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.VISIBLE);
                vpv_audio.setDuration(messageBean.getDuration());
                String proxyUrl = mProxy.getProxyUrl(messageBean.getContent());

                vpv_audio.setMediaPlay(proxyUrl);
            }

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onMessageItemClick(v,getAdapterPosition());
            }
        }
    }

    private class ChatRightViewHolder extends BaseAdapter implements View.OnClickListener {
        private RoundedImageView rw_head;
        private TextView tv_content;
        private RoundedImageView rw_content;
        private VoicePlayingView vpv_audio;
        private OnMessageItemClickListener mListener;

        public ChatRightViewHolder(View view,OnMessageItemClickListener listener) {
            super(view);
            RelativeLayout ry_message_main = (RelativeLayout) view.findViewById(R.id.ry_message_main);

            /**
             * <TextView
             android:id="@+id/tv_content"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:textColor="@android:color/black"
             android:background="@drawable/aio_user_bg_nor_12"
             android:layout_toLeftOf="@+id/rw_head"
             android:layout_toStartOf="@+id/rw_head"
             android:gravity="center"/>
             */
            /*tv_content = new TextView(mContext);
            tv_content.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            tv_content.setTextColor(Color.BLACK);
            tv_content.setBackgroundResource(R.drawable.aio_user_bg_nor_12);
            tv_content.setGravity(Gravity.CENTER);
            ry_message_main.addView(tv_content);
            */


            rw_head = (RoundedImageView) view.findViewById(R.id.rw_head);

            tv_content = (TextView) view.findViewById(R.id.tv_content);
            rw_content = (RoundedImageView) view.findViewById(R.id.rw_content);


            vpv_audio = (VoicePlayingView) view.findViewById(R.id.vpv_audio);

            mListener=listener;
            view.setOnClickListener(this);

        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String imageUrl = "http://123.207.55.204:8081/image/20111130111800760.jpg";
            mImageLoader.displayImage(imageUrl, rw_head, mOptions);
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)) {
                SpannableStringBuilder sb = FaceHelper.imageToGif(tv_content, messageBean.getContent());

                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText(sb);
            } else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)) {
                tv_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);
                rw_content.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(messageBean.getContent(), rw_content, mOptions);

            } else if (messageBean.getType().equals(MessageBean.TYPE_AUDIO)){
                tv_content.setVisibility(View.GONE);
                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.VISIBLE);
                vpv_audio.setDuration(messageBean.getDuration());
                Log.i(TAG, "setData:88888888888 "+messageBean.getContent());
                String proxyUrl;
                if (messageBean.getContent().matches(fileRegex)){
                    proxyUrl=messageBean.getContent();
                }else{
                    proxyUrl=mProxy.getProxyUrl(messageBean.getContent());
                }

                Log.i(TAG, "setData:999999999 "+proxyUrl);

                vpv_audio.setMediaPlay(proxyUrl);
            }

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onMessageItemClick(v,getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnMessageItemClickListener listener){
        this.mOnMessageItemClickListener=listener;
    }
    public interface OnMessageItemClickListener{
        public void onMessageItemClick(View view, int position);
    }

}
