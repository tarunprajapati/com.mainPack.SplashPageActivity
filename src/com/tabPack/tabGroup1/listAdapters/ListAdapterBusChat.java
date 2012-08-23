package com.tabPack.tabGroup1.listAdapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainPack.R;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChat.ViewHolder;

public class ListAdapterBusChat  extends BaseAdapter
{
	private ArrayList<ArrayList<String>> listOfBusChat;

	private Context context;
	private View view;
	private LayoutInflater inflater;

	private ArrayList<Drawable> listOfimage;
	public ListAdapterBusChat(Context context, ArrayList<ArrayList<String>> listOfBusChat) 
	{
		this.context = context;
		this.listOfBusChat = listOfBusChat;			
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfBusChat.size();
	}

	public Object getItem(int position) 
	{
		return listOfBusChat.get(position);
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
			view = inflater.inflate(R.layout.bus_chat_list_item, null);
			viewHolder.text = (TextView) view.findViewById(R.id.textV_bus_chat_item_titele);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_bus_chat_sub_title);
			viewHolder.imgV = (Button) view.findViewById(R.id.img_bus_chat_numbering);
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.text.setText(listOfBusChat.get(position).get(2));
		viewHolder.textSub.setText(listOfBusChat.get(position).get(8));
		viewHolder.imgV.setText(listOfBusChat.get(position).get(1));

		return view;
	}
	public static class ViewHolder
	{
		TextView text;	
		TextView textSub;
		Button imgV;
	}

}
