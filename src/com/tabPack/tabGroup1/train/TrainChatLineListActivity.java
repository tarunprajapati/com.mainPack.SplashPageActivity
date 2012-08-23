package com.tabPack.tabGroup1.train;

import java.util.ArrayList;
import java.util.HashMap;

import com.application.MyApplication;
import com.beanClasses.ChatRoomObject;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.TabGroupActivity;
import com.tabPack.Tabactivity;
import com.tabPack.tabGroup1.HomePageActivity;
import com.tabPack.tabGroup1.TabGroup1;
import com.tabPack.tabGroup1.bus.BusChatLineListActivity;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChatLine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrainChatLineListActivity extends GenericClass
{

	private ListView listView;
	private Button btn_home;
	private MyApplication myApp;
	private ArrayList<ArrayList<String>> listOfTrains;
	private ListAdapterTrainChatLine adapter;
	private TextView textV_title;
	private TextView textV_subTitle;
	private ImageView img_title;
	private Bundle bundle;
	private String title;
	private String subTitle;
	private ArrayList<ArrayList<ArrayList<String>>> listOfTrainsChatLine;
	private int position;
	private ParsingClass parsing;
	private RestClient res;
	private ArrayList<ChatRoomObject> listOfChat;
	private SharedPreferences sharePref;
	private String userName;
	private SharedPreferences.Editor shareEditor;
	private int counter=0;


	public static String lineImg;
	public static HashMap<String, Drawable> listOfChatLineImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_view_train_chat_line);
		initializeControls();
	}

	@Override
	public void initializeControls() 
	{
		// MyApplication 
		myApp = (MyApplication) getApplication();

		// Get all trains list
		listOfTrains = myApp.getAllOfTrains();

		// get train line list
		listOfTrainsChatLine = myApp.getListTrainChatLine();

		// get bundle
		bundle = getIntent().getExtras();
		if(bundle != null)
		{
			title = bundle.getString("lineName");
			subTitle = bundle.getString("lineService");
			lineImg = bundle.getString("lineImg");
			position = bundle.getInt("position"); 
		}

		// get SharedPreference
		sharePref = getSharedPreferences("DeviceInfo", 1);
		//TempUser1
		userName = sharePref.getString("userName" , ""); 
		shareEditor = sharePref.edit();

		// Parsing Class
		parsing = new ParsingClass();

		// All Train Images
		listOfChatLineImage = new HashMap<String, Drawable>();
		listOfChatLineImage.put(listOfTrains.get(0).get(1) , getResources().getDrawable(R.drawable.blue_destination));
		listOfChatLineImage.put(listOfTrains.get(1).get(1) ,getResources().getDrawable(R.drawable.brn_destination));
		listOfChatLineImage.put(listOfTrains.get(2).get(1) ,getResources().getDrawable(R.drawable.g_destination));
		listOfChatLineImage.put(listOfTrains.get(3).get(1) ,getResources().getDrawable(R.drawable.org_destination));
		listOfChatLineImage.put(listOfTrains.get(4).get(1) ,getResources().getDrawable(R.drawable.p_destination));
		listOfChatLineImage.put(listOfTrains.get(5).get(1) ,getResources().getDrawable(R.drawable.pink_destination));
		listOfChatLineImage.put(listOfTrains.get(6).get(1) ,getResources().getDrawable(R.drawable.red_destination));
		listOfChatLineImage.put(listOfTrains.get(7).get(1) ,getResources().getDrawable(R.drawable.y_destination));


		// Set Controls
		listView = (ListView) findViewById(R.id.list_view_train_chat_line);
		btn_home = (Button) findViewById(R.id.btn_home_train_chat_line);
		textV_title = (TextView) findViewById(R.id.textV_train_chat_line_item_titele);	
		textV_subTitle = (TextView) findViewById(R.id.textV_train_chat_line_sub_title);
		img_title = (ImageView) findViewById(R.id.img_train_chat_line_title);
		//img_title.setBackgroundDrawable(TrainChatListActivity.listOfImage.get(lineImg));
		img_title.setBackgroundDrawable(MyApplication.listOfImage.get(lineImg));

		// Set Text On Controls Title ANd Sub Title
		textV_title.setText(title);
		textV_subTitle.setText(subTitle);

		// Set click listener
		listView.setOnItemClickListener(this);
		btn_home.setOnClickListener(this);
	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
		if(listOfTrains != null)
		{
			adapter = new ListAdapterTrainChatLine(this, listOfTrainsChatLine.get(position) , listOfChatLineImage);
			listView.setAdapter(adapter);
		}
		userName = sharePref.getString("userName" , ""); 
	}

	@Override
	public void onClick(View v) 
	{
		if(v == btn_home)
		{
			myApp.setThirdTabSet(false);
			TrainChatLineListActivity.this.finish();			
			/*startActivity(new Intent(getApplicationContext() , Tabactivity.class));	*/
		/*	Intent intent= new Intent(TrainChatLineListActivity.this, Tabactivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);*/
			MyApplication.noUpdate = true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) 
	{

		setSreenNameDialogBox(arg2);
		/*if(userName.equals("TempUser1"))
		{
			System.out.println("Print inside Dialog Box :"+userName);
			final Dialog dialogForSreenName = new Dialog(this);
			dialogForSreenName.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogForSreenName.setContentView(R.layout.change_screen_name);
			Button btn_changeScreenName = (Button) dialogForSreenName.findViewById(R.id.btn_save_screen_name);
			final EditText edit_newScreenName = (EditText) dialogForSreenName.findViewById(R.id.editT_new_screen_name);
			Button btn_cancel = (Button) dialogForSreenName.findViewById(R.id.btn_change_name_cancel);
			dialogForSreenName.show();
			btn_cancel.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{				
					dialogForSreenName.dismiss();
				}
			});
			btn_changeScreenName.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{					
					res = (String) parsing.changeUsername(edit_newScreenName.getText().toString());
					if(res.equals("true"))
					{
						System.out.println("print the EditText Name :"+edit_newScreenName.getText().toString());

						shareEditor.putString("userName", edit_newScreenName.getText().toString());
						shareEditor.commit();						
						dialogForSreenName.dismiss();						
						goToRoom(arg2);						
					}
					else
					{
						Toast.makeText(TrainChatLineListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
					}					
				}
			});

		}
		else
		{
			goToRoom(arg2);
		}*/
		/*	parsing = new ParsingClass();
		//cta-rail-Red-Howard

		String roomName = "cta"+"-"+"rail"+"-"+listOfTrains.get(position).get(1).trim()+"-"+listOfTrainsChatLine.get(position).get(0).get(arg2).trim();
		res = (String) parsing.enterRoom(roomName);
		System.out.println("res of enter Room "+res);
		if(res.equals("true"))
		{
		// https://api2.transitchatterdev.com/cta-bus-1-Drexel%20Square/messages/last-50


		Intent chatRoomAct = new Intent(TrainChatLineListActivity.this , TrainChatRoomActivity.class);
		chatRoomAct.putExtra("trainName", listOfTrainsChatLine.get(position).get(0).get(arg2));
		chatRoomAct.putExtra("trainService", listOfTrainsChatLine.get(position).get(1).get(arg2));
		chatRoomAct.putExtra("trainColor", listOfTrains.get(position).get(1));
		chatRoomAct.putExtra("roomName", roomName);
		TabGroupActivity parentAct = (TabGroupActivity) getParent();
		parentAct.startChildActivity("TrainChatRoomActivity" , chatRoomAct);*/
		//}
	}


	public void goToRoom(int pos)
	{
		//cta-rail-Red-Howard

		String roomName = "cta"+"-"+"rail"+"-"+listOfTrains.get(position).get(1).trim()+"-"+listOfTrainsChatLine.get(position).get(0).get(pos).trim();
		res = (RestClient) parsing.enterRoom(roomName);
		System.out.println("res of enter Room "+res);
		if(res != null && (res.getResponseCode() == 200) && res.getResponse().replaceAll("\n", "").equalsIgnoreCase("true"))
		{
			// https://api2.transitchatterdev.com/cta-bus-1-Drexel%20Square/messages/last-50
			Intent chatRoomAct = new Intent(TrainChatLineListActivity.this , TrainChatRoomActivity.class);
			chatRoomAct.putExtra("trainName", listOfTrainsChatLine.get(position).get(0).get(pos));
			chatRoomAct.putExtra("trainService", listOfTrainsChatLine.get(position).get(1).get(pos));
			chatRoomAct.putExtra("trainColor", listOfTrains.get(position).get(1));
			chatRoomAct.putExtra("roomName", roomName);

			startActivity(chatRoomAct);	
		}
		else
		{
			/*if(counter < 5)
			{
				goToRoom(pos);
				counter++;
			}
			else
			{*/				
				Toast.makeText(TrainChatLineListActivity.this, "Something went wrong at serverside!", Toast.LENGTH_SHORT).show();
			//}

		}

	}

	private void setSreenNameDialogBox(final int arg2)
	{
		userName = sharePref.getString("userName" , "");
		if(userName.equals("TempUser1"))
		{
			System.out.println("Print inside Dialog Box :"+userName);
			final Dialog dialogForSreenName = new Dialog(this);
			dialogForSreenName.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogForSreenName.setContentView(R.layout.change_screen_name);
			Button btn_changeScreenName = (Button) dialogForSreenName.findViewById(R.id.btn_save_screen_name);
			final EditText edit_newScreenName = (EditText) dialogForSreenName.findViewById(R.id.editT_new_screen_name);
			Button btn_cancel = (Button) dialogForSreenName.findViewById(R.id.btn_change_name_cancel);
			dialogForSreenName.show();
			btn_cancel.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{				
					dialogForSreenName.dismiss();
				}
			});
			btn_changeScreenName.setOnClickListener(new OnClickListener() 
			{				
				private String resOfChangeName;

				@Override
				public void onClick(View v) 
				{
					if(edit_newScreenName.getText().length() > 0)
					{
						resOfChangeName = (String) parsing.changeUsername(edit_newScreenName.getText().toString().trim());
						if(resOfChangeName.equals("true"))
						{
							System.out.println("print the EditText Name :"+edit_newScreenName.getText().toString());

							shareEditor.putString("userName", edit_newScreenName.getText().toString());
							shareEditor.putString("ThirdTabSet", "true");
							shareEditor.commit();						
							dialogForSreenName.dismiss();						
							goToRoom(arg2);						
						}
						else
						{
							Toast.makeText(TrainChatLineListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(TrainChatLineListActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
					}
				}
			});

		}
		else
		{
			goToRoom(arg2);
		}
	}
}
