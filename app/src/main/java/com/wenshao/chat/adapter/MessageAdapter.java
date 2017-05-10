package com.wenshao.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.RecentContactBean;
import com.wenshao.chat.gooview.GooViewListener;
import com.wenshao.chat.helper.GlobalApplication;
import com.wenshao.chat.util.GooViewUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 2017/4/1.
 * 消息适配器
 */

public class MessageAdapter extends BaseAdapter {
    private HashSet<Integer> mRemoved = new HashSet<Integer>();
    private List<RecentContactBean> recentContactBeanList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private SwipeRefreshLayout mSwipeRefresh;
    private Map<String, RecentContactBean> mUserIdMessage;

    public MessageAdapter(Context context, List<RecentContactBean> list, SwipeRefreshLayout refresh) {
        this.mContext = context;
        this.recentContactBeanList = list;
        mSwipeRefresh = refresh;
        mUserIdMessage = new HashMap<>();


        for (RecentContactBean l : list) {
            mUserIdMessage.put(l.getUserBean().getUser_id(), l);
        }

        //final ImageView mImageView = (ImageView) findViewById(R.id.image);
        //

        //显示图片的配置
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mImageLoader = ImageLoader.getInstance();
        //ImageLoader.getInstance().displayImage(imageUrl, mImageView, options);

    }

    public void add(RecentContactBean recentContactBean) {
        if (recentContactBeanList != null) {
            String user_id = recentContactBean.getUserBean().getUser_id();
            if (mUserIdMessage.containsKey(user_id)) {
                mUserIdMessage.get(user_id).replace(recentContactBean);
                // 写入本地数据库
                //GlobalApplication.getDaoInstant().getRecentContactBeanDao().insertOrReplace(recentContactBean);
            } else {
                recentContactBeanList.add(recentContactBean);
                mUserIdMessage.put(user_id, recentContactBean);

            }
            GlobalApplication.getDaoInstant().getRecentContactBeanDao().insertOrReplace(mUserIdMessage.get(user_id));

            notifyDataSetChanged();
        }

    }
    public void clearUnreadNumber(RecentContactBean recentContactBean) {
        if (recentContactBeanList != null) {
            recentContactBean.setUnreadNumber(0);
            GlobalApplication.getDaoInstant().getRecentContactBeanDao().update(recentContactBean);
            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return recentContactBeanList.size();
    }

    @Override
    public RecentContactBean getItem(int position) {
        return recentContactBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.message_item_view, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        RecentContactBean recentContactBean = recentContactBeanList.get(position);

        holder.tv_content.setText(recentContactBean.getLastContent());
        holder.tv_name.setText(recentContactBean.getUserBean().getName());
        holder.tv_time.setText(recentContactBean.getLastTimeString());

        //item固定小红点layout
        LinearLayout pointLayout = holder.ll_point;
        //item固定小红点
        final TextView point = holder.tv_point;

        // 加载头像
        String imageUrl = recentContactBean.getUserBean().getHead();
        mImageLoader.displayImage(imageUrl, holder.rw_head, mOptions);

        boolean visitable;
        if (recentContactBean.getUnreadNumber() == 0) {
            visitable = false;
        } else {
            visitable = !mRemoved.contains(position);

        }
        pointLayout.setVisibility(visitable ? View.VISIBLE : View.GONE);
        if (visitable) {
            point.setText(String.valueOf(recentContactBean.getUnreadNumber()));
            pointLayout.setTag(position);
            GooViewListener mGooListener = new GooViewListener(mContext, pointLayout) {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            mSwipeRefresh.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mSwipeRefresh.setEnabled(true);
                            break;
                    }
                    return super.onTouch(v, event);
                }

                @Override
                public void onDisappear(PointF mDragCenter) {
                    super.onDisappear(mDragCenter);
                    mRemoved.add(position);
                    notifyDataSetChanged();
                    GooViewUtils.showToast(mContext, "position " + position + " disappear.");
                }

                @Override
                public void onReset(boolean isOutOfRange) {
                    super.onReset(isOutOfRange);
                    notifyDataSetChanged();//刷新ListView
                    GooViewUtils.showToast(mContext, "position " + position + " reset.");
                }
            };
            //在point父布局内的触碰事件都进行监听
            pointLayout.setOnTouchListener(mGooListener);
        }
        return convertView;
    }

    private static class ViewHolder {

        private RoundedImageView rw_head;
        private TextView tv_name;
        private TextView tv_content;
        private TextView tv_time;
        private TextView tv_point;
        private LinearLayout ll_point;

        ViewHolder(View convertView) {
            rw_head = (RoundedImageView) convertView.findViewById(R.id.rw_head);
            tv_point = (TextView) convertView.findViewById(R.id.tv_point);
            ll_point = (LinearLayout) convertView.findViewById(R.id.ll_point);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        }

        static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
