package com.tabPack;


import java.io.IOException;


import com.application.MyApplication;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.tabPack.tabGroup1.TabGroup1;
import com.tabPack.tabGroup2.TabGroup2;
import com.tabPack.tabGroup3.TabGroup3;



import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class Tabactivity extends TabActivity 
{
	TabHost tabHost;
	public static int CurrentTab = 0;
	public static int articalCount=0;
	Intent intentFirst;
	TabHost.TabSpec spec;
	Resources res;
	int whichtab = -1;
	int id=-1, qid;
	Bundle b;
	private Intent intentSecond;
	private Intent intentThird;
	private Editor shareEditor;
	private MyApplication myApp;
	private SharedPreferences sharedPreferences;
	private String userName;
	private String thirdTabSet;
	private TabSpec specThird;
	private ParsingClass parsing;


	/** Called when tabHoste activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);		
		setAllTabs();
		parsing = new ParsingClass();
	}	

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{	
		super.onConfigurationChanged(newConfig);
		System.out.println("Configuration changed !!");
	}	

	public void setAllTabs()
	{

		myApp = (MyApplication) getApplicationContext();

		res = getResources();
		tabHost = getTabHost();

		tabHost.setOnTabChangedListener(new OnTabChangeListener() 
		{			
			@Override
			public void onTabChanged(String tabId) 
			{				
				System.out.println("Tab Id Is "+tabId);
				/*	if(tabId.equalsIgnoreCase(getResources().getString(R.string.articaltab))==false )
				{

					articalCount = myapp.arrobj.size();

				}*/
			}
		});

		tabHost.setup();

		if (tabHost == null) 
		{
			// notabHosting to do
		}		

		// For First Tab
		intentFirst = new Intent().setClass(Tabactivity.this, TabGroup1.class);
		System.out.println("CTA Chat intent SET");
		spec = tabHost.newTabSpec(getResources().getString(R.string.tab1Chat))
				.setIndicator(getResources().getString(R.string.tab1Chat),
						getResources().getDrawable(R.drawable.chat_icon)).setContent(intentFirst);

		if (spec == null) 
		{
			System.out.println("Spec is null");
		}
		tabHost.addTab(spec);

		// For Second Tab
		intentSecond = new Intent().setClass(Tabactivity.this, TabGroup2.class);
		System.out.println("All Routes intent SET");

		spec = tabHost.newTabSpec(getResources().getString(R.string.tab2tracker))
				.setIndicator(getResources().getString(R.string.tab2tracker),
						getResources().getDrawable(R.drawable.tracker_tab_icon)).setContent(intentSecond);

		tabHost.addTab(spec);

		// Shared Preferences And Editor
		sharedPreferences = getSharedPreferences("DeviceInfo", 1);
		userName = sharedPreferences.getString("userName", "");
		thirdTabSet = sharedPreferences.getString("ThirdTabSet", "");
		if(thirdTabSet.equalsIgnoreCase(""))
		{
			shareEditor = sharedPreferences.edit();
			shareEditor.putString("ThirdTabSet", "false");
			shareEditor.commit();
		}
		
		if(!userName.equals("TempUser1") && !userName.equalsIgnoreCase(""))
		{
			shareEditor = sharedPreferences.edit();
			shareEditor.putString("ThirdTabSet", "true");
			shareEditor.commit();
		}
		
		//setThirdTab();

	}
	@Override
	protected void onResume() 
	{	
		super.onResume();
		System.out.println("onResume() of TabActvity");

		setThirdTab();	
	}

	public void setThirdTab()
	{		
		// Get Username
		userName = sharedPreferences.getString("userName", "");
		thirdTabSet = sharedPreferences.getString("ThirdTabSet", "");
		System.out.println("userName "+userName);
		System.out.println("Third Tab set or not"+myApp.getThirdTabSet());

		if(!userName.equals("TempUser1") && !userName.equalsIgnoreCase(""))
		{
			if(!thirdTabSet.equalsIgnoreCase("false"))
			{	
				shareEditor = sharedPreferences.edit();
				shareEditor.putString("ThirdTabSet", "false");
				shareEditor.commit();

				myApp.setThirdTabSet(true);

				// For Third Tab
				intentThird = new Intent().setClass(Tabactivity.this, TabGroup3.class);
				System.out.println("Screen Name intent SET");
				specThird = tabHost.newTabSpec(getResources().getString(R.string.tab3ScreenName))
						.setIndicator(getResources().getString(R.string.tab3ScreenName),	
								getResources().getDrawable(R.drawable.my_settings_icon)).setContent(intentThird);
				tabHost.addTab(specThird);
				//tabHost.getTag(intentThird);
			}
		}

	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		System.out.println("onPause() of TabActvity");
	}
	public void onBackPressed() 
	{	
		super.onBackPressed();
		Tabactivity.this.finish();
	}
	@Override
	protected void onDestroy() 
	{
		System.out.println("Tab activity OnDestroy()");
		if(myApp != null)
		{
			myApp.setAllOfBus(null);
			myApp.setAllOfTrains(null);
			myApp.setDeviceMap(null);
			parsing.logout();
		}
		super.onDestroy();
	}
}
