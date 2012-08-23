package com.tabPack.tabGroup1.listAdapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.application.MyApplication;
import com.mainPack.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterTrainChat extends BaseAdapter
{
	private ArrayList<ArrayList<String>> listOfTrainChat;

	private Context context;
	private View view;
	private LayoutInflater inflater;

	//private  HashMap<String, Drawable> listOfimage;
	public ListAdapterTrainChat(Context context, ArrayList<ArrayList<String>> listOfTrainChat) 
	{
		this.context = context;
		this.listOfTrainChat = listOfTrainChat;	
		//this.listOfimage = listOfImage;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() 
	{	
		return listOfTrainChat.size();
	}

	public Object getItem(int position) 
	{
		return listOfTrainChat.get(position);
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
			view = inflater.inflate(R.layout.train_chat_list_item, null);
			viewHolder.text = (TextView) view.findViewById(R.id.textV_train_chat_item_titele);
			viewHolder.textSub = (TextView) view.findViewById(R.id.textV_train_chat_sub_title);
			viewHolder.imgV = (ImageView) view.findViewById(R.id.img_train_chat_item);
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.text.setText(listOfTrainChat.get(position).get(2));
		viewHolder.textSub.setText(listOfTrainChat.get(position).get(8));
		viewHolder.imgV.setBackgroundDrawable(MyApplication.listOfImage.get(listOfTrainChat.get(position).get(1)));

		return view;
	}
	public static class ViewHolder
	{
		TextView text;	
		TextView textSub;
		ImageView imgV;
	}

}
