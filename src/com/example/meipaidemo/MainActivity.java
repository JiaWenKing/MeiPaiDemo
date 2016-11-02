package com.example.meipaidemo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Document;

import com.example.Adapter.MyAdapter;
import com.example.bean.Config;
import com.example.bean.MeipaiBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				list.clear();
				List<MeipaiBean> li=(List<MeipaiBean>) msg.obj;
				list.addAll(li);
				
				adapter.notifyDataSetChanged();
				break;
			case 2:
				String s=(String) msg.obj;
				Uri uri=Uri.parse(s);
				  video.setVideoURI(uri);  
		            video.setMediaController(mediaco);  
		            mediaco.setMediaPlayer(video);  
		            //让VideoView获取焦点  
		            video.requestFocus();  
		            
		            video.start();
				break;
			default:
				break;
			}
		};
	};
	
	private VideoView video;
	private MediaController mediaco;
	List<MeipaiBean> list;
	private MyAdapter adapter;
	private GridView gridview;
	private int page=1;//代表页数, 1为首页.
	int index=1;//1,搞笑   2，明星   3，旅行

	private RadioGroup radiogroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		onclick();
		list=new ArrayList<MeipaiBean>();
		adapter = new MyAdapter(list, this);
		gridview.setAdapter(adapter);
		asyn(13,page);
	}

	private void initView() {
		video = (VideoView) findViewById(R.id.video);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setOnItemClickListener(new MyonitemClick());
		gridview.setOnScrollListener(new MyonOnScroll());
		
		mediaco = new MediaController(this);
		
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		
	}
	/**
	 * 线程2视频播放
	 * */
	private void video(final String s) {
		
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					org.jsoup.nodes.Document doc= Jsoup.connect(s).get();//解析链接
					Element mp4path = doc.head();
					String content = mp4path.select("meta[property=\"og:video:url\"]").attr("content");
					Message message = mhandler.obtainMessage();
					message.obj=content;
					message.what=2;
					mhandler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	/**
	 * 线程1请求数据
	 * */
	private void asyn(int id,int page) {
		// TODO Auto-generated method stub
		AsyncHttpClient ac=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		
		params.put("id", id);
		params.put("page", page);
		ac.get(Config.ip+Config.gx, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
				String str=new String(arg2);
				Gson gson=new Gson();
				List<MeipaiBean> data=gson.fromJson(str,new TypeToken<List<MeipaiBean>>(){}.getType());
				Message message = mhandler.obtainMessage();
				message.obj=data;
				message.what=1;
				mhandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * gridview监听
	 * */
	class MyonitemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
//			MeipaiBean item=(MeipaiBean) adapter.getItem(position);
//			String url = item.getUrl();
			MeipaiBean bean = list.get(position);
			Uri url=Uri.parse(bean.getUrl());
			video.setVideoURI(url);
			String u = bean.getUrl();
			video(u);
			
		}
		
	}
	/**
	 * 监听上拉刷新
	 * */
	class MyonOnScroll implements OnScrollListener{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (OnScrollListener.SCROLL_STATE_TOUCH_SCROLL==scrollState) {
				//已经到底部
					if (view.getLastVisiblePosition()==view.getCount()-1) {
						page=page+1;
						if (System.currentTimeMillis()-i>500) {
							Toast.makeText(MainActivity.this, "再拉刷新下一页", 
									Toast.LENGTH_SHORT).show();
							i=System.currentTimeMillis();
						}else {
							if (index==1) {
								asyn(13, page);
							}else if (index==2) {
								asyn(16, page);
							}else if (index==3) {
								asyn(3, page);
							}
						}
					}
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * Button监听 
	 * */
	int num=0;
	private void onclick() {
		
		// TODO Auto-generated method stub
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1==R.id.rb_gaoxiao) {
					num=1;
					if (num==1) {
						list.clear();
						asyn(13,page);
						index=1;
						page=1;
						num=2;
					}else if (num==2) {
						asyn(13,page);
						index=1;
						page=1;
					}
				}else if (arg1==R.id.rb_mingxing) {
					num=3;
					if (num==3) {
						list.clear();
						asyn(16,page);
						index=2;
						page=1;
						num=4;
					}else if (num==4) {
						asyn(16,page);
						index=2;
						page=1;
					}
				}else if (arg1==R.id.rb_lvyou) {
					num=5;
					if (num==5) {
						list.clear();
						asyn(3,page);
						index=3;
						page=1;
						num=6;
					}else if (num==6) {
						asyn(3,page);
						index=3;
						page=1;
					}
					
				}
			}
		});
	}
	/**
	 * 手机退出键
	 * */
	long i=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		// TODO Auto-generated method stub
		if (keyCode==4) {
			if (System.currentTimeMillis()-i>1500) {
				Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
				i=System.currentTimeMillis();
			}else {
//				System.exit(0);
				this.finish();
			}
		}
		return true;
	}
	

}
