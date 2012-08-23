package com.tabPack.tabGroup1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.application.MyApplication;
import com.beanClasses.ChatRoomObject;
import com.beanClasses.DeviceInfoBeanClass;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.mainPack.SplashPageActivity;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.service.BroadCastSericeForCallsApi;
import com.service.BroadcastService;
import com.tabPack.TabGroupActivity;
import com.tabPack.tabGroup1.bus.BusChatListActivity;
import com.tabPack.tabGroup1.train.TrainChatListActivity;

public class HomePageActivity extends GenericClass
{
	public static String lastMessageId;
	private Button btn_train;
	private Button btn_bus;
	private ParsingClass parsing;
	public ProgressDialog mDialog;
	private Handler handler;
	protected int whichmsg;
	private MyApplication myApp;
	private SharedPreferences sharedPreferences;	
	private String userName;
	private Editor sharedPrfEditor;

	private HashMap<String, ArrayList<Boolean>> mapOf_In_service_or_not;
	private ArrayList<Boolean> listOf_In_service_or_not_bus;
	private ArrayList<Boolean> listOf_In_service_or_not_rail;
	private HashMap<String, ArrayList<Boolean>> map_In_service_or_not;
	private Intent intent;
	private LinearLayout linearLayForOrientation;
	private WindowManager mWindowManager;
	private Display mDisplay;
	private boolean need_up_to_date;

	public static ArrayList<Boolean> listOf_True_or_false_of_service;	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_page);

		// Initialize Controls 
		initializeControls();

		if(myApp.getAllOfBus() == null)
		{
			// Start ThreadPreLoader here
			final ThreadPreLoader loader = new ThreadPreLoader();
			loader.execute();
		}
		// This is hanlder For ThreadPreLoader
		handler = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				startAPI_callingService();
				if (whichmsg == -1) 
				{
					//ShowAlert("Please check your net");						
				} 
				else if (whichmsg == 2) 
				{

				}
			}
		};

	}

	// Initialize Controls
	@Override
	public void initializeControls() 
	{	
		linearLayForOrientation = (LinearLayout) findViewById(R.id.rel_chatBtns);

		mWindowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

		Log.d("ORIENTATION_TEST", "getOrientation(): " + mDisplay.getOrientation());
		if(mDisplay.getOrientation() == 3 || mDisplay.getOrientation() == 1)
		{
			linearLayForOrientation.setOrientation(LinearLayout.HORIZONTAL);
		}
		else 
		{
			linearLayForOrientation.setOrientation(LinearLayout.VERTICAL);
		}


		// Application Object
		myApp = (MyApplication) getApplication();

		// Parsing Class
		parsing = new ParsingClass();

		// Shared Preferences And Editor
		sharedPreferences = getSharedPreferences("DeviceInfo", 1);

		// Get Username
		userName = sharedPreferences.getString("userName", "");
		System.out.println("userName "+userName);

		// if username hes no name then Tempuser1
		if(userName.equals(""))
		{
			sharedPrfEditor = sharedPreferences.edit();
			sharedPrfEditor.putString("userName", "TempUser1");
			sharedPrfEditor.commit();
			userName = "TempUser1";
		}

		//  Bus And Train Button
		btn_train = (Button) findViewById(R.id.btn_trainChat);
		btn_bus = (Button) findViewById(R.id.btn_busChat);

		// Service Intent 
		intent = new Intent(HomePageActivity.this , BroadCastSericeForCallsApi.class);		

		// Set on Click Listener
		btn_train.setOnClickListener(this);
		btn_bus.setOnClickListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{	
		super.onConfigurationChanged(newConfig);
		mWindowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

		Log.d("ORIENTATION_TEST", "getOrientation(): " + mDisplay.getOrientation());
		if(mDisplay.getOrientation() == 3 || mDisplay.getOrientation() == 1)
		{
			linearLayForOrientation.setOrientation(LinearLayout.HORIZONTAL);
		}
		else 
		{
			linearLayForOrientation.setOrientation(LinearLayout.VERTICAL);
		}

	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
	}
	protected boolean isVersionUpToDate() 
	{
		Map versionObject = myApp.getDeviceMap();
		int installed_App_Version;
		if(sharedPreferences.getString("min_app_ver_andr", "").toString().trim().equalsIgnoreCase(""))
		{
			sharedPrfEditor = sharedPreferences.edit();			
			sharedPrfEditor.putString("min_app_ver_andr", versionObject.get("min_app_ver_andr").toString().trim());
			sharedPrfEditor.putString("pref_app_ver_andr", versionObject.get("pref_app_ver_andr").toString().trim());
			sharedPrfEditor.commit();
			String minValue = ((sharedPreferences.getString("min_app_ver_andr", "")).toString().trim());
			minValue = minValue.replaceAll("\\.", "");
			System.out.println("min Value : "+minValue);
			installed_App_Version = Integer.valueOf(minValue);
		}
		else
		{
			String minValue = ((sharedPreferences.getString("min_app_ver_andr", "")).toString().trim());
			minValue = minValue.replaceAll("\\.", "");
			System.out.println("min Value : "+minValue);
			installed_App_Version = Integer.valueOf(minValue);
		}

		int min_App_Version = Integer.valueOf(versionObject.get("min_app_ver_andr").toString().trim().replaceAll("\\.", ""));
		int pre_App_Version = Integer.valueOf(versionObject.get("pref_app_ver_andr").toString().trim().replaceAll("\\.", ""));

		if(installed_App_Version < min_App_Version)
		{
			need_up_to_date = true;
		}
		else
		{
			need_up_to_date = false;
		}

		if(need_up_to_date)
		{
			//Recommended Update" message:@"There is a new version of TransitChatter available.
			//Required Update" message:@"We just added some new must have features! Please upgrade to the next version of TransitChatter." 
			//delegate:self cancelButtonTitle:@"Upgrade Now" otherButtonTitles:nil, nil];
			return dialog_up_to_date_verion("Required Update", "We just added some new must have features! Please upgrade to the next version of TransitChatter.", "Upgrade Now", null);
		}
		else
		{
			if(installed_App_Version == pre_App_Version)
			{
				return true;
			}
			else
			{
				return dialog_up_to_date_verion("Recommended Update", "There is a new version of TransitChatter available.", "Upgrade Now", "Later");
			}
		}
	}

	public boolean dialog_up_to_date_verion(final String title , final String msg , final String possitiveBtn , final String nagetiveBtn)
	{		
		runOnUiThread(new Runnable() 
		{			
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(getParent());
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setPositiveButton(possitiveBtn, new OnClickListener() 
				{				
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{				
						HomePageActivity.this.finish();						
						//https://play.google.com/store/apps/details?id=air.com.transitchatter.transitchatter&feature=nav_result#?t=W251bGwsMSwyLDNd
						String url = "http://play.google.com/store/apps/details?id=air.com.transitchatter.transitchatter&feature=nav_result#?t=W251bGwsMSwyLDNd";
						Intent goTogooglePlay = new Intent(Intent.ACTION_VIEW);
						goTogooglePlay.setData(Uri.parse(url));
						startActivity(goTogooglePlay);
					}
				});
				if(nagetiveBtn != null)
				{
					dialog.setNegativeButton(nagetiveBtn, new OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{				
							dialog.dismiss();
							//share
						}						
					});

				}		
				dialog.show();				
			}
		});

		return true;
	}

	public boolean callApiAndStartService()
	{	
		if(myApp.getDeviceMap() == null)
		{
			String resAPI_Info = (String) parsing.apiInfo();
			if(resAPI_Info != null)
			{
				myApp.setDeviceMap(parseJSON_API_Data_Object(resAPI_Info));				
				return isVersionUpToDate();
			}
			else 
			{			
				return false;
			}
		}
		else
		{
			return true;
		}
	}


	// JSON parsing method parse data from API info
	public static Map parseJSON_API_Data_Object(String resString)
	{
		DeviceInfoBeanClass deviceObject = null;

		Map<String, String> map=null;

		try 
		{
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(resString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator keys = jsonObject.keys();
			map = new HashMap<String, String>();
			while (keys.hasNext()) 
			{
				String key = (String) keys.next();
				System.out.println("Key "+key);
				map.put(key, jsonObject.getString(key));
			}
			System.out.println(map);// this map will contain your json stuff
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return map;
	}	


	// Thread Pre Loader with AsyncTast
	private class ThreadPreLoader extends AsyncTask<Object, String, Void> 
	{
		public ThreadPreLoader() 
		{
			super();
		}

		protected void onPreExecute() 
		{
			mDialog = new ProgressDialog(getParent());
			mDialog.setMessage("Contacting TransitChatter. Please wait...");
			mDialog.show();
		}

		protected Void doInBackground(Object... args) 
		{			
			if (doTask()) 
			{
				this.onPostExecute();
			}

			return null;
		}

		protected void onPostExecute() 
		{
			try 
			{
				mDialog.dismiss();
			} 
			catch (Exception e) 
			{				
				e.printStackTrace();
			}
			try {
				Message mesg = Message.obtain();
				mesg.what = 1;
				handler.sendMessage(mesg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// DoTast() work in background
	public boolean doTask() 
	{
		boolean flagApi = callApiAndStartService();
		if(flagApi)
		{
			boolean flag = login_N_Getting_CTA_trains_buses();
			if (flag) 
			{
				whichmsg = 2;
			} 
			else 
			{
				whichmsg = -1;
			}
		}
		else
		{
			whichmsg = -1;
			HomePageActivity.this.finish();
		}

		return true;
	}

	private void ShowAlert(String title) 
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(getParent());
		alertbox.setMessage(title);
		// Add a neutral button to the alert box and assign a click listener
		alertbox.setNeutralButton("Ok", new OnClickListener() 
		{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{	
				if(myApp.getDeviceMap() == null)
				{
					onResume();
				}
			}
		});
		// show the alert box
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean login_N_Getting_CTA_trains_buses()
	{	
		String android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
		Log.i("System out", "android_id : " + android_id);

		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		Log.i("System out", "tmDevice : " + tmDevice);
		tmSerial = "" + tm.getSimSerialNumber();
		Log.i("System out", "tmSerial : " + tmSerial);
		androidId = ""+ android.provider.Settings.Secure.getString(getContentResolver(),  android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String UUID = deviceUuid.toString();

		sharedPrfEditor = sharedPreferences.edit();
		sharedPrfEditor.putString("UUID", UUID);
		sharedPrfEditor.commit();

		Log.i("System out", "UUID : " + UUID);
		if(userName.equals("TempUser1") || myApp.getAllOfBus() == null)
		{
			RestClient res = parsing.login(userName , UUID);
			if(userName != null && res.getResponseCode() == 200)
			{
				runOnUiThread(new Runnable() 
				{				
					@Override
					public void run() 
					{
						mDialog.setMessage("Getting CTA trains & buses");					
					}
				});

				if(myApp.getAllOfTrains() == null)
				{
					String resAllRoutes = parsing.allRoutes();
					if(resAllRoutes != null)
					{
						parseJSONData(resAllRoutes);
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	public void parseJSONData(String resString)
	{
		// All Train Route
		ArrayList<ArrayList<String>> listOfTrain = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> listOfTrainChatLine = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> listOfTrainLine;

		// All Bus Route
		ArrayList<ArrayList<String>> listOfBus = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> listOfBusChatLine = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> listOfBusLine;

		mapOf_In_service_or_not = new HashMap<String , ArrayList<Boolean>>();

		listOf_In_service_or_not_bus = new ArrayList<Boolean>();
		listOf_In_service_or_not_rail = new ArrayList<Boolean>();
		ArrayList<String> listOfSingleRoute;
		try
		{
			JSONArray jArray = new JSONArray("["+resString+"]");
			System.out.println("Outer Array size "+jArray.length());
			for(int i = 0; i < jArray.length(); i++)
			{				
				JSONArray secArr = jArray.getJSONArray(i);				
				//System.out.println("inner SecArray size "+secArr.length());

				for(int j = 0; j < secArr.length(); j++)
				{	
					listOfSingleRoute = new ArrayList<String>();
					JSONArray thArr = secArr.getJSONArray(j);
					//System.out.println("subinner thirdArr size "+thArr.length());
					if(thArr.get(0).equals("bus"))
					{
						listOfBusLine =  new ArrayList<ArrayList<String>>();

						bus:	for(int k=0; k < thArr.length(); k++)
						{	
							//System.out.println("Print Object :"+thArr.get(k).toString());
							listOfSingleRoute.add(thArr.get(k).toString());
							try
							{

								JSONArray fourthArr = thArr.getJSONArray(k);
								if(k == 6)
								{
									if(fourthArr.toString().contains("true"))
									{
										listOf_In_service_or_not_bus.add(true);
									}
									else
									{
										listOf_In_service_or_not_bus.add(false);
									}
								}
								ArrayList<String> lineArray = new ArrayList<String>();
								for(int l=0; l < fourthArr.length(); l++)
								{
									//System.out.println(fourthArr.get(l).toString());
									lineArray.add(fourthArr.get(l).toString());

								}
								listOfBusLine.add(lineArray);

							}
							catch (Exception e) 
							{
								//e.printStackTrace();								
								continue;
							}
						}
						listOfBusChatLine.add(listOfBusLine);
						listOfBus.add(listOfSingleRoute);
						mapOf_In_service_or_not.put("bus", listOf_In_service_or_not_bus);
					}
					else
					{
						listOfTrainLine =  new ArrayList<ArrayList<String>>();

						train:	for(int k=0; k < thArr.length(); k++)
						{	
							//System.out.println("Print Object :"+thArr.get(k).toString());
							listOfSingleRoute.add(thArr.get(k).toString());
							try
							{
								JSONArray fourthArr = thArr.getJSONArray(k);
								if(k == 6)
								{
									if(fourthArr.toString().contains("true"))
									{
										listOf_In_service_or_not_rail.add(true);
									}
									else
									{
										listOf_In_service_or_not_rail.add(false);
									}
								}
								ArrayList<String> lineArray = new ArrayList<String>();
								for(int l=0; l < fourthArr.length(); l++)
								{
									//System.out.println(fourthArr.get(l).toString());
									lineArray.add(fourthArr.get(l).toString());
								}
								listOfTrainLine.add(lineArray);

							}
							catch (Exception e) 
							{
								//e.printStackTrace();								
								continue;
							}


						}
						listOfTrainChatLine.add(listOfTrainLine);
						listOfTrain.add(listOfSingleRoute);
						mapOf_In_service_or_not.put("rail", listOf_In_service_or_not_rail);
					}

				}

				myApp.setAllOfBus(listOfBus);
				myApp.setAllOfTrains(listOfTrain);
				myApp.setListTrainChatLine(listOfTrainChatLine);
				myApp.setListBusChatLine(listOfBusChatLine);
				myApp.setMapOf_In_service_or_not(mapOf_In_service_or_not);
				//	System.out.println("Size Of list Of In service or not for bus "+listOf_In_service_or_not_bus.size());
				//System.out.println("Size Of list Of In service or not bus "+listOf_In_service_or_not_rail.size());

				map_In_service_or_not = myApp.getMapOf_In_service_or_not();
				System.out.println("map OF in service "+map_In_service_or_not);	


				ArrayList<Boolean> listOfRailService = map_In_service_or_not.get("rail");
				ArrayList<Boolean> listOfBusService = map_In_service_or_not.get("bus");

				listOf_True_or_false_of_service = new ArrayList<Boolean>();
				for(int flag= 0 ; flag < listOfRailService.size() ; flag++)
				{
					listOf_True_or_false_of_service.add(listOfRailService.get(flag));
				}

				for(int flag= 0 ; flag < listOfBusService.size() ; flag++)
				{
					listOf_True_or_false_of_service.add(listOfBusService.get(flag));
				}

				System.out.println("list OF in service lengh "+listOf_True_or_false_of_service.size());
				System.out.println("list OF in service "+listOf_True_or_false_of_service);

			}

		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}		
	}
	public static ArrayList<ChatRoomObject> parseJSONChatRoomDataObject(String resString)
	{		
		ArrayList<ChatRoomObject> listOfChatRoomMsg = new ArrayList<ChatRoomObject>();
		ChatRoomObject chatObject;

		try
		{
			JSONArray jArray = new JSONArray(resString);
			System.out.println("Outer Array size "+jArray.length());
			for(int i = 0; i < jArray.length(); i++)
			{				
				JSONObject secArr = jArray.getJSONObject(i);				
				//System.out.println("inner SecArray size "+secArr.length());

				chatObject = new ChatRoomObject(); 
				//System.out.println(secArr.get("id")+" id");
				chatObject.setId(secArr.get("id").toString());

				//System.out.println(secArr.get("ts").toString()+" ts");
				convertIntoFormatedDate(secArr.get("ts").toString());
				chatObject.setTimeSeconds(convertIntoFormatedDate(secArr.get("ts").toString()));				

				//System.out.println(secArr.get("ty").toString()+" ty");
				chatObject.setTy(secArr.get("ty").toString());

				//System.out.println(secArr.get("a")+" a");
				chatObject.setA(secArr.get("a").toString());

				//System.out.println(secArr.get("b")+" b");
				chatObject.setBody(secArr.get("b").toString());

				try {
					//System.out.println(secArr.get("u")+" u");
					chatObject.setUserName(secArr.get("u").toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

				listOfChatRoomMsg.add(chatObject);
			}
			lastMessageId = listOfChatRoomMsg.get(listOfChatRoomMsg.size()-1).getId();
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}		
		return listOfChatRoomMsg;
	}
	private static String convertIntoFormatedDate(String mill){

		long currentMilliSecond = System.currentTimeMillis();
		long diff = currentMilliSecond - Long.valueOf(mill)*1000;

		//System.out.println("Print the Current :"+currentMilliSecond);
		//System.out.println("Print the Server :"+Long.valueOf(mill)*1000);
		//System.out.println("Print the Diff :"+diff);


		long oneSec = 1 * 1000;
		long oneMin = oneSec * 60;
		long oneHour = oneMin * 60;
		long oneDay = oneHour * 24;
		long oneWeek = oneDay * 7;
		long oneMonth = oneDay *30;
		long twoMonth = oneMonth * 2;
		String printTime=""; 
		if(diff < oneMin)
		{
			return printTime +"few seconds ago";
		}
		else if(diff > oneMin && diff < oneHour)
		{
			return printTime =(diff/1000)/60 +" min ago";			
		}
		else if(diff > oneHour && diff < oneDay)
		{
			return  printTime =((diff/1000)/60)/60 +" hour ago";		
		}
		else if(diff > oneDay && diff < oneWeek)
		{
			return printTime =(((diff/1000)/60)/60)/24 +" days ago";	
		}
		else if(diff > oneWeek && diff < oneMonth)
		{
			return printTime =((((diff/1000)/60)/60)/24)/7+" week ago";
		}
		else if(diff > oneMonth && diff < twoMonth)
		{
			return "1 month ago";
		}
		else if(diff > twoMonth)
		{
			return "more then 1 month ago";
		}

		return printTime;
	}

	@Override
	public void onClick(View v) 
	{		
		if(v == btn_bus)
		{			
			Intent busChatAct = new Intent(this , BusChatListActivity.class);
			startActivity(busChatAct);
			/*Intent busChatAct = new Intent(getParent() , BusChatListActivity.class);
			TabGroupActivity parent = (TabGroupActivity) getParent();
			parent.startChildActivity("TrainChatListActivity", busChatAct);	*/		
		}
		else if(v == btn_train)
		{
			Intent trainChatAct = new Intent(this, TrainChatListActivity.class);
			startActivity(trainChatAct);

			/*Intent trainChatAct = new Intent(getParent() , TrainChatListActivity.class);
			TabGroupActivity parent = (TabGroupActivity) getParent();
			parent.startChildActivity("TrainChatListActivity", trainChatAct);*/
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{	

	}

	// Start Service that calls login and apiinfo api
	private void startAPI_callingService()
	{
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadCastSericeForCallsApi.BROADCAST_ACTION));
	}

	private void stopAPI_callingService()
	{
		if(broadcastReceiver.isInitialStickyBroadcast())
		{
			unregisterReceiver(broadcastReceiver);
			stopService(intent);
		}
	}	

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			System.out.println("onReceive() ");	

		}
	};


	@Override
	protected void onDestroy() 
	{	
		System.out.println("OnDestroy Home");
		super.onDestroy();
		if(mDialog != null)
		{
			mDialog.dismiss();
		}
		stopAPI_callingService();
	}

}
