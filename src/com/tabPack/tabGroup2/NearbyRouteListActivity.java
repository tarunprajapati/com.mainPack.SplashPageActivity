package com.tabPack.tabGroup2;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.id;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.application.MyApplication;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.tabPack.TabGroupActivity;
import com.tabPack.tabGroup1.listAdapters.ListAdapterForAllRoutes;
import com.tabPack.tabGroup2.listAdapter.ListAdapterForNearByRoute;

public class NearbyRouteListActivity extends GenericClass
{

	private Button btn_home;
	private ListView listView;
	private Bundle bundle;
	private String title;
	private ArrayList<String> listOFTrainOrBus;
	private TextView textV_Title;
	private MyApplication myApp;
	private ArrayList<ArrayList<String>> listOfBus;
	private ArrayList<ArrayList<String>> listOfTrain;
	private String check_bus_or_train;
	private ArrayList<ArrayList<String>> listOfRoute;
	private ListAdapterForNearByRoute adapter;
	private ArrayList<ArrayList<String>> makeList;
	private String bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bus_chat_list);
		initializeControls();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeControls() 
	{
		// Bundle get Title and List of Train and bus 
		bundle = getIntent().getExtras();

		if(bundle != null)
		{
			title = bundle.getString("titleRoute");
			listOFTrainOrBus = (ArrayList<String>) bundle.getSerializable("listRoutes");
			check_bus_or_train = (String) bundle.getString("check_bus_or_train");
		}

		textV_Title = (TextView) findViewById(R.id.textV_header);
		btn_home = (Button) findViewById(R.id.btn_bus_chat_home);
		listView = (ListView) findViewById(R.id.list_view_bus_chat);
		listView.setDividerHeight(0);

		myApp = (MyApplication) getApplication();


		// Set Title of the page 
		textV_Title.setText(title);
		
		btn_home.setVisibility(View.INVISIBLE);

		btn_home.setOnClickListener(this);
		listView.setOnItemClickListener(this);	
	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
		setList();
		if(makeList != null)
		{
			FavoriteRoutesActivity.fav_activity = true;
			adapter = new ListAdapterForNearByRoute(NearbyRouteListActivity.this, makeList);
			//ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listOFTrainOrBus);

			listView.setAdapter(adapter);
		}
	}
	private void setList() {
		if(check_bus_or_train.equalsIgnoreCase("rail"))
		{
			listOfRoute = myApp.getAllOfTrains();


			if(listOfRoute != null)
			{
				makeList = new  ArrayList<ArrayList<String>>();
				int chekcJ = 0;
				outerLoop :	for(int i=0 ; i < listOFTrainOrBus.size(); i++)
				{
					String item = listOFTrainOrBus.get(i);
					for(int j=0 ; j < listOfRoute.size() ; j++)
					{
						if(listOfRoute.get(j).get(1).equalsIgnoreCase(item))
						{
							makeList.add(listOfRoute.get(j));
							j = 0;
							continue outerLoop;
						}
					}
				}

				/*	loop:	for(int no=0 ; no < listOfRoute.size() ; no++)
				{		
					if(listOfRoute.get(no).get(1).equalsIgnoreCase(listOFTrainOrBus.get(chekcJ)))
					{
						makeList.add(listOfRoute.get(no));
						if(chekcJ < listOFTrainOrBus.size() -1)
							chekcJ++;
						else 
							break loop;
						no=0;
					}
				}*/
			}

		}
		else if(check_bus_or_train.equalsIgnoreCase("bus"))
		{
			bus = "bus";
			listOfRoute = myApp.getAllOfBus();

			if(listOfRoute != null)
			{
				makeList = new  ArrayList<ArrayList<String>>();
				int chekcJ = 0;
				outerLoop :	for(int i=0 ; i < listOFTrainOrBus.size(); i++)
				{
					String item = listOFTrainOrBus.get(i);
					for(int j=0 ; j < listOfRoute.size() ; j++)
					{
						if(listOfRoute.get(j).get(1).equalsIgnoreCase(item))
						{
							makeList.add(listOfRoute.get(j));
							j = 0;
							continue outerLoop;
						}
					}
				}
				/*loop:	for(int no=0 ; no < listOfRoute.size() ; no++)
				{		
					if(listOfRoute.get(no).get(1).equalsIgnoreCase(listOFTrainOrBus.get(chekcJ)))
					{
						makeList.add(listOfRoute.get(no));
						if(chekcJ < listOFTrainOrBus.size() -1)
							chekcJ++;
						else
							break loop;
						no=0;
					}
				}*/
			}
		}

	}

	@Override
	public void onClick(View v) 
	{	

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{	
		Intent goToArrivalRoute = new Intent(NearbyRouteListActivity.this , ArrivalTimeListActivity.class);
		goToArrivalRoute.putExtra("stop_name", title.trim());
		goToArrivalRoute.putExtra("title", makeList.get(arg2).get(2));
		goToArrivalRoute.putExtra("subTitle", makeList.get(arg2).get(8));
		if(check_bus_or_train.equalsIgnoreCase("rail"))
		{
			goToArrivalRoute.putExtra("trainImgPos", makeList.get(arg2).get(1));
		}
		else
		{
			goToArrivalRoute.putExtra("busNo", makeList.get(arg2).get(1));
		}
		
	/*	TabGroupActivity parent = (TabGroupActivity) getParent();
		parent.startChildActivity("ArrivalTimeListActivity", goToArrivalRoute);*/
		startActivity(goToArrivalRoute);
	}


}
