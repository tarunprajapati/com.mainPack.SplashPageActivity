package com.tabPack.tabGroup1.listAdapters;

import java.util.ArrayList;
import java.util.HashMap;

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

public class ListAdapterBusChatLine extends BaseAdapter
{
	private ArrayList<ArrayList<String>> listOfBusChat;

	private Context context;
	private View view;
	private LayoutInflater inflater;

	private HashMap<String, Drawable> listOfChatLineImg;
	public ListAdapterBusChatLine(Context context, ArrayList<ArrayList<String>> listOfBusChat) 
	{
		this.context = context;
		this.listOfBusChat = listOfBusChat;			
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfBusChat.get(0).size();
	}

	public Object getItem(int position) 
	{
		return listOfBusChat.get(0).get(position);
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
			view = inflater.inflate(R.layout.train_chat_line_list_item, null);
			viewHolder.btnImg = (TextView) view.findViewById(R.id.btn_train_chat_line);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_train_chat_line);			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		
		viewHolder.textSub.setText(listOfBusChat.get(1).get(position));
		viewHolder.btnImg.setText(listOfBusChat.get(0).get(position));

		return view;
	}
	public static class ViewHolder
	{
		TextView btnImg;	
		TextView textSub;
		ImageView imgV;
	}

}
