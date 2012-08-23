package com.tabPack.tabGroup2.listAdapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.application.MyApplication;
import com.mainPack.R;
import com.tabPack.tabGroup1.HomePageActivity;

public class ListAdapterForNearByRoute extends BaseAdapter
{
	private ArrayList<ArrayList<String>> listOfAllRoutes;

	private Context context;
	private View view;
	private LayoutInflater inflater;
	boolean ignoreDisabled = false;	
	ViewHolder viewHolder;
	public ListAdapterForNearByRoute(Context context, ArrayList<ArrayList<String>> listOfAllRoutes) 
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

	public View getView(int position, View convertView, ViewGroup parent) 
	{

		view = convertView;
		if(view == null)
		{
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.list_view_item_tracker_all_routes, null);
			viewHolder.text = (TextView) view.findViewById(R.id.textV_tracker_all_route_title);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_tracker_all_route_sub_title);
			viewHolder.imgV = (Button) view.findViewById(R.id.img_tracker_all_routes_numbering);
			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}
		
		
		
		viewHolder.text.setText(listOfAllRoutes.get(position).get(2));
		viewHolder.textSub.setText(listOfAllRoutes.get(position).get(8));

		if(listOfAllRoutes.get(position).get(0).equalsIgnoreCase("rail"))
		{
			viewHolder.imgV.setBackgroundDrawable(MyApplication.listOfImage.get(listOfAllRoutes.get(position).get(1)));
			viewHolder.imgV.setText("");			
		}
		else
		{
			viewHolder.imgV.setBackgroundResource(R.drawable.bus_stop_icon_bkg);
			viewHolder.imgV.setText(listOfAllRoutes.get(position).get(1));
		}		
	

		return view;
	}
	
	public static class ViewHolder
	{
		TextView text;	
		TextView textSub;
		Button imgV;
	}

}

