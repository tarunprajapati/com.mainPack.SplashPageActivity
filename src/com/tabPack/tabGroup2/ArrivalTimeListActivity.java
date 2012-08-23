package com.tabPack.tabGroup2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MyApplication;
import com.dataBase.AllQueriesFavoriteRouteClass;
import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.parsing.RestClient;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;
import com.tabPack.tabGroup1.train.TrainChatRoomActivity;
import com.tabPack.tabGroup2.listAdapter.ListAdapterOrArrivalTiem;


public class ArrivalTimeListActivity extends GenericClass
{
	private Button btn_refresh;
	private ImageButton img_buttonFav;
	private ImageButton img_btn_direction;
	private ListView listView;
	private TextView textV_title;
	private TextView textV_subTitle;
	private TextView textV_routeName;
	private TextView textV_routeDetail;
	private com.tabPack.tabGroup2.ArrivalTimeListActivity.ThreadPreLoader loader;
	private Handler handler;
	protected int whichmsg;
	private ParsingClass parsing;
	private MyApplication myApp;
	private Bundle bundle;
	private String stop_name;
	private String title;
	//private String subTitle;
	private String trainImgPos;
	public static String busNo;
	private Button imgV_title;
	HashMap<String, ArrayList<HashMap<String, String>>> mapOfRoute;
	private TextView textV_routeSubDetail;
	public static HashMap<String, Drawable> mapOfListImg;
	private ListAdapterOrArrivalTiem adapter;
	private Character boundType;
	private ArrayList<HashMap<String, String>> listOfFavoritesRouteStops;
	private boolean flagOnce = true;
	private TextView textV_no_list;
	private HashMap<String, String> map_fav_rout_stop;
	private SharedPreferences sharePref;
	private String userName;
	private Editor shareEditor;
	private RestClient res;
	private String seriveThis;
	private String pressBound;
	private boolean flagTextV_no_list = false;
	private String defalutRouteSide=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.estimated_arrival_time);
		initializeControls();
	}
	@Override
	public void onClick(View v) 
	{
		if(v == img_btn_direction)
		{
			if(boundType != null)
			{
				if(boundType == 'N')
				{
					img_btn_direction.setBackgroundDrawable(getResources().getDrawable(R.drawable.direction_switch_alt_btn));
					setNorthBound();
				}
				else if(boundType == 'S')
				{
					img_btn_direction.setBackgroundDrawable(getResources().getDrawable(R.drawable.direction_switch_default_btn));
					setSouthBound();
				}
				else if(boundType == 'W')
				{
					img_btn_direction.setBackgroundDrawable(getResources().getDrawable(R.drawable.direction_switch_alt_btn));
					setWestBound();
				}
				else if(boundType == 'E')
				{
					img_btn_direction.setBackgroundDrawable(getResources().getDrawable(R.drawable.direction_switch_default_btn));
					setEastBound();
				}
			}
		}
		else if(v == img_buttonFav)
		{
			if(flagOnce)
			{
				map_fav_rout_stop = new HashMap<String, String>();
				map_fav_rout_stop.put("stop_name", stop_name);
				if(trainImgPos != null)
				{
					map_fav_rout_stop.put("color_or_no", trainImgPos);
				}
				else if(busNo != null)
				{
					map_fav_rout_stop.put("color_or_no", busNo);
				}

				if(boundType != null)
				{
					if(boundType == 'N')
					{
						map_fav_rout_stop.put("boundType", "South Bound");
					}
					else if(boundType == 'S')
					{
						map_fav_rout_stop.put("boundType", "North Bound");
					}
					else if(boundType == 'E')
					{
						map_fav_rout_stop.put("boundType", "West Bound");
					}
					else if(boundType == 'W')
					{
						map_fav_rout_stop.put("boundType", "East Bound");
					}
				}
				map_fav_rout_stop.put("service", seriveThis);

				img_buttonFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_star_selected));
				ShowAlert("Set Favorite Successfully.");
				AllQueriesFavoriteRouteClass.insertFavorite(map_fav_rout_stop, this);
				flagOnce = false;
			}
			else
			{
				map_fav_rout_stop = null;
				img_buttonFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_star_deselected));
				ShowAlert("Remove Favorite Successfully.");
				AllQueriesFavoriteRouteClass.deleteFavoriteRoutes(this, stop_name);
				flagOnce = true;
			}
		}
		else if(v == btn_refresh)
		{
			if(textV_routeDetail != null)
			{
				defalutRouteSide = textV_routeDetail.getText().toString();
			}
			onResume();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		setSreenNameDialogBox(arg2);
	}

	@Override
	public void initializeControls() 
	{
		// Bundle Object 
		bundle = getIntent().getExtras();
		if(bundle != null)
		{
			stop_name = bundle.getString("stop_name");
			System.out.println("Stop name : "+stop_name);
			title = bundle.getString("title");
			seriveThis = bundle.getString("subTitle");
			//seriveThis = bundle.getString("service");
			trainImgPos = bundle.getString("trainImgPos");
			busNo = bundle.getString("busNo");
		}

		// get SharedPreference
		sharePref = getSharedPreferences("DeviceInfo", 1);
		//TempUser1
		userName = sharePref.getString("userName" , ""); 
		shareEditor = sharePref.edit();

		// Parsing Class
		parsing = new ParsingClass();

		// Refresh Button , Favorite , Direction 
		btn_refresh = (Button) findViewById(R.id.btn_refesh);
		img_buttonFav = (ImageButton) findViewById(R.id.imageButtonFavorite);
		img_btn_direction = (ImageButton) findViewById(R.id.imageButtonDirectionChange);

		// Text View Title , SubTitle , arrivalTime , arrival Route name , 
		textV_title = (TextView) findViewById(R.id.textV_train_chat_line_item_titele);
		textV_subTitle = (TextView) findViewById(R.id.textV_train_chat_line_sub_title);
		textV_routeName = (TextView) findViewById(R.id.textV_route_name);
		textV_routeDetail = (TextView) findViewById(R.id.textV_route_detail);
		textV_routeSubDetail = (TextView) findViewById(R.id.textV_route_subdetail);
		textV_no_list = (TextView) findViewById(R.id.textV_show_no_list);

		// Title Image for bus and train
		imgV_title = (Button) findViewById(R.id.img_train_chat_line_title);

		// List View 
		listView = (ListView) findViewById(R.id.list_view);

		// List of favorite Route Stop
		listOfFavoritesRouteStops = new ArrayList<HashMap<String, String>>();

		// Set Title  , SubTitle and Title Image
		textV_title.setText(title);
		textV_subTitle.setText(seriveThis);

		Cursor cursor = AllQueriesFavoriteRouteClass.selectParticularRow(this , stop_name);
		System.out.println("Corsor count "+cursor.getCount());
		if(cursor != null && cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			do
			{
				String stop_fav = cursor.getString(cursor.getColumnIndex("stop_name"));
				if(stop_fav != null)
				{
					img_buttonFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_star_selected));
					flagOnce = false;
				}
			}
			while(cursor.moveToNext());
		}			
		AllQueriesFavoriteRouteClass.closeCursor(cursor);

		if(trainImgPos != null && MyApplication.listOfImage.containsKey(trainImgPos))
		{
			imgV_title.setBackgroundDrawable(MyApplication.listOfImage.get(trainImgPos));
		}
		else
		{
			imgV_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.bus_stop_icon_bkg));
			imgV_title.setText(busNo);
		}

		mapOfListImg = new HashMap<String, Drawable>();

		// All Train Images
		mapOfListImg.put("Blue", getResources().getDrawable(R.drawable.blue_destination));
		mapOfListImg.put("Brn", getResources().getDrawable(R.drawable.brn_destination));
		mapOfListImg.put("G", getResources().getDrawable(R.drawable.g_destination));		
		mapOfListImg.put("Org", getResources().getDrawable(R.drawable.org_destination));
		mapOfListImg.put("P", getResources().getDrawable(R.drawable.p_destination));
		mapOfListImg.put("Pink", getResources().getDrawable(R.drawable.pink_destination));		
		mapOfListImg.put("Red", getResources().getDrawable(R.drawable.red_destination));
		mapOfListImg.put("Y", getResources().getDrawable(R.drawable.y_destination));		


		parsing = new ParsingClass();
		myApp = (MyApplication) getApplicationContext();

		// Set on Click Listener
		img_btn_direction.setOnClickListener(this);
		img_buttonFav.setOnClickListener(this);
		btn_refresh.setOnClickListener(this);
		listView.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() 
	{	
		super.onResume();
		startThreaderPreLoader();
	/*	if(mapOfRoute != null)
		{
			setList();
		}*/
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
					setList();
				}
			}
		};
	}

	private void ShowAlert(String title) 
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(ArrivalTimeListActivity.this);
		alertbox.setMessage(title);		
		alertbox.setNeutralButton("Ok", null);		
		alertbox.show();
	}

	private class ThreadPreLoader extends AsyncTask<Object, String, Void> 
	{
		private ProgressDialog mDialog;

		public ThreadPreLoader() 
		{
			super();
		}

		protected void onPreExecute() 
		{
			mDialog = new ProgressDialog(ArrivalTimeListActivity.this);
			//mDialog.setMessage("Entering Chat Room...");
			mDialog.setMessage("Getting Estimated Arrival time...");
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
			try {
				handler.sendMessage(mesg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean doTask() 
	{	
		RestClient res = (RestClient) parsing.arrivalTime(stop_name);
		if(res != null && res.getResponseCode() == 200)
		{
			parseJSON_routes(res.getResponse().toString());		
			whichmsg = 2;			
		} 
		else 
		{
			whichmsg = -1;
		}
		return true;
	}

	private void parseJSON_routes(String resString) 
	{			
		ArrayList<HashMap<String, String>> listInner;
		HashMap<String, String> mapOfRouteInner;
		try 
		{
			JSONObject jObj = new JSONObject(resString);
			Iterator keys = jObj.keys();
			mapOfRoute = new HashMap<String, ArrayList<HashMap<String,String>>>();
			while (keys.hasNext()) 
			{
				String key = (String) keys.next();
				System.out.println("Key "+key);
				//mapOfRoute.put(key , jObj.getString(key));
				JSONArray innerSecond = jObj.getJSONArray(key);
				listInner = new ArrayList<HashMap<String, String>>();
				for(int i=0 ; i < innerSecond.length() ; i++)
				{
					JSONObject jsonObj = innerSecond.getJSONObject(i);
					Iterator keysInner = jsonObj.keys();
					mapOfRouteInner = new HashMap<String, String>();
					while (keysInner.hasNext()) 
					{
						String strKey = (String) keysInner.next();
						System.out.println("Key "+strKey);						
						mapOfRouteInner.put(strKey, jsonObj.getString(strKey));
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

	private void setList()
	{
		// Set Name of Route
		textV_routeName.setText(stop_name);

		// South And Norht Bound
		if(mapOfRoute.containsKey("South Bound") && mapOfRoute.containsKey("North Bound"))
		{
			img_btn_direction.setVisibility(View.VISIBLE);
			if(defalutRouteSide != null)
			{
				if(defalutRouteSide.contains("South Bound"))
				{
					setBounds('S');
				}
				else 
				{
					setBounds('N');
				}			
			}
			else 
			{
				setBounds('S');
			}
		}
		else if(mapOfRoute.containsKey("South Bound") || mapOfRoute.containsKey("North Bound"))
		{
			img_btn_direction.setVisibility(View.INVISIBLE);
			if(mapOfRoute.containsKey("South Bound"))
			{
				setBounds('S');
			}
			else if(mapOfRoute.containsKey("North Bound"))
			{
				setBounds('N');
			}				
		}		
		
		// Set East And West Bound
		else if(mapOfRoute.containsKey("West Bound") && mapOfRoute.containsKey("East Bound"))
		{
			img_btn_direction.setVisibility(View.VISIBLE);
			if(defalutRouteSide != null)
			{
				if(defalutRouteSide.contains("West Bound"))
				{
					setBounds('W');
				}
				else
				{
					setBounds('E');
				}
			}
			else
			{
				setBounds('W');
			}
			
		}
		else if(mapOfRoute.containsKey("West Bound") || mapOfRoute.containsKey("East Bound"))
		{
			img_btn_direction.setVisibility(View.INVISIBLE);
			if(mapOfRoute.containsKey("West Bound"))
			{
				setBounds('W');
			}
			else if(mapOfRoute.containsKey("East Bound"))
			{
				setBounds('E');
			}				
		}		
		else 
		{
			textV_no_list.setVisibility(View.VISIBLE);
		}
	}
	public void setBounds(char boundType)
	{
		switch (boundType) 
		{	
		case 'S':
			setSouthBound();
			break;

		case 'N':
			setNorthBound();
			break;

		case 'E':
			setEastBound();
			break ;

		case 'W':
			setWestBound();		
			break;
		}
	}
	// Set South Bound
	public void setSouthBound()
	{	
		boundType = 'N';
		try 
		{
			textV_routeDetail.setText("South Bound:");
			pressBound = "South Bound";
			if(mapOfRoute.get("South Bound").size() > 0)
			{
				textV_routeSubDetail.setText(mapOfRoute.get(pressBound).get(0).get("d"));
				adapter = new ListAdapterOrArrivalTiem(ArrivalTimeListActivity.this , mapOfRoute.get("South Bound"));
				listView.setAdapter(adapter);
			}
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
	}

	// Set North Bound
	public void setNorthBound()
	{
		boundType = 'S';
		try 
		{
			textV_routeDetail.setText("North Bound:");
			pressBound = "North Bound";
			if(mapOfRoute.get("North Bound").size() > 0)
			{
				textV_routeSubDetail.setText(mapOfRoute.get(pressBound).get(0).get("d"));
				adapter = new ListAdapterOrArrivalTiem(ArrivalTimeListActivity.this , mapOfRoute.get("North Bound"));
				listView.setAdapter(adapter);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	// Set East Bound
	public void setEastBound()
	{
		boundType = 'W';
		try 
		{
			textV_routeDetail.setText("East Bound:");
			pressBound = "East Bound";
			if(mapOfRoute.get("East Bound").size() > 0)
			{
				textV_routeSubDetail.setText(mapOfRoute.get(pressBound).get(0).get("d"));
				adapter = new ListAdapterOrArrivalTiem(ArrivalTimeListActivity.this , mapOfRoute.get("East Bound"));
				listView.setAdapter(adapter);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	// Set West Bound 
	public void setWestBound()
	{
		boundType = 'E';
		try 
		{
			textV_routeDetail.setText("West Bound:");
			pressBound = "West Bound";
			if(mapOfRoute.get("West Bound").size() > 0)
			{
				textV_routeSubDetail.setText(mapOfRoute.get(pressBound).get(0).get("d"));
				adapter = new ListAdapterOrArrivalTiem(ArrivalTimeListActivity.this , mapOfRoute.get("West Bound"));
				listView.setAdapter(adapter);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}


	@Override
	protected void onDestroy() 
	{
		/*if(map_fav_rout_stop != null)
		{
			//MyApplication.listOfFavoriteStops.add(map_fav_rout_stop);
			AllQueriesFavoriteRouteClass.insertFavorite(map_fav_rout_stop, this);
		}*/
		super.onDestroy();
	}

	private void setSreenNameDialogBox(final int arg2)
	{
		userName = sharePref.getString("userName" , "");
		if(userName.equals("TempUser1"))
		{
			System.out.println("Print inside Dialog Box :"+userName);
			final Dialog dialogForSreenName = new Dialog(ArrivalTimeListActivity.this);
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
					resOfChangeName = (String) parsing.changeUsername(edit_newScreenName.getText().toString().trim());
					if(resOfChangeName.equals("true"))
					{
						System.out.println("print the EditText Name :"+edit_newScreenName.getText().toString());

						shareEditor.putString("userName", edit_newScreenName.getText().toString().trim());
						shareEditor.putString("ThirdTabSet", "true");
						shareEditor.commit();						
						
						dialogForSreenName.dismiss();						
						goToRoom(arg2);						
					}
					else
					{
						Toast.makeText(ArrivalTimeListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
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
		String roomName=null;
		// https://api2.transitchatterdev.com/cta-bus-1-Drexel%20Square/messages/last-50
		Intent chatRoomAct = new Intent(ArrivalTimeListActivity.this , TrainChatRoomActivity.class);
		if(MyApplication.listOfImage.containsKey(mapOfRoute.get(pressBound).get(pos).get("r")))
		{
			//cta-rail-Red-Howard
			try {
				String splitStr[] = ((String)mapOfRoute.get(pressBound).get(pos).get("d")).split("/");
				roomName = "cta"+"-"+"rail"+"-"+mapOfRoute.get(pressBound).get(pos).get("r")+"-"+splitStr[0].trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chatRoomAct.putExtra("trainColor", mapOfRoute.get(pressBound).get(pos).get("r"));
		}
		else
		{
			//cta-bus-53-Howard
			try {
				String splitStr[] = ((String)mapOfRoute.get(pressBound).get(pos).get("d")).split("/");
				
				roomName = "cta"+"-"+"bus"+"-"+mapOfRoute.get(pressBound).get(pos).get("r")+"-"+(String)mapOfRoute.get(pressBound).get(pos).get("d");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chatRoomAct.putExtra("Bus", "Bus");
			chatRoomAct.putExtra("BusNo", mapOfRoute.get(pressBound).get(pos).get("r"));
		}
		
		System.out.println("Room Name "+roomName);
		res = (RestClient) parsing.enterRoom(roomName);
		System.out.println("res of enter Room "+res);
		if((res.getResponse().replaceAll("\n", "")).equals("true"))
		{

			chatRoomAct.putExtra("trainName", mapOfRoute.get(pressBound).get(pos).get("d"));
			chatRoomAct.putExtra("trainService", seriveThis);

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
			Toast.makeText(ArrivalTimeListActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
		}

	}

}
