package com.mainPack;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

public abstract class GenericClass extends Activity implements OnClickListener , OnItemClickListener
{
	public abstract void initializeControls();	
}
