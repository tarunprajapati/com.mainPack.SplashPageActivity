package com.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.beanClasses.ChatRoomObject;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.tabGroup1.HomePageActivity;
import com.tabPack.tabGroup1.train.TrainChatRoomActivity;

public class BroadcastService  extends Service 
{
	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
	private final Handler handler = new Handler();
	Intent intent;
	int counter = 0;
	int i=0;
	private ParsingClass parsing;
	private MyApplication myApp;
	static String lastId="";
	private ArrayList<ChatRoomObject> lastMessageList;
	private RestClient responce;
	SharedPreferences sharedPreferences ;
	protected int couter=0;

	private TimerTask timerTask = new TimerTask() 
	{	

		private String userName;
		private String uuID;
		private RestClient resLogin;
		private ChatRoomObject chatObj;

		@Override
		public void run() 
		{
			couter++;
			String resmapOfUsers = parsing.listOfUsers(TrainChatRoomActivity.roomName);
			if(resmapOfUsers != null && resmapOfUsers.length() > 0)
			{
				TrainChatRoomActivity.listOfUsers = parseJSON_List_users(resmapOfUsers);
				//TrainChatRoomActivity.textV_numberOfusers.setText(TrainChatRoomActivity.listOfUsers.size()+"");
			}


			//			TrainChatRoomActivity.nextStop = (String) parsing.sendHeartBeat(MyApplication.CURRENT_LAT+"" , MyApplication.CURRENT_LON+"", TrainChatRoomActivity.roomName);
			TrainChatRoomActivity.nextStop = (String) parsing.sendHeartBeat("41.980041" , "-87.658740", TrainChatRoomActivity.roomName);
			if(TrainChatRoomActivity.nextStop != null && TrainChatRoomActivity.nextStop.length() > 0 && TrainChatRoomActivity.nextStop != "404")
			{
				ArrayList<HashMap<String, String>> nextStopList = parseJSON_List_users(TrainChatRoomActivity.nextStop);
				try 
				{
					TrainChatRoomActivity.nextStopName = nextStopList.get(0).get("ns");
					chatObj  = new ChatRoomObject();

					//	TrainChatRoomActivity.listOfChat.add(object);
				} 
				catch (Exception e) 
				{					
					e.printStackTrace();
				}
			}

			if(HomePageActivity.lastMessageId != null)
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
			}
			sendBroadcast(intent);
		}		
	};
	private Timer timer;
	private ChatRoomObject chatObj;
	private JSONArray jsonArr;

	@Override
	public void onCreate() 
	{
		System.out.println("Service onCreate() ");
		super.onCreate();
		parsing = new ParsingClass();
		myApp = (MyApplication) getApplicationContext();
		intent = new Intent(BROADCAST_ACTION);
		timer = new Timer();
		timer.schedule(timerTask, 0, 5000);
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		System.out.println("service onStart() ");
		/*handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 15000); // 1 second   
		 */	}
	@Override
	public boolean stopService(Intent name) 
	{	
		return super.stopService(name);
	}


	/*	private Runnable sendUpdatesToUI = new Runnable() 
	{
		public void run() 
		{
			System.out.println("service sendUpadateUI() ");
			DisplayLoggingInfo();    		
			handler.postDelayed(this, 15000); // 5 seconds
		}
	};
	 */	

	private void DisplayLoggingInfo() 
	{
		System.out.println("service DisplayLogginInfo() ");
		Log.d(TAG, "entered DisplayLoggingInfo");
		/*		if(HomePageActivity.lastMessageId != null)
		{
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
			else if(res.getResponseCode() == 401)
			{
				responce = (String) parsing.enterRoom(TrainChatRoomActivity.roomName);
				if(responce.equals("true"))
				{
					DisplayLoggingInfo();
				}
			}
		}
		intent.putExtra("time", new Date().toLocaleString());
		intent.putExtra("counter", String.valueOf(++counter));
		intent.putExtra("I",""+(++i));
		sendBroadcast(intent);
		 */	}

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

	// JSON parsing method
	public  ArrayList<HashMap<String, String>> parseJSON_List_users(String resString)
	{
		ArrayList<HashMap<String, String>> listOfUsers = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> mapOfUsers;
		try 
		{

			JSONArray jArr;
			try {
				jArr = new JSONArray(resString);
				for(int user=0; user<jArr.length();user++)
				{
					JSONObject jsonObject = jArr.getJSONObject(user);
					Iterator keys = jsonObject.keys();
					mapOfUsers = new HashMap<String, String>();
					while (keys.hasNext()) 
					{
						String key = (String) keys.next();
						System.out.println("Key "+key);
						mapOfUsers.put(key, jsonObject.getString(key));

					}
					listOfUsers.add(mapOfUsers);
				}
			} catch (Exception e) {
				JSONObject jObj = new JSONObject(resString);
				Iterator keys = jObj.keys();
				mapOfUsers = new HashMap<String, String>();
				while (keys.hasNext()) 
				{
					String key = (String) keys.next();
					System.out.println("Key "+key);
					mapOfUsers.put(key , jObj.getString(key));					
				}
				listOfUsers.add(mapOfUsers);
				e.printStackTrace();
			}

			/*for(int user=0; user<jArr.length();user++)
				{
					JSONObject jsonObject = jArr.getJSONObject(user);
					Iterator keys = jsonObject.keys();
					mapOfUsers = new ArrayList<String>();
					while (keys.hasNext()) 
					{
						String key = (String) keys.next();
						System.out.println("Key "+key);
						mapOfUsers.add(jsonObject.getString(key));
					}
				}		*/		
			System.out.println(listOfUsers);// this map will contain your json stuff
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		return listOfUsers;
	}

	// JSON parsing method
	public  ArrayList<HashMap<String, String>> parseJSON_sendHeartBeat(String resString)
	{
		ArrayList<HashMap<String, String>> listOfUsers = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> mapOfUsers;
		try 
		{
			JSONObject jObj = new JSONObject(resString);
			Iterator keys = jObj.keys();
			mapOfUsers = new HashMap<String, String>();
			while (keys.hasNext()) 
			{
				String key = (String) keys.next();
				System.out.println("Key "+key);
				mapOfUsers.put(key , jObj.getString(key));
				if(key.equalsIgnoreCase("a"))
				{
					jsonArr = jObj.getJSONArray(key);
					if(jsonArr != null)
					{
						//Iterator heartBeanKey  = jsonArr
						chatObj = new ChatRoomObject();
						while (keys.hasNext()) 
						{
							String keyOFa = (String) keys.next();
							System.out.println("Key "+keyOFa);
							//mapOfUsers.put(key, jsonObject.getString(key));

						}
					}
				}

			}
			listOfUsers.add(mapOfUsers);


		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		System.out.println(listOfUsers);// this map will contain your json stuff	
		return listOfUsers;
	}

}