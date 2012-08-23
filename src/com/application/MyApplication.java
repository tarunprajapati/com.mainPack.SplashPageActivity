package com.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beanClasses.ChatRoomObject;
import com.beanClasses.DeviceInfoBeanClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.service.BroadcastService;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyApplication extends Application
{
	public static DefaultHttpClient client;	
	private  ArrayList<ArrayList<String>> allOfTrains;
	private  ArrayList<ArrayList<String>> allOfBus;
	private ArrayList<ArrayList<ArrayList<String>>> listTrainChatLine;
	private ArrayList<ArrayList<ArrayList<String>>> listBusChatLine;
	private ArrayList<ChatRoomObject> listOfChatObject;
	//private DeviceInfoBeanClass deviceObj;
	private Map deviceMap = null;
	private Boolean thirdTabSet= false;
	private ArrayList<HashMap<String, String>> listOfRouteStops=null;
	private HashMap<String , ArrayList<Boolean>> mapOf_In_service_or_not;
	private LocationManager locManager;
	private Location location;
	public static double CURRENT_ATI;
	public static double CURRENT_SPEED;
	public static double CURRENT_ACCURACY;
	public static double CURRENT_LAT;
	public static double CURRENT_LON;
	public static HashMap<String, Drawable> listOfChatLineImage;
	public static  HashMap<String, Drawable> listOfImage;
	public static ArrayList<HashMap<String, String>> listOfFavoriteStops = new ArrayList<HashMap<String,String>>();
	public static boolean noUpdate = false;

	public ArrayList<ChatRoomObject> getListOfChatObject() 
	{
		return listOfChatObject;
	}

	public void setListOfChatObject(ArrayList<ChatRoomObject> listOfChatObject) {
		this.listOfChatObject = listOfChatObject;
	}

	public ArrayList<ArrayList<ArrayList<String>>> getListBusChatLine() {
		return listBusChatLine;
	}

	public void setListBusChatLine(
			ArrayList<ArrayList<ArrayList<String>>> listBusChatLine) {
		this.listBusChatLine = listBusChatLine;
	}

	public ArrayList<ArrayList<ArrayList<String>>> getListTrainChatLine() {
		return listTrainChatLine;
	}

	public void setListTrainChatLine(
			ArrayList<ArrayList<ArrayList<String>>> listTrainChatLine) {
		this.listTrainChatLine = listTrainChatLine;
	}

	public ArrayList<ArrayList<String>> getAllOfTrains() 
	{
		return allOfTrains;
	}

	public void setAllOfTrains(ArrayList<ArrayList<String>> allOfTrains) 
	{
		this.allOfTrains = allOfTrains;
		setAllTrainImages();	
	}

	public void setAllTrainImages()
	{
		if(allOfTrains != null)
		{
			// Set Images for train in HashMap 
			listOfImage = new HashMap<String, Drawable>();
			listOfImage.put(allOfTrains.get(0).get(1) , getResources().getDrawable(R.drawable.blue_list));
			listOfImage.put(allOfTrains.get(1).get(1) ,getResources().getDrawable(R.drawable.brn_list));
			listOfImage.put(allOfTrains.get(2).get(1) ,getResources().getDrawable(R.drawable.g_list));
			listOfImage.put(allOfTrains.get(3).get(1) ,getResources().getDrawable(R.drawable.org_list));
			listOfImage.put(allOfTrains.get(4).get(1) ,getResources().getDrawable(R.drawable.p_list));
			listOfImage.put(allOfTrains.get(5).get(1) ,getResources().getDrawable(R.drawable.pink_list));
			listOfImage.put(allOfTrains.get(6).get(1) ,getResources().getDrawable(R.drawable.red_list));
			listOfImage.put(allOfTrains.get(7).get(1) ,getResources().getDrawable(R.drawable.y_list));


			// All Train Images
			listOfChatLineImage = new HashMap<String, Drawable>();
			listOfChatLineImage.put(allOfTrains.get(0).get(1) , getResources().getDrawable(R.drawable.blue_destination));
			listOfChatLineImage.put(allOfTrains.get(1).get(1) ,getResources().getDrawable(R.drawable.brn_destination));
			listOfChatLineImage.put(allOfTrains.get(2).get(1) ,getResources().getDrawable(R.drawable.g_destination));
			listOfChatLineImage.put(allOfTrains.get(3).get(1) ,getResources().getDrawable(R.drawable.org_destination));
			listOfChatLineImage.put(allOfTrains.get(4).get(1) ,getResources().getDrawable(R.drawable.p_destination));
			listOfChatLineImage.put(allOfTrains.get(5).get(1) ,getResources().getDrawable(R.drawable.pink_destination));
			listOfChatLineImage.put(allOfTrains.get(6).get(1) ,getResources().getDrawable(R.drawable.red_destination));
			listOfChatLineImage.put(allOfTrains.get(7).get(1) ,getResources().getDrawable(R.drawable.y_destination));


		}


	}



	public ArrayList<ArrayList<String>> getAllOfBus() 
	{
		return allOfBus;
	}

	public void setAllOfBus(ArrayList<ArrayList<String>> allOfBus) 
	{
		this.allOfBus = allOfBus;
	}


	@Override
	public void onCreate() 
	{	
		super.onCreate();
		System.out.println("My Application");	


		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		ConnManagerParams.setMaxTotalConnections(params, 10);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		client = new DefaultHttpClient(cm, params);
		
		getlocation_LAT_LON();
	}



	/*public DeviceInfoBeanClass getDeviceObj() {
		return deviceObj;
	}

	public void setDeviceObj(DeviceInfoBeanClass deviceObj) {
		this.deviceObj = deviceObj;
	}
*/
	public Map getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map deviceMap) {
		this.deviceMap = deviceMap;
	}

	public Boolean getThirdTabSet() {
		return thirdTabSet;
	}

	public void setThirdTabSet(Boolean thirdTabSet) {
		this.thirdTabSet = thirdTabSet;
	}

	public ArrayList<HashMap<String, String>> getListOfRouteStops() {
		return listOfRouteStops;
	}

	public void setListOfRouteStops(ArrayList<HashMap<String, String>> listOfRouteStops) {
		this.listOfRouteStops = listOfRouteStops;
	}

	public HashMap<String , ArrayList<Boolean>> getMapOf_In_service_or_not() {
		return mapOf_In_service_or_not;
	}

	public void setMapOf_In_service_or_not(HashMap<String , ArrayList<Boolean>> mapOf_In_service_or_not) {
		this.mapOf_In_service_or_not = mapOf_In_service_or_not;
	}

	public static ArrayList<HashMap<String, String>> getListOfFavoriteStops() 
	{
		return listOfFavoriteStops;
	}

	public static void setListOfFavoriteStops(HashMap<String, String> listOfFavoriteStops) 
	{
		MyApplication.listOfFavoriteStops.add(listOfFavoriteStops);
	}

	public Location getlocation_LAT_LON()
	{
		try 
		{
			locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,	0, locationListener);

			location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		return location;
	}

	public static LocationListener locationListener = new LocationListener() 
	{		

		public void onLocationChanged(Location location) 
		{
			System.out.println("IN Location Listener");


			if(location != null)
			{
				CURRENT_LAT = location.getLatitude();
				CURRENT_LON = location.getLongitude();
				CURRENT_ATI = location.getAltitude();
				CURRENT_SPEED = location.getSpeed();
				CURRENT_ACCURACY  = location.getAccuracy();
			}			
			System.out.println("Network latitude is ---------:" + CURRENT_LAT);
		}

		public void onProviderDisabled(String provider) 
		{
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	public void onDestroy()
	{
		if(allOfTrains != null)
		{
			allOfTrains = null;
		}
		if(allOfBus != null)
		{
			allOfBus = null;
		}		
	}

}
