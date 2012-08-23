package com.dataBase;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AllQueriesFavoriteRouteClass
{
	private static ContentValues values;
	//private static Context contextThis;
	private static SQLiteDatabase dataBase;
	
	public static SQLiteDatabase openDataBase(Context context)
	{		 
		return (new OpenHelperClass(context)).getWritableDatabase();
	}
	
	public static void insertFavorite(HashMap<String, String> map , Context context)
	{
		dataBase = openDataBase(context);
		
		values = new ContentValues();
		values.put("stop_name", map.get("stop_name").replaceAll("'", " "));
		values.put("color_or_no", map.get("color_or_no"));
		values.put("boundType", map.get("boundType"));
		values.put("service", map.get("service"));
		
		dataBase.insert("ListOfFavoriteRoute", null, values);	
		
		closeResources(dataBase);
	}
	
	public static Cursor selectFavoriteRoutes(Context context)
	{		
		dataBase = openDataBase(context);
		
		Cursor cursor = dataBase.rawQuery("select * from ListOfFavoriteRoute", null);	
		System.out.println("Cursor count from Base "+cursor.getCount());
		closeResources(dataBase);
		
		return cursor;
	}
	
	public static Cursor selectParticularRow(Context context , String routeName)
	{
		dataBase = openDataBase(context);
		
		Cursor cursor = dataBase.rawQuery("select * from ListOfFavoriteRoute where (stop_name='"+routeName.trim().replaceAll("'", " ")+"')", null);
		System.out.println("cursor count from base "+cursor.getCount());
		
		closeResources(dataBase);
		return cursor;
	}
	
	public static void deleteFavoriteRoutes(Context context , String routeName)
	{
		dataBase = openDataBase(context);
		
		dataBase.execSQL("delete from ListOfFavoriteRoute where stop_name='"+routeName.trim()+"'");
		
		closeResources(dataBase);
	}
	
	public static void closeResources(SQLiteDatabase dataBase) 
	{
		if(dataBase != null)
		{
			dataBase.close();
		}
	}
	
	public static void closeCursor(Cursor cursor) 
	{
		if(cursor != null)
		{
			cursor.close();
		}
	}
}
