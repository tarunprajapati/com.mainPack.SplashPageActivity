package com.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;


import com.application.MyApplication;
import com.beanClasses.ChatRoomObject;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.tabGroup1.HomePageActivity;
import com.tabPack.tabGroup1.train.TrainChatRoomActivity;

public class BroadCastSericeForCallsApi  extends Service 
{
	private static final String TAG = "BroadCastSericeForCallsApi";
	public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";	
	Intent intent;
	int counter = 0;
	int i=0;
	private ParsingClass parsing;	


	SharedPreferences sharedPreferences ;
	protected String lastId;
	protected ArrayList<ChatRoomObject> lastMessageList;
	protected RestClient responce;

	private TimerTask timerTask = new TimerTask() 
	{	

		private String userName;
		private String uuID;
		private RestClient resLogin;
		private RestClient restClient;

		@Override
		public void run() 
		{
			System.out.println("run() ");

			if(myApp.getDeviceMap() == null)
			{
				String resAPI_Info =(String) parsing.apiInfo();
				try 
				{
					myApp.setDeviceMap(HomePageActivity.parseJSON_API_Data_Object(resAPI_Info));
				} 
				catch (Exception e) 
				{				
					e.printStackTrace();
				}
			}

			// Get Username
			userName = sharedPreferences.getString("userName", "");
			uuID  = sharedPreferences.getString("UUID", "");

			System.out.println("userName "+userName+" UUID "+uuID);

			if(!userName.equalsIgnoreCase(""))
			{				
				resLogin = (RestClient) parsing.login(userName, uuID);
				System.out.println("Login Res "+resLogin.getResponseCode()+" : "+resLogin.getResponse());
				if(resLogin.getResponseCode() != 200)
				{
					restClient = (RestClient) parsing.logout();
					if(restClient.getResponseCode() == 401)
					{
						String resAPI_Info =(String) parsing.apiInfo();
						myApp.setDeviceMap(HomePageActivity.parseJSON_API_Data_Object(resAPI_Info));
						parsing.sessionInfo();
					}
					else
					{
						parsing.login(userName, uuID);
					}
				}

		/*		if(HomePageActivity.lastMessageId != null)
				{
					lastId = HomePageActivity.lastMessageId;
					RestClient res = (RestClient) parsing.messageSince(HomePageActivity.lastMessageId , TrainChatRoomActivity.roomName);
					System.out.println("res Of messages "+res);		
					if(res.getResponseCode() == 200)
					{
						if(!res.getResponse().replaceAll("\n", "").equals("false"))
						{
							lastMessageList = HomePageActivity.parseJSONChatRoomDataObject(res.getResponse());	

							if(lastMessageList != null && lastMessageList.size() > 0)
							{
								for(int msg = 0 ; msg < lastMessageList.size(); msg++)
								{
									TrainChatRoomActivity.listOfChat.add(lastMessageList.get(msg));
								}
							}						
						}
					}
					else if(res.getResponseCode() == 401 && !TrainChatRoomActivity.roomName.equals(""))
					{
						responce = (RestClient) parsing.enterRoom(TrainChatRoomActivity.roomName);	
						if(responce.getResponseCode() == 401)
						{
							// Shared Preferences And Editor
							sharedPreferences = getSharedPreferences("DeviceInfo", 1);

							// Get Username
							userName = sharedPreferences.getString("userName", "");
							uuID  = sharedPreferences.getString("UUID", "");

							System.out.println("userName "+userName+" UUID "+uuID);

							resLogin = (RestClient) parsing.login(userName, uuID);
							System.out.println("Login Res "+resLogin.getResponseCode());
							if(resLogin.getResponseCode() != 200)
							{
								parsing.logout();
								parsing.login(userName, uuID);
							}
						}
					}
				}*/
			}


			sendBroadcast(intent);
		}		
	};
	private Timer timer;
	private MyApplication myApp;

	@Override
	public void onCreate() 
	{
		System.out.println("Service onCreate() ");
		super.onCreate();
		// Shared Preferences And Editor
		sharedPreferences = getSharedPreferences("DeviceInfo", 1);
		parsing = new ParsingClass();
		myApp = (MyApplication) getApplicationContext();
		intent = new Intent(BROADCAST_ACTION);
		timer = new Timer();
		timer.schedule(timerTask, 0, 60000);

	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		System.out.println("service onStart() ");
	}
	@Override
	public boolean stopService(Intent name) 
	{	
		return super.stopService(name);
	}


	@Override
	public IBinder onBind(Intent intent) 
	{
		System.out.println("service onBind() ");
		return null;
	}

	@Override
	public void onDestroy() 
	{
		this.timerTask.cancel();
		super.onDestroy();
	}
}