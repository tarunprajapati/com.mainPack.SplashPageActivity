package com.tabPack.tabGroup3;



import com.tabPack.TabGroupActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle; 
import android.view.Window;
public class TabGroup3 extends TabGroupActivity
{
	private SharedPreferences storeData;
	private String userName;

	private Builder builder;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);    

	}
	@Override
	protected void onResume() 
	{	
		super.onResume();		
		startChildActivity("Favorites", new Intent(TabGroup3.this, ChangeScreenNameActivity.class));        
	}
}