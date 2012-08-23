package com.tabPack.tabGroup2.listAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainPack.R;
import com.tabPack.tabGroup2.RouteStopsActivity;

public class ListAdapterRouteStops extends BaseAdapter
{


	private Context context;
	private View view;
	private LayoutInflater inflater;

	private HashMap<String, Drawable> listOfChatLineImg;

	private HashMap<String, ArrayList<Drawable>> listOfRoutesStopImg;
	private ArrayList<HashMap<String, String>> listOfRoutesStop;
	private String routeType;

	public ListAdapterRouteStops(Context context, ArrayList<HashMap<String, String>> listOfRoutesStop ,  HashMap<String, ArrayList<Drawable>> listOfRoutesStopImg , String routeType) 
	{
		this.context = context;
		this.listOfRoutesStop = listOfRoutesStop;
		this.listOfRoutesStopImg = listOfRoutesStopImg;
		this.routeType = routeType;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfRoutesStop.size();
	}

	public Object getItem(int position) 
	{
		return listOfRoutesStop.get(position);
	}

	public long getItemId(int position) 
	{		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		view = convertView;
		if(view == null)
		{
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.train_chat_line_route_bottom, null);
			//view.getLayoutParams().height = 70;
			viewHolder.imgV = (ImageView) view.findViewById(R.id.img_route_bottom);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_train_chat_line_route_name_bottom);			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}


		viewHolder.textSub.setText(listOfRoutesStop.get(position).get("n"));
		if(listOfRoutesStopImg != null)
		{
			viewHolder.imgV.setVisibility(View.VISIBLE);

			if(position == 0)
			{
				System.out.println("Drawable "+listOfRoutesStopImg.get(routeType).get(0));

				viewHolder.imgV.setBackgroundDrawable(listOfRoutesStopImg.get(routeType).get(0));
			}
			else if((listOfRoutesStop.size()-1) == position)
			{
				viewHolder.imgV.setBackgroundDrawable(listOfRoutesStopImg.get(routeType).get(2));
			}
			else
			{
				viewHolder.imgV.setBackgroundDrawable(listOfRoutesStopImg.get(routeType).get(1));
			}
		}		


		return view;
	}
	public static class ViewHolder
	{			
		TextView textSub;
		ImageView imgV;
	}
}
