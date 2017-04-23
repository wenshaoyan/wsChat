package com.wenshao.chat.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.FriendBean;
import com.wenshao.chat.bean.UserBean;

import java.util.List;

/**
 * Created by wenshao on 2017/4/4.
 * 联系人列表
 */

public class ContactAdapter implements ExpandableListAdapter {

    private Context mContext;
    private List<FriendBean>  mFriends;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public ContactAdapter(Context context, List<FriendBean>  list) {
        mContext=context;
        mFriends=list;
        //显示图片的配置
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mImageLoader = ImageLoader.getInstance();


    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mFriends.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFriends.get(groupPosition).getFriend_info_list().size();
    }

    @Override
    public  FriendBean getGroup(int groupPosition) {
        return mFriends.get(groupPosition);
    }

    @Override
    public UserBean getChild(int groupPosition, int childPosition) {
        return mFriends.get(groupPosition).getFriend_info_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(mContext, R.layout.user_group_item_view,null);
        }
        TextView tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
        TextView tv_group_number = (TextView) convertView.findViewById(R.id.tv_group_number);
        ImageView iv_triangle = (ImageView) convertView.findViewById(R.id.iv_triangle);
        tv_group_name.setText(getGroup(groupPosition).getFriend_group_name());
        tv_group_number.setText(String.valueOf(getChildrenCount(groupPosition)));

        if (!isExpanded){
            iv_triangle.setImageResource(R.drawable.ic_right_triangle);
        }else{
            iv_triangle.setImageResource(R.drawable.ic_down_triangle);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = View.inflate(mContext, R.layout.contact_item_view, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        UserBean child = getChild(groupPosition, childPosition);
        holder.tv_name.setText(child.getName());
        //holder.tv_signature.setText("lalallalalalla");
        mImageLoader.displayImage(child.getHead(),holder.rw_head,mOptions);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    private static class ViewHolder {

        private RoundedImageView rw_head;
        private TextView tv_name;
        private TextView tv_signature;


        ViewHolder(View convertView) {
            rw_head = (RoundedImageView) convertView.findViewById(R.id.rw_head);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_signature = (TextView) convertView.findViewById(R.id.tv_signature);

        }

        static ContactAdapter.ViewHolder getHolder(View convertView) {
            ContactAdapter.ViewHolder holder = (ContactAdapter.ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ContactAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
