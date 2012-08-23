package com.tabPack.tabGroup3;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mainPack.GenericClass;
import com.mainPack.R;
import com.parsing.ParsingClass;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;

public class ChangeScreenNameActivity extends GenericClass
{

	private Button btn_change_screen_name;
	private SharedPreferences sharePref;
	private String userName;
	private Editor shareEditor;
	private ParsingClass parsing;
	private TextView textV_screeb_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_name);
		initializeControls();
	}
	
	@Override
	protected void onResume() 
	{	
		super.onResume();
		System.out.println("OnResume Of change Screen Name");
		userName = sharePref.getString("userName" , ""); 
		if(userName != null)
			textV_screeb_name.setText(userName);
	}

	@Override
	public void onClick(View v) 
	{	
		if(v == btn_change_screen_name)
		{
			setSreenNameDialogBox();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeControls() 
	{
		// Change Screen Name
		btn_change_screen_name = (Button) findViewById(R.id.btn_change_screen_name);

		// TExt View With Screen Name
		textV_screeb_name = (TextView) findViewById(R.id.textV_screenName);

		// Parsing Class
		parsing = new ParsingClass();

		// get SharedPreference
		sharePref = getSharedPreferences("DeviceInfo", 1);	
		shareEditor = sharePref.edit();

		// Set on Click Listener
		btn_change_screen_name.setOnClickListener(this);

	}
	private void setSreenNameDialogBox()
	{		
		System.out.println("Print inside Dialog Box :"+userName);
		final Dialog dialogForSreenName = new Dialog(getParent());
		dialogForSreenName.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogForSreenName.setContentView(R.layout.change_screen_name);
		TextView textV_title = (TextView) dialogForSreenName.findViewById(R.id.textV_header_change_screenName);
		TextView textV_newScreen_name = (TextView) dialogForSreenName.findViewById(R.id.textV_logo_newName);		
		Button btn_changeScreenName = (Button) dialogForSreenName.findViewById(R.id.btn_save_screen_name);
		final EditText edit_newScreenName = (EditText) dialogForSreenName.findViewById(R.id.editT_new_screen_name);
		Button btn_cancel = (Button) dialogForSreenName.findViewById(R.id.btn_change_name_cancel);
		
		
		textV_title.setText("Change Screenname");
		textV_newScreen_name.setText("New Screen Name:");
		btn_changeScreenName.setText("Save Screen Name");
		
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
			@Override
			public void onClick(View v) 
			{					
				String res = (String) parsing.changeUsername(edit_newScreenName.getText().toString());
				if(res.equals("true"))
				{
					System.out.println("print the EditText Name :"+edit_newScreenName.getText().toString());

					shareEditor.putString("userName", edit_newScreenName.getText().toString());
					shareEditor.commit();						
					dialogForSreenName.dismiss();
					onResume();
				}
				else
				{
					Toast.makeText(ChangeScreenNameActivity.this, "Please check your net", Toast.LENGTH_SHORT).show();
				}					
			}
		});

	}

}

