package com.tabPack.tabGroup2;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.application.MyApplication;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.tabPack.TabGroupActivity;
import com.tabPack.tabGroup1.listAdapters.ListAdapterForAllRoutes;
import com.tabPack.tabGroup2.listAdapter.ListAdapterRouteStops;

public class RouteStopsActivity extends GenericClass
{

	private Button btn_home;
	private Button imgV_title;
	private TextView textV_title;
	private TextView textV_subTitle;
	private EditText edit_search;
	private ListView listView;
	private MyApplication myApp;
	private ParsingClass parsing;
	private ArrayList<HashMap<String, String>> listOfRouteStops;
	private ArrayList<Drawable> arrListBlue;
	private ArrayList<Drawable> arrListBrown;
	private ArrayList<Drawable> arrListGreen;
	private ArrayList<Drawable> arrListOrange;
	private ArrayList<Drawable> arrListpurple;
	private ArrayList<Drawable> arrListPink;
	private ArrayList<Drawable> arrListRed;
	private ArrayList<Drawable> arrListYellow;
	private Bundle bundle;
	private String roputeType;
	private ListAdapterRouteStops adapter;
	private int positionClicked;
	private String title;
	private String subTitle;
	private String trainImgPos;
	private String busNo;
	private ArrayList<Drawable> arrListBus;
	private ArrayList<HashMap<String, String>> searchedlistOfAllRoutes;
	private InputMethodManager imm;
	private RelativeLayout rootLayout;
	public static HashMap<String, ArrayList<Drawable>> routeImagesMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.route_stops);
		initializeControls();
		search();
	}

	@Override
	public void onClick(View v) 
	{	
		if(v == btn_home)
		{
			onBackPressed();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		Intent goToArrivalRoute = new Intent(RouteStopsActivity.this , ArrivalTimeListActivity.class);
		goToArrivalRoute.putExtra("stop_name", listOfRouteStops.get(arg2).get("n"));
		goToArrivalRoute.putExtra("title", title);
		goToArrivalRoute.putExtra("subTitle", subTitle);
		goToArrivalRoute.putExtra("trainImgPos", trainImgPos);
		goToArrivalRoute.putExtra("busNo", busNo);
		startActivity(goToArrivalRoute);
	}

	@Override
	public void initializeControls() 
	{

		bundle = getIntent().getExtras();
		if(bundle != null)
		{
			roputeType = bundle.getString("routeType");
			positionClicked = bundle.getInt("positionClicked");
			title = bundle.getString("title");
			subTitle = bundle.getString("subTitle");
			trainImgPos = bundle.getString("trainImgPos");
			busNo = bundle.getString("busNo");
		}
		
		// Root Relative Layout
		rootLayout = (RelativeLayout) findViewById(R.id.root_rel);
		
		// Button Go to Home
		btn_home = (Button) findViewById(R.id.btn_home_train_chat_line);
		btn_home.requestFocus();
		btn_home.setFocusable(true);
		// Title Image 
		imgV_title = (Button) findViewById(R.id.img_train_chat_line_title);

		// TextView Title And subTitle 
		textV_title = (TextView) findViewById(R.id.textV_train_chat_line_item_titele);
		textV_subTitle = (TextView) findViewById(R.id.textV_train_chat_line_sub_title);

		// EditBoX for search route Stop
		edit_search = (EditText) findViewById(R.id.editT_search_route);
		
		// List View 
		listView = (ListView) findViewById(R.id.list_view_train_chat_line);

		// Set Title  , SubTitle and Title Image
		textV_title.setText(title);
		textV_subTitle.setText(subTitle);
		
		if(trainImgPos != null)
		{
			imgV_title.setBackgroundDrawable(MyApplication.listOfImage.get(trainImgPos));
		}
		else
		{
			imgV_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.bus_stop_icon_bkg));
			imgV_title.setText(busNo);
		}
		
		//MyApplication Object
		myApp = (MyApplication) getApplicationContext();
		listOfRouteStops = myApp.getListOfRouteStops();

		// Parsing Class Object
		parsing = new ParsingClass();

		routeImagesMap = new HashMap<String, ArrayList<Drawable>>();

		arrListBlue = new ArrayList<Drawable>();
		arrListBlue.add(getResources().getDrawable(R.drawable.blue_track_top));
		arrListBlue.add(getResources().getDrawable(R.drawable.blue_track_segment));
		arrListBlue.add(getResources().getDrawable(R.drawable.blue_track_bottom));
		routeImagesMap.put("Blue", arrListBlue);

		arrListBrown = new ArrayList<Drawable>();
		arrListBrown.add(getResources().getDrawable(R.drawable.brn_track_top));
		arrListBrown.add(getResources().getDrawable(R.drawable.brn_track_segment));
		arrListBrown.add(getResources().getDrawable(R.drawable.brn_track_bottom));
		routeImagesMap.put("Brn", arrListBrown);

		arrListGreen = new ArrayList<Drawable>();
		arrListGreen.add(getResources().getDrawable(R.drawable.g_track_top));
		arrListGreen.add(getResources().getDrawable(R.drawable.g_track_segment));
		arrListGreen.add(getResources().getDrawable(R.drawable.g_track_bottom));
		routeImagesMap.put("G", arrListGreen);

		arrListOrange = new ArrayList<Drawable>();
		arrListOrange.add(getResources().getDrawable(R.drawable.org_track_top));
		arrListOrange.add(getResources().getDrawable(R.drawable.org_track_segment));
		arrListOrange.add(getResources().getDrawable(R.drawable.org_track_bottom));
		routeImagesMap.put("Org", arrListOrange);

		arrListpurple = new ArrayList<Drawable>();
		arrListpurple.add(getResources().getDrawable(R.drawable.p_track_top));
		arrListpurple.add(getResources().getDrawable(R.drawable.p_track_segment));
		arrListpurple.add(getResources().getDrawable(R.drawable.p_track_bottom));
		routeImagesMap.put("P", arrListpurple);

		arrListPink = new ArrayList<Drawable>();
		arrListPink.add(getResources().getDrawable(R.drawable.pink_track_top));
		arrListPink.add(getResources().getDrawable(R.drawable.pink_track_segment));
		arrListPink.add(getResources().getDrawable(R.drawable.pink_track_bottom));
		routeImagesMap.put("Pink", arrListPink);

		arrListRed = new ArrayList<Drawable>();
		arrListRed.add(getResources().getDrawable(R.drawable.red_track_top));
		arrListRed.add(getResources().getDrawable(R.drawable.red_track_segment));
		arrListRed.add(getResources().getDrawable(R.drawable.red_track_bottom));
		routeImagesMap.put("Red", arrListRed);

		arrListYellow = new ArrayList<Drawable>();
		arrListYellow.add(getResources().getDrawable(R.drawable.y_track_top));
		arrListYellow.add(getResources().getDrawable(R.drawable.y_track_segment));
		arrListYellow.add(getResources().getDrawable(R.drawable.y_track_bottom));
		routeImagesMap.put("Y", arrListYellow);

		arrListBus = new ArrayList<Drawable>();
		arrListBus.add(getResources().getDrawable(R.drawable.bus_track_top));
		arrListBus.add(getResources().getDrawable(R.drawable.bus_track_segment));
		arrListBus.add(getResources().getDrawable(R.drawable.bus_track_bottom));
		routeImagesMap.put("Bus", arrListBus);

		// Input Method service
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// Set on click listener 
		btn_home.setOnClickListener(this);
		edit_search.setOnClickListener(this);
		listView.setOnItemClickListener(this);	
		edit_search.setOnClickListener(this);

	}

	public void search()
	{
		searchedlistOfAllRoutes = new ArrayList<HashMap<String, String>>();
		edit_search.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				searchedlistOfAllRoutes.clear();

				String searchKey = s.toString();
				try {
					for (int i = 0; i < listOfRouteStops.size(); i++) 
					{
						if ( listOfRouteStops.get(i).get("n").toLowerCase().contains(s.toString().toLowerCase()) )
						{
							searchedlistOfAllRoutes.add(listOfRouteStops.get(i));
						}
					}					
					
					if(searchedlistOfAllRoutes != null)
					{
						if(trainImgPos != null)
						{
							adapter = new ListAdapterRouteStops(RouteStopsActivity.this, searchedlistOfAllRoutes, routeImagesMap , roputeType);
							listView.setDividerHeight(0);
							listView.setAdapter(adapter);
						}
						else if(busNo != null)
						{
							adapter = new ListAdapterRouteStops(RouteStopsActivity.this, searchedlistOfAllRoutes, routeImagesMap , "Bus");
							listView.setDividerHeight(0);
							listView.setAdapter(adapter);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				listView.setOnItemClickListener(new OnItemClickListener() 
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) 
					{						
						System.out.println("Available routes of Train "+searchedlistOfAllRoutes.get(arg2).get(1));
						
						Intent goToArrivalRoute = new Intent(RouteStopsActivity.this
								, ArrivalTimeListActivity.class);
						goToArrivalRoute.putExtra("stop_name", searchedlistOfAllRoutes.get(arg2).get("n"));
						goToArrivalRoute.putExtra("title", title);
						goToArrivalRoute.putExtra("subTitle", subTitle);
						goToArrivalRoute.putExtra("trainImgPos", trainImgPos);
						goToArrivalRoute.putExtra("busNo", busNo);
						//TabGroupActivity parent = (TabGroupActivity) getParent();
						//parent.startChildActivity("ArrivalTimeListActivity", goToArrivalRoute);
						startActivity(goToArrivalRoute);
						edit_search.setText("");
					}
				});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
			{
				
			}

			@Override
			public void afterTextChanged(Editable s) 
			{

			}

		});
		
		listView.setOnTouchListener(new OnTouchListener() 
		{			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
				            					getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(RouteStopsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
		if(listOfRouteStops != null)
		{
			if(trainImgPos != null)
			{
				adapter = new ListAdapterRouteStops(RouteStopsActivity.this, listOfRouteStops, routeImagesMap , roputeType);
				listView.setDividerHeight(0);
				listView.setAdapter(adapter);
			}
			else if(busNo != null)
			{
				adapter = new ListAdapterRouteStops(RouteStopsActivity.this, listOfRouteStops, routeImagesMap , "Bus");
				listView.setDividerHeight(0);
				listView.setAdapter(adapter);
			}
		}
	}

}
