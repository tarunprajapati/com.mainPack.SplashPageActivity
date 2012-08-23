package com.tabPack.tabGroup1;


import com.tabPack.TabGroupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
public class TabGroup1 extends TabGroupActivity
{


	public static Activity parent;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parent =this;		
	}
	@Override
	protected void onResume() 
	{	
		super.onResume();
		startChildActivity("HomePageActivity", new Intent(this, HomePageActivity.class));
	}

	@Override
	public void onBackPressed() 
	{	
		System.out.println("OnBackPressed of the Parent");	
		System.out.println("Print Current Activity :"+getParent().getLocalClassName());		 
		super.onBackPressed();
	}
}