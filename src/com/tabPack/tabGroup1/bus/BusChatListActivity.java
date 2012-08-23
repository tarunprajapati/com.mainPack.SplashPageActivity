package com.tabPack.tabGroup1.bus;

import java.util.ArrayList;

import android.content.Intent;
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
import com.tabPack.tabGroup1.listAdapters.ListAdapterBusChat;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChat;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;
import com.tabPack.tabGroup1.train.TrainChatListActivity;

public class BusChatListActivity extends GenericClass
{
	private Button btn_home;
	private ListView listView;
	private MyApplication myApp;
	private ArrayList<ArrayList<String>> listOfBus;
	private ListAdapterBusChat adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bus_chat_list);
		initializeControls();
	}
	
	@Override
	public void initializeControls() 
	{
		btn_home = (Button) findViewById(R.id.btn_bus_chat_home);
		listView = (ListView) findViewById(R.id.list_view_bus_chat);	

		myApp = (MyApplication) getApplication();
		listOfBus = myApp.getAllOfBus();
		

		btn_home.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		if(MyApplication.noUpdate)
		{
			BusChatListActivity.this.finish();
			MyApplication.noUpdate = false;
		}
		else if(listOfBus != null)
		{
			adapter = new ListAdapterBusChat(BusChatListActivity.this, listOfBus);
			listView.setAdapter(adapter);
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		if(v == btn_home)
		{
			myApp.setThirdTabSet(false);
			BusChatListActivity.this.finish();
			//onBackPressed();
			/*Intent intent= new Intent(BusChatListActivity.this, Tabactivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);				*/
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		System.out.println("Item Clicked");
		Intent busLineAct = new Intent(this , BusChatLineListActivity.class);
		busLineAct.putExtra("lineName", listOfBus.get(arg2).get(2));
		busLineAct.putExtra("lineService", listOfBus.get(arg2).get(8));
		busLineAct.putExtra("lineImg", listOfBus.get(arg2).get(1));
		busLineAct.putExtra("position", arg2);
		startActivity(busLineAct);
		
	/*	Intent busLineAct = new Intent(getParent() , BusChatLineListActivity.class);
		busLineAct.putExtra("lineName", listOfBus.get(arg2).get(2));
		busLineAct.putExtra("lineService", listOfBus.get(arg2).get(8));
		busLineAct.putExtra("lineImg", listOfBus.get(arg2).get(1));
		busLineAct.putExtra("position", arg2);
		
		TabGroupActivity parent = (TabGroupActivity) getParent();
		parent.startChildActivity("BusChatLineListActivity", busLineAct);*/

	}

	

}
