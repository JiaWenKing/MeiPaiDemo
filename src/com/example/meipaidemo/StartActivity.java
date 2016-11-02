package com.example.meipaidemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class StartActivity extends Activity{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent=new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
				StartActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		
		
		
		
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
//		ViewPager start_vp=(ViewPager) findViewById(R.id.start_vp);
		ImageView start_img=(ImageView) findViewById(R.id.start_img);
		mhandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mhandler.sendEmptyMessage(1);
			}
		}, 2000);
	}
}
