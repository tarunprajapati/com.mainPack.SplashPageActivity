package com.tabPack.tabGroup2.listAdapter;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.tabPack.tabGroup2.FavoriteRoutesActivity;

public class ListAdapterForFavorite extends BaseAdapter
{
	private ArrayList<HashMap<String, String>> listOfAllRoutes;

	private Context context;
	private View view;
	private LayoutInflater inflater;
	boolean ignoreDisabled = false;	
	ViewHolder viewHolder;
	public ListAdapterForFavorite(Context context, ArrayList<HashMap<String, String>> listOfAllRoutes) 
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



		viewHolder.text.setText(listOfAllRoutes.get(position).get("stop_name"));
		if(listOfAllRoutes.get(position).containsKey("boundType"))
		{			
			viewHolder.textSub.setText(listOfAllRoutes.get(position).get("boundType"));
		}

		if((MyApplication.listOfImage.containsKey(listOfAllRoutes.get(position).get("color_or_no"))))
		{
			viewHolder.imgV.setBackgroundDrawable(MyApplication.listOfImage.get(listOfAllRoutes.get(position).get("color_or_no")));
			viewHolder.imgV.setText("");			
		}
		else
		{
			viewHolder.imgV.setBackgroundResource(R.drawable.bus_stop_icon_bkg);
			viewHolder.imgV.setText(listOfAllRoutes.get(position).get("color_or_no"));
		}

		if(!FavoriteRoutesActivity.fav_activity)
		{
			if (!HomePageActivity.listOf_True_or_false_of_service.get(position)) 
			{
				viewHolder.text.setTextColor(Color.GRAY);
				viewHolder.textSub.setTextColor(Color.GRAY);
				viewHolder.textSub.setText("NOT IN SERVICE");
			}
			else
			{
				viewHolder.text.setTextColor(Color.WHITE);
				viewHolder.textSub.setTextColor(Color.WHITE);
			}
		}


		return view;
	}
	@Override
	public boolean isEnabled(int position) 
	{	
		if(FavoriteRoutesActivity.fav_activity)
		{
			return true;
		}
		else
		{
			if (HomePageActivity.listOf_True_or_false_of_service.get(position)) 
			{
				return true;
			}
			else
			{
				/*viewHolder.text.setTextColor(Color.GRAY);
			viewHolder.textSub.setTextColor(Color.GRAY);*/
				return false;			
			}
		}
	}

	@Override
	public boolean areAllItemsEnabled() 
	{
		return ignoreDisabled;
	}

	public static class ViewHolder
	{
		TextView text;	
		TextView textSub;
		Button imgV;
	}

}

