package com.example.Adapter;

import java.util.List;

import com.example.bean.MeipaiBean;
import com.example.meipaidemo.R;
import com.example.view.CircleTransform;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private List<MeipaiBean> list;
	LayoutInflater layout;
	Context context;
	private ViewHadler vh;

	public MyAdapter(List<MeipaiBean> list, Context context) {
		super();
		this.list = list;
		layout = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		vh = null;
		if (v == null) {
			vh = new ViewHadler();
			v = layout.inflate(R.layout.video_item, null);
			
			vh.iv_fengmian = (ImageView) v.findViewById(R.id.iv_fengmian);
//			vh.tv_miaoshu = (TextView) v.findViewById(R.id.tv_miaoshu);
			vh.iv_touxiang = (ImageView) v.findViewById(R.id.iv_touxiang);
			vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
			
			android.view.ViewGroup.LayoutParams params = vh.iv_fengmian.getLayoutParams();
//			params.height=(int) (200+Math.random()*400);
//			params.width=(int) (200+Math.random()*400);
			params.width=((Activity)vh.iv_fengmian.getContext()).getWindowManager().getDefaultDisplay().getWidth()/2;
			vh.iv_fengmian.setLayoutParams(params);
			
			v.setTag(vh);
		}else {
			vh=(ViewHadler) v.getTag();
		}
		MeipaiBean bean = list.get(position);
		Picasso.with(context).load(bean.getCover_pic()).into(vh.iv_fengmian);
//		vh.tv_miaoshu.setText(bean.getCaption());
//		Picasso.with(context).load(bean.getAvatar()).into(vh.iv_touxiang);
		Picasso.with(context).load(bean.getAvatar()).transform(new CircleTransform()).into(vh.iv_touxiang);
//		ImageLoader.getInstance().displayImage(bean.getAvatar(),
//				vh.iv_touxiang);
		vh.tv_name.setText(bean.getScreen_name());
		return v;
	}

	static class ViewHadler {
		ImageView iv_fengmian;
		TextView tv_miaoshu;
		ImageView iv_touxiang;
		TextView tv_name;
	}
	

}
