package com.tabPack.tabGroup2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.application.MyApplication;
import com.dataBase.AllQueriesFavoriteRouteClass;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.TabGroupActivity;

import com.tabPack.tabGroup1.listAdapters.ListAdapterForAllRoutes;
import com.tabPack.tabGroup2.listAdapter.ListAdapterForClosestRoute;
import com.tabPack.tabGroup2.listAdapter.ListAdapterForFavorite;
import com.tabPack.tabGroup2.listAdapter.ListAdapterRouteStops;


public class FavoriteRoutesActivity extends GenericClass
{
	private Button btn_nearBy;
	private Button btn_all_route;
	private LinearLayout rel_no_fav;
	private ListView listView;
	private MyApplication myApp;
	private ArrayList<HashMap<String, String>> listOfFav;
	private ListAdapterForFavorite adapter;
	private Cursor cursor;
	private HashMap<String, String> mapFav;
	private Button btn_favoriteRoute;
	private ParsingClass parsing;
	private ArrayList<ArrayList<String>> listofTrain;
	private ArrayList<ArrayList<String>> listOfbuses;
	private ArrayList<ArrayList<String>> listOfAllRoutes;
	private ListAdapterForAllRoutes adapterAllRoutes;
	private Handler handler;
	protected int whichmsg;
	private static char flagActivity= 'F';
	private int position;	
	private EditText edit_searchRoute;
	private ArrayList<ArrayList<String>> searchedlistOfAllRoutes;
	protected ListAdapterForClosestRoute adapterForNearBy;
	protected Button btn;
	protected boolean flagBtn= true;
	private HashMap<String, ArrayList<HashMap<String, Object>>> mapOfRoute;
	private RestClient resOfClosestRoute;	
	public static boolean  fav_activity = true;
	private ArrayList<HashMap<String, Object>> listOfClosestRoute;
	private ThreadPreLoaderForNearBy loaderForNrearBy;
	private Handler handlerForNearBy;
	public static  ProgressDialog mDialogForNearBy;
	public WindowManager windowManager;
	private Display display;
	protected RestClient res;
	private TextView textV_notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.favorites_routes);
		initializeControls();		
	}	

	@Override
	public void initializeControls() 
	{
		// Get Window service for Change Orientation
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();


		// Button All route And nearBy 
		btn_nearBy = (Button) findViewById(R.id.btn_bus_nearby);
		btn_all_route = (Button) findViewById(R.id.btn_all_routes);
		btn_favoriteRoute = (Button) findViewById(R.id.btn_favorites);

		// Relative Layout for notice .
		rel_no_fav = (LinearLayout) findViewById(R.id.rel_fav_routes);

		// Text View for Notice for nearby And Favorites
		textV_notice = (TextView) findViewById(R.id.textV_notic_msg);

		// List View
		listView = (ListView) findViewById(R.id.list_view);
		listView.setSmoothScrollbarEnabled(true);
		listView.setFastScrollEnabled(true);

		//listOfFav = MyApplication.getListOfFavoriteStops();
		listOfFav = new ArrayList<HashMap<String,String>>();


		// Search EditText		 
		edit_searchRoute = (EditText) findViewById(R.id.editT_search_route);

		// Parsing Class
		parsing = new ParsingClass();

		// Applications 
		myApp = (MyApplication) getApplication();
		listofTrain = myApp.getAllOfTrains();
		listOfbuses = myApp.getAllOfBus();
		listOfAllRoutes = new ArrayList<ArrayList<String>>();
		try 
		{
			for(int train = 0; train <listofTrain.size(); train++)
			{
				listOfAllRoutes.add(listofTrain.get(train));			
			}
			for(int bus=0 ; bus < listOfbuses.size(); bus ++)
			{
				listOfAllRoutes.add(listOfbuses.get(bus));	
			}
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		btn_nearBy.setOnClickListener(this);
		btn_all_route.setOnClickListener(this);
		btn_favoriteRoute.setOnClickListener(this);
		listView.setOnItemClickListener(this);	



	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
		switch (flagActivity) 
		{
		case 'F':
			rel_no_fav.setVisibility(View.GONE);
			listView.setAdapter(null);
			listOfFav.clear();
			cursor = AllQueriesFavoriteRouteClass.selectFavoriteRoutes(this);
			System.out.println("cursor count "+cursor.getCount());
			if(cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do 
				{
					mapFav = new HashMap<String, String>();
					mapFav.put("stop_name" , cursor.getString(cursor.getColumnIndex("stop_name")));
					mapFav.put("color_or_no" , cursor.getString(cursor.getColumnIndex("color_or_no")));
					mapFav.put("boundType" , cursor.getString(cursor.getColumnIndex("boundType")));
					mapFav.put("service" , cursor.getString(cursor.getColumnIndex("service")));
					listOfFav.add(mapFav);
				}
				while(cursor.moveToNext());
			}
			AllQueriesFavoriteRouteClass.closeCursor(cursor);

			if(listOfFav != null && listOfFav.size() > 0)
			{
				adapter = new ListAdapterForFavorite(getParent() , listOfFav);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);			
			}
			else
			{
				rel_no_fav.setVisibility(View.VISIBLE);		

				try 
				{					
					textV_notice.setText("You have not added any favorites stops yet");
					/*if(rel_no_fav.getChildAt(1) != null)
					{
						rel_no_fav.removeViewAt(1);
					}*/
				} 
				catch (NullPointerException e) 
				{					
					e.printStackTrace();
				}

			}
			break;

		case 'A':
			listView.setAdapter(null);
			rel_no_fav.setVisibility(View.GONE);
			if(listOfAllRoutes != null)
			{
				adapterAllRoutes = new ListAdapterForAllRoutes(getParent(), listOfAllRoutes);
				listView.setAdapter(adapterAllRoutes);
			}
			break;

		case 'N' :
			listView.setAdapter(null);
			rel_no_fav.setVisibility(View.GONE);
			startThreaderPreLoaderForNearBy();
			break;
		}
	}
	@Override
	public void onClick(View v) 
	{	
		if(v == btn_all_route)
		{
			allRoutes();
		}
		else if( v == btn_nearBy)
		{
			nearbyRoutes();
		}
		else if( v == btn_favoriteRoute)
		{
			favoriteRoutes();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		switch (flagActivity) 
		{
		case 'F':
			Intent goToArrivalRoute = new Intent(FavoriteRoutesActivity.this , ArrivalTimeListActivity.class);
			goToArrivalRoute.putExtra("stop_name", listOfFav.get(arg2).get("stop_name"));
			goToArrivalRoute.putExtra("title", listOfFav.get(arg2).get("stop_name"));
			goToArrivalRoute.putExtra("subTitle", listOfFav.get(arg2).get("boundType"));
			goToArrivalRoute.putExtra("service", listOfFav.get(arg2).get("service"));		
			goToArrivalRoute.putExtra("trainImgPos", listOfFav.get(arg2).get("color_or_no"));
			goToArrivalRoute.putExtra("busNo", listOfFav.get(arg2).get("color_or_no"));		
			startActivity(goToArrivalRoute);		
			break;

		case 'A' :			
			System.out.println("Available routes of Train "+listOfAllRoutes.get(arg2).get(1));
			position = arg2;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			startThreaderPreLoader(listOfAllRoutes);			
			break;

		case 'N' :
			Intent goToNearbySubPage = new Intent(FavoriteRoutesActivity.this , NearbyRouteListActivity.class);		
			goToNearbySubPage.putExtra("titleRoute", (String)listOfClosestRoute.get(arg2).get("n"));
			goToNearbySubPage.putExtra("listRoutes", (ArrayList<String>)listOfClosestRoute.get(arg2).get("r"));
			goToNearbySubPage.putExtra("check_bus_or_train", (String)listOfClosestRoute.get(arg2).get("t"));
			//TabGroupActivity parentNearBy = (TabGroupActivity) getParent();
			//parentNearBy.startChildActivity("NearbyRouteListActivity", goToNearbySubPage);
			startActivity(goToNearbySubPage);
			break;
		}

	}
	@Override
	public void onBackPressed() 
	{		
		super.onBackPressed();
	}

	public void favoriteRoutes()
	{
		flagActivity = 'F';
		edit_searchRoute.setVisibility(View.GONE);
		onResume();
	}
	public void allRoutes()
	{
		flagActivity = 'A';
		search();
		edit_searchRoute.setVisibility(View.VISIBLE);
		onResume();
	}

	public void nearbyRoutes()
	{
		flagActivity = 'N';
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		edit_searchRoute.setVisibility(View.GONE);
		onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}


	// This Section For AllRoutes ==============================================================
	private void startThreaderPreLoader(final ArrayList<ArrayList<String>> listOfAllRoutes)
	{
		try 
		{			
			loaderForNrearBy = new ThreadPreLoaderForNearBy();
			loaderForNrearBy.execute();
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

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
					runOnUiThread(new Runnable() 
					{
						@Override
						public void run() 
						{
							Intent routeStop = new Intent(FavoriteRoutesActivity.this, RouteStopsActivity.class);
							routeStop.putExtra("routeType", listOfAllRoutes.get(position).get(1));
							routeStop.putExtra("positionClicked", position);
							routeStop.putExtra("title", listOfAllRoutes.get(position).get(2));
							routeStop.putExtra("subTitle", listOfAllRoutes.get(position).get(8));
							if(listOfAllRoutes.get(position).get(0).equalsIgnoreCase("rail"))
							{
								routeStop.putExtra("trainImgPos", listOfAllRoutes.get(position).get(1));									
							}
							else
							{
								//R.drawable.bus_stop_icon_bkg);
								routeStop.putExtra("busNo", listOfAllRoutes.get(position).get(1));
							}
							edit_searchRoute.setText("");
							startActivity(routeStop);							
						}
					});
				}
			}
		};
	}

	public boolean doTask() 
	{
		if(searchedlistOfAllRoutes != null && searchedlistOfAllRoutes.size() > 0)
		{
			res = (RestClient) parsing.routeStops(searchedlistOfAllRoutes.get(position).get(1));
		}
		else
		{
			res = (RestClient) parsing.routeStops(listOfAllRoutes.get(position).get(1));
		}
		if(res != null && res.getResponseCode() == 200)
		{
			parseJSON_routes(res.getResponse().toString());		
			whichmsg = 2;			
		} 
		else 
		{
			whichmsg = -1;
		}				
		/*			}
		});
		 */		
		return true;
	}

	public void search()
	{
		searchedlistOfAllRoutes = new ArrayList<ArrayList<String>>();
		edit_searchRoute.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				searchedlistOfAllRoutes.clear();

				String searchKey = s.toString();
				for (int i = 0; i < listOfAllRoutes.size(); i++) 
				{
					if (    listOfAllRoutes.get(i).get(2).toLowerCase().contains(s.toString().toLowerCase()) | 
							listOfAllRoutes.get(i).get(8).toLowerCase().contains(s.toString().toLowerCase()) |
							listOfAllRoutes.get(i).get(1).toLowerCase().contains(s.toString().toLowerCase()))
					{
						searchedlistOfAllRoutes.add(listOfAllRoutes.get(i));
					}
				}					

				ListAdapterForAllRoutes adapter = new ListAdapterForAllRoutes(getParent(), searchedlistOfAllRoutes);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() 
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) 
					{						
						System.out.println("Available routes of Train "+searchedlistOfAllRoutes.get(arg2).get(1));
						position = arg2;
						startThreaderPreLoader(searchedlistOfAllRoutes);
						//edit_searchRoute.setText("");
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
				try {
					InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
					            					getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputManager.hideSoftInputFromWindow(FavoriteRoutesActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});

	}
	private void parseJSON_routes(String resString) 
	{
		ArrayList<HashMap<String, String>> listOfRoute = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> mapOfRoute;
		try 
		{
			JSONArray jArr;
			try 
			{
				jArr = new JSONArray(resString);
				for(int user=0; user<jArr.length();user++)
				{
					JSONObject jsonObject = jArr.getJSONObject(user);
					Iterator keys = jsonObject.keys();
					mapOfRoute = new HashMap<String, String>();
					while (keys.hasNext()) 
					{
						String key = (String) keys.next();
						System.out.println("Key "+key);
						mapOfRoute.put(key, jsonObject.getString(key));

					}
					listOfRoute.add(mapOfRoute);
				}
			} 
			catch (Exception e) 
			{
				JSONObject jObj = new JSONObject(resString);
				Iterator keys = jObj.keys();
				mapOfRoute = new HashMap<String, String>();
				while (keys.hasNext()) 
				{
					String key = (String) keys.next();
					System.out.println("Key "+key);
					mapOfRoute.put(key , jObj.getString(key));

				}
				listOfRoute.add(mapOfRoute);

				e.printStackTrace();
			}

			System.out.println(listOfRoute);// this map will contain your json stuff
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		myApp.setListOfRouteStops(listOfRoute);
	}

	private void ShowAlert(String title) 
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(getParent());
		alertbox.setMessage(title);
		// Add a neutral button to the alert box and assign a click listener
		alertbox.setNeutralButton("Ok", null);
		// show the alert box
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// This Section For NearBy ==============================================================
	private void startThreaderPreLoaderForNearBy()
	{
		try 
		{			
			loaderForNrearBy = new ThreadPreLoaderForNearBy();
			loaderForNrearBy.execute();
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		handlerForNearBy = new Handler() 
		{		

			public void handleMessage(Message msg) 
			{				
				if (whichmsg == -1) 
				{
					//ShowAlert("Please check your net");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				} 
				else if (whichmsg == 2) 
				{
					if(mDialogForNearBy != null)
					{
						mDialogForNearBy.dismiss();
					}
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
					if(listOfClosestRoute != null)
					{			
						adapterForNearBy = new ListAdapterForClosestRoute(getParent() , listOfClosestRoute);
						adapterForNearBy.notifyDataSetChanged();
						listView.setAdapter(adapterForNearBy);

					}
					else
					{
						rel_no_fav.setVisibility(View.VISIBLE);
						textV_notice.setText("No nearby route");
						/*		if(rel_no_fav.getChildAt(1) == null)
						{
							btn = new Button(getParent());
							btn.setText("Try Again");
							LinearLayout.LayoutParams layoutParam =new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
							layoutParam.topMargin = 20;						
							btn.setGravity(Gravity.CENTER);						
							btn.setLayoutParams(layoutParam);
							rel_no_fav.addView(btn);
						}
						btn.setOnClickListener(new OnClickListener() 
						{							
							@Override
							public void onClick(View v) 
							{							
								nearbyRoutes();
							}
						});*/

					}
				}



			}
		};
	}

	private class ThreadPreLoaderForNearBy extends AsyncTask<Object, String, Void> 
	{
		public ThreadPreLoaderForNearBy() 
		{
			super();
		}

		protected void onPreExecute() 
		{
			try 
			{
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						mDialogForNearBy = new ProgressDialog(getParent());	
						if(flagActivity == 'N')
						{
							mDialogForNearBy.setMessage("Getting Closest Stops...");
						}
						else if(flagActivity == 'A')
						{
							mDialogForNearBy.setMessage("Getting All Routes...");
						}

						mDialogForNearBy.show();
					}
				});

			} 
			catch (Exception e) 
			{			
				e.printStackTrace();
			}
		}

		protected Void doInBackground(Object... args) 
		{		

			if(flagActivity == 'N')
			{
				if (doTaskForNearBy()) 
				{
					this.onPostExecute();
				}
			}
			else if(flagActivity == 'A')
			{	
				if (doTask()) 
				{
					this.onPostExecute();
				}

			}
			return null;
		}
		protected void onPostExecute() 
		{
			try 
			{
				if(mDialogForNearBy != null)
				{
					mDialogForNearBy.dismiss();
					mDialogForNearBy = null;
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			Message mesg = Message.obtain();
			mesg.what = 1;
			try 
			{
				if(flagActivity == 'N')
				{
					handlerForNearBy.sendMessage(mesg);
				}
				else if(flagActivity == 'A') 
				{
					handler.sendMessage(mesg);
				}
			} 
			catch (Exception e) 
			{			
				e.printStackTrace();
			}

		}
	}

	public boolean doTaskForNearBy() 
	{	
		try {
			//resOfClosestRoute = (RestClient) parsing.closestStops("41.8781136", "-87.6297982");
			if(MyApplication.CURRENT_LAT != 0.0)
			{
				resOfClosestRoute = (RestClient) parsing.closestStops(MyApplication.CURRENT_LAT+"" , MyApplication.CURRENT_LON+"");

				if(resOfClosestRoute != null && resOfClosestRoute.getResponseCode() == 200)
				{
					parseJSON_routesForNearBy(resOfClosestRoute.getResponse().toString());
					listOfClosestRoute = mapOfRoute.get("s");		
					whichmsg = 2;			
				} 
			}
			else 
			{
				whichmsg = -1;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	private void parseJSON_routesForNearBy(String resString) 
	{			
		ArrayList<HashMap<String, Object>> listInner;
		HashMap<String, Object> mapOfRouteInner;
		try 
		{
			JSONObject jObj = new JSONObject(resString);
			Iterator keys = jObj.keys();
			mapOfRoute = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			while (keys.hasNext()) 
			{
				String key = (String) keys.next();
				System.out.println("Key "+key);
				//mapOfRoute.put(key , jObj.getString(key));
				JSONArray innerSecond = jObj.getJSONArray(key);
				listInner = new ArrayList<HashMap<String, Object>>();
				for(int i=0 ; i < innerSecond.length() ; i++)
				{
					JSONObject jsonObj = innerSecond.getJSONObject(i);
					Iterator keysInner = jsonObj.keys();
					mapOfRouteInner = new HashMap<String, Object>();
					while (keysInner.hasNext()) 
					{
						String strKey = (String) keysInner.next();
						System.out.println("Key "+strKey);	


						if(strKey.equalsIgnoreCase("r"))
						{
							JSONArray jSubArr = jsonObj.getJSONArray(strKey);
							ArrayList<String> listNo_N_color = new ArrayList<String>();
							for(int j=0 ; j < jSubArr.length(); j++)
							{
								listNo_N_color.add(jSubArr.getString(j));
							}
							mapOfRouteInner.put(strKey , listNo_N_color);
						}
						else
						{
							mapOfRouteInner.put(strKey, jsonObj.getString(strKey));
						}
					}
					listInner.add(mapOfRouteInner);
				}	
				mapOfRoute.put(key , listInner);
			}		

			System.out.println(mapOfRoute);// this map will contain your json stuff
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		//myApp.setlistOfArrivalTimeStops(listOfArrivalTime);
	}
	@Override
	protected void onPause() 
	{	
		super.onPause();
		if(mDialogForNearBy != null)
		{
			mDialogForNearBy.dismiss();
		}
	}
	@Override
	protected void onStart() 
	{		
		super.onStart();
	}

}
