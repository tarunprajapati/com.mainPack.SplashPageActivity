package com.tabPack.tabGroup2.listAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.MyApplication;
import com.mainPack.R;

public class ListAdapterForClosestRoute extends BaseAdapter
{
	private ArrayList<HashMap<String, Object>> listOfAllRoutes;

	private Context context;
	private View view;
	private LayoutInflater inflater;
	boolean ignoreDisabled = false;	
	ViewHolder viewHolder;

	private ArrayList<String> listRailColor;

	private ArrayList<String> listBusNo;

	private int no_of_rail_or_bus;
	public ListAdapterForClosestRoute(Context context, ArrayList<HashMap<String, Object>> listOfAllRoutes) 
	{
		this.context = context;
		this.listOfAllRoutes = listOfAllRoutes;		
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfAllRoutes.size();
	}

	public Object getItem(int position) 
	{
		return listOfAllRoutes.get(position);
	}

	public long getItemId(int position) 
	{		
		return position;
	}

	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		view = convertView;
		if(view == null)
		{
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.custom_nearby_item, null);
			viewHolder.text_Name = (TextView) view.findViewById(R.id.textV_nearby_name);
			viewHolder.text_distance = (TextView) view.findViewById(R.id.textV_nearby_distance);
			viewHolder.imgV = (LinearLayout) view.findViewById(R.id.linear_train_n_bus_no);

			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.text_Name.setText((String)listOfAllRoutes.get(position).get("n"));				
		viewHolder.text_distance.setText(listOfAllRoutes.get(position).get("d")+" mi");

		if(((String)listOfAllRoutes.get(position).get("t")).equalsIgnoreCase("rail"))
		{
			no_of_rail_or_bus = ((ArrayList<String>)listOfAllRoutes.get(position).get("r")).size();
			listRailColor =   ((ArrayList<String>)listOfAllRoutes.get(position).get("r"));
			viewHolder.imgV.removeAllViews();
			for(int rail_no= 0 ; rail_no < no_of_rail_or_bus; rail_no++)
			{
				ImageView btn = new ImageView(context);
				btn.setBackgroundDrawable(MyApplication.listOfChatLineImage.get(listRailColor.get(rail_no)));
				LinearLayout.LayoutParams layO = new LinearLayout.LayoutParams(30 , 30); 
				layO.setMargins(5, 2, 5, 2);
				btn.setLayoutParams(layO);
				btn.setMaxHeight(30);
				btn.setMaxWidth(30);				
				viewHolder.imgV.addView(btn);
			}
		}
		else
		{
			no_of_rail_or_bus = ((ArrayList<String>)listOfAllRoutes.get(position).get("r")).size();
			listBusNo =   ((ArrayList<String>)listOfAllRoutes.get(position).get("r"));
			viewHolder.imgV.removeAllViews();
			for(int busNo= 0 ; busNo < no_of_rail_or_bus; busNo++)
			{
				TextView btn = new TextView(context);
				LinearLayout.LayoutParams layO = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT); 
				layO.setMargins(5, 2, 5, 2);
				btn.setLayoutParams(layO);
				btn.setMaxHeight(40);
				btn.setMaxWidth(40);
				if(busNo != no_of_rail_or_bus - 1)
					btn.setText(listBusNo.get(busNo)+",");
				else
					btn.setText(listBusNo.get(busNo));
				viewHolder.imgV.addView(btn);
			}
		}	

		return view;
	}

	public static class ViewHolder
	{
		TextView text_Name;	
		TextView text_distance;
		LinearLayout imgV;
	}

}

