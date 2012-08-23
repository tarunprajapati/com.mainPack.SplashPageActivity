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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mainPack.R;
import com.tabPack.tabGroup1.listAdapters.ListAdapterTrainChat.ViewHolder;
import com.tabPack.tabGroup1.train.TrainChatLineListActivity;

public class ListAdapterTrainChatLine extends BaseAdapter
{
	private ArrayList<ArrayList<String>> listOfTrainChat;
	private Context context;
	private View view;
	private LayoutInflater inflater;

	private HashMap<String, Drawable> listOfChatLineImg;
	public ListAdapterTrainChatLine(Context context, ArrayList<ArrayList<String>> listOfTrainChat , HashMap<String, Drawable> listOfChatLineImg) 
	{
		this.context = context;
		this.listOfTrainChat = listOfTrainChat;	
		this.listOfChatLineImg = listOfChatLineImg;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfTrainChat.get(0).size();
	}

	public Object getItem(int position) 
	{
		return listOfTrainChat.get(0).get(position);
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
			viewHolder.imgV = (RelativeLayout) view.findViewById(R.id.rel_btn_chat_line);
			viewHolder.btnImg = (TextView) view.findViewById(R.id.btn_train_chat_line);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_train_chat_line);			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.imgV.setBackgroundDrawable(TrainChatLineListActivity.listOfChatLineImage.get(TrainChatLineListActivity.lineImg));
		viewHolder.textSub.setText(listOfTrainChat.get(1).get(position));
		viewHolder.btnImg.setText(listOfTrainChat.get(0).get(position));

		return view;
	}
	public static class ViewHolder
	{
		TextView btnImg;	
		TextView textSub;
		RelativeLayout imgV;
	}

}
