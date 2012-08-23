package com.tabPack.tabGroup1.listAdapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanClasses.ChatRoomObject;
import com.mainPack.R;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;
import com.tabPack.tabGroup1.train.TrainChatRoomActivity;

public class ListAdapterTrainChatRoom extends BaseAdapter
{
	private ArrayList<ChatRoomObject> listOfTrainChat;
	private Context context;
	private View view;
	private LayoutInflater inflater;

	private HashMap<String, Drawable> listOfChatLineImg;
	public ListAdapterTrainChatRoom(Context context) 
	{
		this.context = context;
		this.listOfTrainChat = listOfTrainChat;	
		this.listOfChatLineImg = listOfChatLineImg;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return TrainChatRoomActivity.listOfChat.size();
	}

	public Object getItem(int position) 
	{
		return TrainChatRoomActivity.listOfChat.get(position);
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
			view = inflater.inflate(R.layout.chat_room, null);
			viewHolder.textV_chatterName = (TextView) view.findViewById(R.id.textV_chatter_name);
			viewHolder.textV_dayAgo = (TextView) view.findViewById(R.id.textV_msg_days_ago);
			viewHolder.textV_mgsBody = (EditText) view.findViewById(R.id.textV_chat_msg_body);
			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		
		viewHolder.textV_chatterName.setText(TrainChatRoomActivity.listOfChat.get(position).getUserName());
		viewHolder.textV_dayAgo.setText(TrainChatRoomActivity.listOfChat.get(position).getTimeSeconds());
		viewHolder.textV_mgsBody.setText(TrainChatRoomActivity.listOfChat.get(position).getBody());

		return view;
	}
	public static class ViewHolder
	{
		TextView textV_chatterName;	
		TextView textV_dayAgo;	
		EditText textV_mgsBody;
	}

}
