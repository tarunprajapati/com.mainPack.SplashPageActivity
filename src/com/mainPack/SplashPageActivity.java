package com.mainPack;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.beanClasses.DeviceInfoBeanClass;
import com.parsing.ParsingClass;
import com.service.BroadCastSericeForCallsApi;
import com.service.BroadcastService;
import com.tabPack.Tabactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class SplashPageActivity extends Activity 
{
	private Handler handler = new Handler();
	private ParsingClass parsing;
	private MyApplication myApp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_page);
		
		Thread thread = new Thread() 
		{
			@Override
			public void run() 
			{
				try 
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{				
					e.printStackTrace();
				}
				handler.post(new Runnable() 
				{
					public void run() 
					{					
						startActivity(new Intent(getApplicationContext(), Tabactivity.class));
						SplashPageActivity.this.finish();
					}
				});
			}
		};
		thread.start();
	}
}
