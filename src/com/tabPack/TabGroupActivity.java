package com.tabPack;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import android.view.Window;
import android.widget.TabHost.OnTabChangeListener;


public class TabGroupActivity  extends ActivityGroup {

	private ArrayList<String> mIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);      
		if (mIdList == null) mIdList = new ArrayList<String>();

	}

	/**
	 * This is called when a child activity of this one calls its finish method. 
	 * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
	 * and starts the previous activity.
	 * If the last child activity just called finish(),this activity (the parent),
	 * calls finish to finish the entire group.
	 */
	@Override
	public void finishFromChild(Activity child) 
	{
		LocalActivityManager manager = getLocalActivityManager();
		int index = mIdList.size()-1;
		System.out.println("index is "+index);
		if (index < 1) 
		{
			finish();
			return;
		}
		manager.destroyActivity(mIdList.get(index), true);
		mIdList.remove(index); 
		index--;
		String lastId = mIdList.get(index);
		if(lastId!=null)
		{
			Activity act = manager.getActivity(lastId);	      
			if(act!=null)
			{
				Intent lastIntent = act.getIntent();
				System.out.println("Hello activity is Not Null");
				if(lastId.equals("NearByListActivity") || (lastId.equals("SearchActivity")))
				{
					//setContentView(act.getWindow().getDecorView());

					Window newWindow = manager.startActivity(lastId, lastIntent);
					setContentView(newWindow.getDecorView());

				}
				else
				{
					setContentView(act.getWindow().getDecorView());

				}
				//  Window newWindow = manager.startActivity(lastId, lastIntent);

			}
			else
			{
				// lastId = mIdList.get(index-1);
				//  System.out.println("Hello Null New Intent second laet id "+lastId);
				//	  act = manager.getActivity(lastId);
				//  Intent lastIntent = act.getIntent();
				// Window newWindow = manager.startActivity(lastId, lastIntent);
				// setContentView(newWindow.getDecorView());
			}	      
		}
	}

	/**
	 * Starts an Activity as a child Activity to this.
	 * @param Id Unique identifier of the activity to be started.
	 * @param intent The Intent describing the activity to be started.
	 * @throws android.content.ActivityNotFoundException.
	 */
	public void startChildActivity(String Id, Intent intent)
	{

		Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null) 
		{          
			mIdList.add(Id);
			setContentView(window.getDecorView()); 
		}    
	}

	/**
	 * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
	 * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK 
	 * so that all systems call onBackPressed().
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * If a Child Activity handles KeyEvent.KEYCODE_BACK.
	 * Simply override and add this method.
	 */
	@Override
	public void  onBackPressed() 
	{
		System.out.println("onBackPressed TabGroupActivity");
		int length = mIdList.size();
		System.out.println("length of activity is "+length);
		if ( length > 1) 
		{
			Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
			if(current != null)
			current.finish();
		}
		else
		{
			this.finish();
		}
	} 
	@Override
	protected void onResume() 
	{	
		super.onResume();
		System.out.println("onResume() of TabGroupActvity");
		int length = mIdList.size();
		System.out.println("length of activity is "+length);
		if ( length > 1) 
		{
			Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
			try {
				if(current != null)
				current.finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		System.out.println("onPause() of TabGroupActvity");
		
	}
	
	@Override
	protected void onDestroy() 
	{
		System.out.println("OnDestroy TAbGroup");
		
		super.onDestroy();
	}
}

