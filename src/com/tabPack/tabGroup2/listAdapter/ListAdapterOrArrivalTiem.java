package com.tabPack.tabGroup2.listAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mainPack.R;
import com.tabPack.tabGroup2.ArrivalTimeListActivity;

public class ListAdapterOrArrivalTiem extends BaseAdapter
{	
	private View view;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String, String>> list;
	private Context context;


	public ListAdapterOrArrivalTiem(Context context, ArrayList<HashMap<String, String>> arrayList) 
	{
		this.list =arrayList;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return list.size();
	}

	public Object getItem(int position) 
	{
		return list.get(position);
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
			view = inflater.inflate(R.layout.custom_route_item, null);			
			viewHolder.imgV = (RelativeLayout) view.findViewById(R.id.rel_arrival_route);
			viewHolder.textV_route_id = (TextView) view.findViewById(R.id.arrival_route_id);
			viewHolder.textV_route_name = (TextView) view.findViewById(R.id.arrival_route_name);
			viewHolder.textV_arrival_time = (TextView) view.findViewById(R.id.arrival_time);
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}		
		
		try 
		{
			if(Integer.parseInt(list.get(position).get("s")) > 60)
			{
				viewHolder.textV_arrival_time.setText(Integer.parseInt(list.get(position).get("s")) / 60+" min");
			}
			else
			{
				viewHolder.textV_arrival_time.setText("Due");
			}
			if(ArrivalTimeListActivity.mapOfListImg.containsKey(list.get(position).get("r")))
			{			
				viewHolder.textV_route_id.setText("#"+list.get(position).get("rn")+" to");
				viewHolder.textV_route_name.setText(list.get(position).get("d"));
				viewHolder.imgV.setBackgroundDrawable(ArrivalTimeListActivity.mapOfListImg.get(list.get(position).get("r")));
			}
			else
			{			
				viewHolder.textV_route_id.setText("#"+list.get(position).get("v")+" to");
				viewHolder.textV_route_name.setText(ArrivalTimeListActivity.busNo+" "+list.get(position).get("d"));
				viewHolder.imgV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bus_destination));
			}
		} 
		catch (NumberFormatException e) 
		{		
			e.printStackTrace();
		} 
		catch (NotFoundException e) 
		{
		
			e.printStackTrace();
		}

		return view;
	}
	public static class ViewHolder
	{			
		public TextView textV_arrival_time;
		public TextView textV_route_name;
		TextView textV_route_id;
		RelativeLayout imgV;
	}
}
