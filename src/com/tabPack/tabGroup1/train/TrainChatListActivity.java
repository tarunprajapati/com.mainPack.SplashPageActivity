package com.tabPack.tabGroup1.train;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.application.MyApplication;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.tabPack.TabGroupActivity;
import com.tabPack.Tabactivity;
import com.tabPack.tabGroup1.bus.BusChatListActivity;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChat;

public class TrainChatListActivity extends GenericClass
{
	private Button btn_home;
	private ListView listView;
	private ListAdapterTrainChat adapter;

	//public static HashMap<String , Drawable> listOfImage;
	private MyApplication myApp;
	private ArrayList<ArrayList<String>> listOfTrains;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_train_chat);
		initializeControls();
	}

	@Override
	public void initializeControls() 
	{
		// Home And ListView Initialized
		btn_home = (Button) findViewById(R.id.btn_train_chat_home);
		listView = (ListView) findViewById(R.id.list_view_train_chat);

		// MyApplication
		myApp = (MyApplication) getApplication();

		// Get All Trains List from MyApplication
		listOfTrains = myApp.getAllOfTrains();

		/*	// Set Images for train in HashMap 
		if(listOfTrains != null)
		{
			listOfImage = new HashMap<String, Drawable>();
			listOfImage.put(listOfTrains.get(0).get(1) , getResources().getDrawable(R.drawable.blue_list));
			listOfImage.put(listOfTrains.get(1).get(1) ,getResources().getDrawable(R.drawable.brn_list));
			listOfImage.put(listOfTrains.get(2).get(1) ,getResources().getDrawable(R.drawable.g_list));
			listOfImage.put(listOfTrains.get(3).get(1) ,getResources().getDrawable(R.drawable.org_list));
			listOfImage.put(listOfTrains.get(4).get(1) ,getResources().getDrawable(R.drawable.p_list));
			listOfImage.put(listOfTrains.get(5).get(1) ,getResources().getDrawable(R.drawable.pink_list));
			listOfImage.put(listOfTrains.get(6).get(1) ,getResources().getDrawable(R.drawable.red_list));
			listOfImage.put(listOfTrains.get(7).get(1) ,getResources().getDrawable(R.drawable.y_list));
		}*/

		// Set Click on Listener
		btn_home.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}


	@Override
	protected void onResume() 
	{	
		super.onResume();
		if(MyApplication.noUpdate)
		{
			TrainChatListActivity.this.finish();
			MyApplication.noUpdate = false;
		}
		else
		{
			if(listOfTrains != null)
			{
				adapter = new ListAdapterTrainChat(TrainChatListActivity.this, listOfTrains);
				listView.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onClick(View v) 
	{	
		if(v == btn_home)
		{
			myApp.setThirdTabSet(false);
			TrainChatListActivity.this.finish();

			/*	Intent intent= new Intent(TrainChatListActivity.this, Tabactivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);		*/
		}
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{	
		System.out.println("Item Clicked");
		Intent trainLineAct = new Intent(this , TrainChatLineListActivity.class);
		trainLineAct.putExtra("lineName", listOfTrains.get(arg2).get(2));
		trainLineAct.putExtra("lineService", listOfTrains.get(arg2).get(8));
		trainLineAct.putExtra("lineImg", listOfTrains.get(arg2).get(1));
		trainLineAct.putExtra("position", arg2);

		startActivity(trainLineAct);

		/*	Intent trainLineAct = new Intent(getParent() , TrainChatLineListActivity.class);
		trainLineAct.putExtra("lineName", listOfTrains.get(arg2).get(2));
		trainLineAct.putExtra("lineService", listOfTrains.get(arg2).get(8));
		trainLineAct.putExtra("lineImg", listOfTrains.get(arg2).get(1));
		trainLineAct.putExtra("position", arg2);

		TabGroupActivity parent = (TabGroupActivity) getParent();
		parent.startChildActivity("TrainChatLineListActivity", trainLineAct);*/
	}


}
