package com.wenshao.chat.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.MessageBean;
import com.wenshao.chat.helper.FaceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 2017/4/10.
 * 聊天室聊天记录适配器
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BaseAdapter> {
    private List<MessageBean> dataList = new ArrayList<>();
    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader;

    public ChatMessageAdapter(){
        //显示图片的配置
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.load_ing)
                .showImageOnFail(R.mipmap.load_fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mImageLoader = ImageLoader.getInstance();
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
            notifyItemRangeChanged(dataList.size(),list.size());
        }
        notifyDataSetChanged();
    }
    public void add(MessageBean messageBean){
        if (messageBean!=null){
            dataList.add(messageBean);
            notifyItemRangeChanged(dataList.size(),1);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case MessageBean.LOCATION_LEFT:
                return new ChatLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_left, parent, false));
            case MessageBean.LOCATION_RIGHT:
                return new ChatRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_right, parent, false));
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
        void setData(MessageBean message){

        }


    }

    private class ChatLeftViewHolder extends BaseAdapter {
        private RoundedImageView rw_head;
        private TextView tv_content;
        private RoundedImageView rw_content;
        public ChatLeftViewHolder(View view) {
            super(view);
            rw_head = (RoundedImageView) view.findViewById(R.id.rw_head);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            rw_content = (RoundedImageView) view.findViewById(R.id.rw_content);

        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String imageUrl = "http://123.207.55.204:8081/image/20111130111800760.jpg";
            mImageLoader.displayImage(imageUrl,rw_head,mOptions);
            SpannableStringBuilder sb = FaceHelper.imageToGif(tv_content, messageBean.getContent());
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)){
                tv_content.setVisibility(View.VISIBLE);
                rw_content.setVisibility(View.GONE);
                tv_content.setText(sb);
            }else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)){
                tv_content.setVisibility(View.GONE);
                rw_content.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(messageBean.getContent(),rw_content,mOptions);
            }

        }
    }

    private class ChatRightViewHolder extends BaseAdapter {
        private RoundedImageView rw_head;
        private TextView tv_content;
        private RoundedImageView rw_content;
        public ChatRightViewHolder(View view) {
            super(view);
            rw_head = (RoundedImageView) view.findViewById(R.id.rw_head);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            rw_content = (RoundedImageView) view.findViewById(R.id.rw_content);


        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String imageUrl = "http://123.207.55.204:8081/image/20111130111800760.jpg";
            mImageLoader.displayImage(imageUrl,rw_head,mOptions);
            SpannableStringBuilder sb = FaceHelper.imageToGif(tv_content, messageBean.getContent());
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)){
                tv_content.setVisibility(View.VISIBLE);
                rw_content.setVisibility(View.GONE);
                tv_content.setText(sb);
            }else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)){
                tv_content.setVisibility(View.GONE);
                rw_content.setVisibility(View.VISIBLE);

                mImageLoader.displayImage(messageBean.getContent(),rw_content,mOptions);
            }

        }
    }

}
