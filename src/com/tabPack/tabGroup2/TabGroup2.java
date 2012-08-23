package com.tabPack.tabGroup2;

import com.tabPack.TabGroupActivity;

import android.content.Intent;
import android.os.Bundle; 
import android.view.Window;
public class TabGroup2 extends TabGroupActivity
{
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
    	 startChildActivity("SearchCity", new Intent(this, FavoriteRoutesActivity.class));
    }
}