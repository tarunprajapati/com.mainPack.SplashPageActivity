package com.tabPack.tabGroup1.train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.beanClasses.ChatRoomObject;
import com.beanClasses.DeviceInfoBeanClass;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.service.BroadcastService;
import com.tabPack.TabGroupActivity;
import com.tabPack.tabGroup1.HomePageActivity;
import com.tabPack.tabGroup1.TabGroup1;

import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChatRoom;
import com.tabPack.tabGroup2.FavoriteRoutesActivity;


import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TrainChatRoomActivity extends GenericClass
{
	private static final String TAG = "BroadcastTest";
	private Bundle bundle;
	private String trainName;
	private String trainService;
	private String trainColor;
	private ListView listView;
	private Button imgV_train;
	private TextView textV_trainService;
	private TextView textV_trainName;
	private ImageView btn_home;
	private ListAdapterTrainChatRoom adapter;
	private MyApplication myApp;
	public static ArrayList<ChatRoomObject> listOfChat;
	private Button btn_send_msg;
	private EditText editT_send_msg;
	private ImageButton btn_list_chatting_users;
	private RelativeLayout rel_layout_list_user;
	private ParsingClass parsing;
	public static String roomName;
	private Button btn_exitRoom;
	private Context context;
	public static ArrayList<HashMap<String , String>> listOfUsers;
	public ProgressDialog mDialog;
	private Handler handler;
	protected int whichmsg;
	private ThreadPreLoader loader;
	private SharedPreferences sharePref;
	private String userName;
	private TextView textV_current_user;
	private RelativeLayout rel_list_chatting_users;
	private TextView textV_numberOfusers;
	private Intent intent;
	private TextView textV_next_trainName;
	private RestClient restClient;
	private RestClient res;
	private LocationManager locManager;
	private String bus;
	private String busNo;
	protected static double CURRENT_LAT;
	protected static double CURRENT_LON;
	private ArrayAdapter arrAdapter;
	public static String nextStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.conversation_chat);

		// Initialize all controls 
		initializeControls();		
	}

	@Override
	public void initializeControls() 
	{
		// Bundle Object 
		bundle = getIntent().getExtras();
		if(bundle != null)
		{
			trainName = bundle.getString("trainName");
			trainService = bundle.getString("trainService");
			trainColor = bundle.getString("trainColor");
			roomName = bundle.getString("roomName");
			bus = bundle.getString("Bus");
			busNo = bundle.getString("BusNo");

		}		

		// Intent of Service
		intent = new Intent(this, BroadcastService.class);		

		// MyApplication Object
		myApp = (MyApplication) getApplicationContext();

		// SharedPrerences 
		sharePref = getSharedPreferences("DeviceInfo", 1);		
		userName = sharePref.getString("userName" , ""); 

		// Parsing Class
		parsing = new ParsingClass();

		// Initialize Controls
		btn_list_chatting_users = (ImageButton) findViewById(R.id.btn_home_train_chat_line_room_drop_down);
		rel_list_chatting_users = (RelativeLayout) findViewById(R.id.rel_drop_down_list);
		textV_current_user = (TextView) findViewById(R.id.textV_chatter_name_header);	
		textV_next_trainName = (TextView) findViewById(R.id.textV_next_train_name);
		textV_numberOfusers = (TextView) findViewById(R.id.textV_drop_down_number_users);
		btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
		btn_send_msg.requestFocus();
		btn_exitRoom = (Button) findViewById(R.id.btn_room_exit);
		editT_send_msg = (EditText) findViewById(R.id.editT_send_msg);
		listView = (ListView) findViewById(R.id.list_view_train_line_room);
		textV_trainName = (TextView) findViewById(R.id.textV_train_chat_line_room_item_titele);		
		textV_trainService = (TextView) findViewById(R.id.textV_train_chat_line_room_sub_title);		
		imgV_train = (Button) findViewById(R.id.img_train_chat_line_room_title);

		// Set Texts on Controls Train Name , Servie , Train img
		textV_current_user.setText(userName);
		textV_trainName.setText(trainName);
		textV_trainService.setText(trainService);

		try 
		{
			if(bus == null)
			{
				//imgV_train.setBackgroundDrawable(TrainChatListActivity.listOfImage.get(trainColor));
				imgV_train.setBackgroundDrawable(MyApplication.listOfImage.get(trainColor));
			}
			else
			{
				imgV_train.setText(busNo);
			}
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		listView.setSmoothScrollbarEnabled(true);
		listView.setFastScrollEnabled(true);


		listView.setOnTouchListener(new OnTouchListener() 
		{			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
						getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(TrainChatRoomActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});

		// Set Click on Listener
		btn_list_chatting_users.setOnClickListener(this);
		btn_send_msg.setOnClickListener(this);
		btn_exitRoom.setOnClickListener(this);


	}

	@Override
	protected void onStart() 
	{	
		super.onStart();
		// Start Message Service
		//startMsgService();
	}

	// OnResume  
	@Override
	protected void onResume() 
	{	
		super.onResume();		
		//Start ThreadPreLaoder
		startThreaderPreLoader();

		// Start Message Service
		startMsgService();
	}

	private void startThreaderPreLoader()
	{
		loader = new ThreadPreLoader();
		loader.execute();

		handler = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				if (whichmsg == -1) 
				{
					//ShowAlert("Please check your net");
				} 
				else if (whichmsg == 2) 
				{
					listView.setSelection(listOfChat.size()-1);
				}
			}
		};
	}

	private class ThreadPreLoader extends AsyncTask<Object, String, Void> 
	{
		public ThreadPreLoader() 
		{
			super();
		}

		protected void onPreExecute() 
		{
			mDialog = new ProgressDialog(TrainChatRoomActivity.this);
			//mDialog.setMessage("Entering Chat Room...");
			mDialog.setMessage("Getting most recent messages");
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
			Message mesg = Message.obtain();
			mesg.what = 1;
			handler.sendMessage(mesg);
		}
	}

	public boolean doTask() 
	{		
		if (getAllMessages()) 
		{
			whichmsg = 2;			
			runOnUiThread(new Runnable() 
			{				
				@Override
				public void run() 
				{
					// Fill List with Messages
					if(listOfChat != null)
					{						
						adapter = new ListAdapterTrainChatRoom(TrainChatRoomActivity.this);
						listView.setStackFromBottom(true);
						listView.setAdapter(adapter);						
					}

					// Get List Of chatting users
					String resmapOfUsers = parsing.listOfUsers(roomName);
					if(resmapOfUsers != null && resmapOfUsers.length() > 0)
					{
						listOfUsers = parseJSON_List_users(resmapOfUsers);
						textV_numberOfusers.setText(listOfUsers.size()+"");
					}

					// For testing Fix lat , lon

					/*location = getlocation_LAT_LON();
					if(location != null)
					{*/
					if(MyApplication.CURRENT_LAT != 0.0)
					{
						nextStop = (String) parsing.sendHeartBeat("41.980041", "-87.658740" , roomName);
						//nextStop = (String) parsing.sendHeartBeat(MyApplication.CURRENT_LAT+"" , MyApplication.CURRENT_LON+"", roomName);
						if(nextStop != null && nextStop.length() > 0 && nextStop != "404")
						{
							ArrayList<HashMap<String, String>> nextStopList = parseJSON_List_users(nextStop);
							try {
								textV_next_trainName.setText("Next: "+nextStopList.get(0).get("ns"));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					//}
					else
					{
						textV_next_trainName.setText("");
					}

				}
			});

		} 
		else 
		{
			whichmsg = -1;
		}
		return true;
	}

	private boolean getAllMessages() 
	{

		String res = (String) parsing.lastMessages(roomName);
		System.out.println("res Of messages "+res);		
		if(res != null && res.length() > 0 && !res.equals("false"))
		{
			listOfChat = HomePageActivity.parseJSONChatRoomDataObject(res);
			return true;
		}
		else
		{
			return false;
		}
	}


	private void ShowAlert(String title) 
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(TrainChatRoomActivity.this);
		alertbox.setMessage(title);
		// Add a neutral button to the alert box and assign a click listener
		alertbox.setNeutralButton("Ok", null);
		// show the alert box
		alertbox.show();
	}


	@Override
	public void onClick(View v) 
	{
		if(v == btn_list_chatting_users || v == rel_list_chatting_users)
		{
			System.out.println("Show list");

			runOnUiThread(new Runnable()
			{
				@Override
				public void run() {
					Dialog diamapOfUsers = new Dialog(TrainChatRoomActivity.this);
					diamapOfUsers.requestWindowFeature(Window.FEATURE_NO_TITLE);
					LayoutInflater iLayoutInflater = LayoutInflater.from(TrainChatRoomActivity.this);
					View view = iLayoutInflater.inflate(R.layout.custom_list_of_users, null);
					TextView textV_currentUser = (TextView) view.findViewById(R.id.textV_chat_room_current_user_name);
					textV_currentUser.setText(userName);
					ListView list_view = (ListView) view.findViewById(R.id.list_view_list_of_users);
					//list_view.setCacheColorHint(android.R.color.white);
					diamapOfUsers.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
					diamapOfUsers.setContentView(view);

					diamapOfUsers.show();
					if(listOfUsers != null)
					{   
						ArrayList<String> list = new ArrayList<String>();
						for(int i =0 ; i < listOfUsers.size(); i++)
						{
							list.add(listOfUsers.get(i).get("u"));
						}
						arrAdapter = new ArrayAdapter(TrainChatRoomActivity.this, R.layout.custom_row_for_list_of_users, R.id.textV_custom_user_name , list);
						arrAdapter.notifyDataSetChanged();
						list_view.setSelector(R.drawable.list_bg);					
						list_view.setAdapter(arrAdapter);
					}

				}
			});
		}
		else if(v == btn_send_msg)
		{
			if(editT_send_msg.length() > 0)
			{
				sendMessage();
			}
			//messageUpdaterListener();
		}	
		else if(v == btn_exitRoom)
		{
			exitRoom();
		}
	}

	public void exitRoom()
	{
		String myLocalRoom = TrainChatRoomActivity.roomName;
		TrainChatRoomActivity.roomName = "";
		parsing.exitRoom(myLocalRoom);
		TrainChatRoomActivity.this.finish();
		System.out.println("activity finish");
	}

	@Override
	public void onBackPressed() 
	{
		exitRoom();
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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

			System.out.println(listOfUsers);// this map will contain your json stuff
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		return listOfUsers;
	}


	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			System.out.println("onReceive() ");
			updateUI(intent);       
		}
	};
	private int lastSizeOfList = 0;
	public static String nextStopName= null;
	private static Location location;

	private void updateUI(Intent intent) 
	{
		System.out.println("updateUI() ");

		if(listOfChat != null && adapter != null && lastSizeOfList < listOfChat.size())
		{	
			lastSizeOfList = listOfChat.size();
			adapter.notifyDataSetChanged();
			listView.setSelection(listOfChat.size()-1);
		}	
		try 
		{
			textV_numberOfusers.setText(listOfUsers.size()+"");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(nextStopName != null)
		{
			textV_next_trainName.setText("Next: "+nextStopName);
		}
	}
	private void startMsgService()
	{
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
	}

	private void stopMsgService()
	{
		unregisterReceiver(broadcastReceiver);
		stopService(intent);		
	}

	@Override
	protected void onDestroy() 
	{	
		System.out.println("onDestroy of Room");
		if(TrainChatRoomActivity.this != null)
		{			
			stopMsgService();			
		}
		super.onDestroy();
	}
	private void sendMessage()
	{
		String body = editT_send_msg.getText().toString();
		editT_send_msg.setText("");
		restClient = (RestClient) parsing.postMessage(body , roomName);
		if(restClient.getResponseCode() == 200)
		{
			Toast.makeText(TrainChatRoomActivity.this, "Sent successfully.", Toast.LENGTH_SHORT).show();				
		}
		else 
		{				
			res = (RestClient) parsing.enterRoom(TrainChatRoomActivity.roomName);

			sendMessage();

			Toast.makeText(TrainChatRoomActivity.this, "Not Sent successfully.", Toast.LENGTH_SHORT).show();
		}
	}



}
