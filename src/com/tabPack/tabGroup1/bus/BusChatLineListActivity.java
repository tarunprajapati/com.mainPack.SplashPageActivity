package com.tabPack.tabGroup1.bus;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MyApplication;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.TabGroupActivity;
import com.tabPack.Tabactivity;
import com.tabPack.tabGroup1.HomePageActivity;
import com.tabPack.tabGroup1.listAdapters.ListAdapterBusChatLine;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChatLine;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;
import com.tabPack.tabGroup1.train.TrainChatListActivity;
import com.tabPack.tabGroup1.train.TrainChatRoomActivity;

public class BusChatLineListActivity extends GenericClass
{

	private ListView listView;
	private Button btn_home;
	private MyApplication myApp;
	private ArrayList<ArrayList<String>> listOfBus;
	private ListAdapterBusChatLine adapter;
	private TextView textV_title;
	private TextView textV_subTitle;
	private Button img_title;
	private Bundle bundle;
	private String title;
	private String subTitle;
	private ArrayList<ArrayList<ArrayList<String>>> listOfBusChatLine;
	private int position;
	private SharedPreferences sharePref;
	private String userName;
	private SharedPreferences.Editor shareEditor;
	private ParsingClass parsing;


	public static String lineImg;
	public static HashMap<String, Drawable> listOfChatLineImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bus_chat_line_list);
		initializeControls();
	}

	@Override
	public void initializeControls() 
	{
		myApp = (MyApplication) getApplication();
		listOfBus = myApp.getAllOfBus();
		listOfBusChatLine = myApp.getListBusChatLine();

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


		listView = (ListView) findViewById(R.id.list_view_bus_chat_line);
		btn_home = (Button) findViewById(R.id.btn_home_bus_chat_line);

		textV_title = (TextView) findViewById(R.id.textV_bus_chat_line_item_titele);
		textV_title.setText(title);
		textV_subTitle = (TextView) findViewById(R.id.textV_bus_chat_line_sub_title);
		textV_subTitle.setText("Bus Chat");

		img_title = (Button) findViewById(R.id.img_bus_chat_line_title);
		img_title.setBackgroundResource(R.drawable.bus_icon_bkg);
		img_title.setText(lineImg);


		listView.setOnItemClickListener(this);
		btn_home.setOnClickListener(this);
	}


	@Override
	protected void onResume() 
	{	
		super.onResume();	
		
		if(listOfBus != null)
		{
			adapter = new ListAdapterBusChatLine(BusChatLineListActivity.this, listOfBusChatLine.get(position));
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
			BusChatLineListActivity.this.finish();
		/*	Intent intent= new Intent(BusChatLineListActivity.this, Tabactivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);	*/
			MyApplication.noUpdate = true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) 
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
				@Override
				public void onClick(View v) 
				{					
					String res = (String) parsing.changeUsername(edit_newScreenName.getText().toString().trim());
					if(res.equals("true"))
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
						Toast.makeText(BusChatLineListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
					}					
				}
			});

		}
		else
		{
			goToRoom(arg2);
		}
	}

	public void goToRoom(int pos)
	{
		//cta-rail-Red-Howard

		String roomName = "cta"+"-"+"bus"+"-"+listOfBus.get(position).get(1).trim()+"-"+listOfBusChatLine.get(position).get(0).get(pos).trim();
		RestClient res = (RestClient) parsing.enterRoom(roomName);
		System.out.println("res of enter Room "+res);
		if((res != null  && res.getResponseCode() == 200))
		{
			// https://api2.transitchatterdev.com/cta-bus-1-Drexel%20Square/messages/last-50
			Intent chatRoomAct = new Intent(BusChatLineListActivity.this , TrainChatRoomActivity.class);
			chatRoomAct.putExtra("trainName", listOfBusChatLine.get(position).get(0).get(pos));
			chatRoomAct.putExtra("trainService", listOfBusChatLine.get(position).get(1).get(pos));
			chatRoomAct.putExtra("trainColor", listOfBus.get(position).get(1));
			chatRoomAct.putExtra("Bus", "Bus");
			chatRoomAct.putExtra("BusNo", lineImg);
			chatRoomAct.putExtra("roomName", roomName);

			startActivity(chatRoomAct);

			/*Intent chatRoomAct = new Intent(TrainChatLineListActivity.this , TrainChatRoomActivity.class);
			chatRoomAct.putExtra("trainName", listOfTrainsChatLine.get(position).get(0).get(pos));
			chatRoomAct.putExtra("trainService", listOfTrainsChatLine.get(position).get(1).get(pos));
			chatRoomAct.putExtra("trainColor", listOfTrains.get(position).get(1));
			chatRoomAct.putExtra("roomName", roomName);
			TabGroupActivity parentAct = (TabGroupActivity) getParent();
			parentAct.startChildActivity("TrainChatRoomActivity" , chatRoomAct);*/
		}
		else
		{
			Toast.makeText(BusChatLineListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
		}

	}

}
