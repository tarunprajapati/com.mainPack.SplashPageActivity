package com.dataBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelperClass extends SQLiteOpenHelper 
{
	//public static final String DB_PATH="/data/data/com.mainPack/databases/";
	public static final String DB_NAME="FavoriteRoutes.sqlite";
	public static final int dataBaseVersion=101;

	public static String createTable="create table IF NOT EXISTS ListOfFavoriteRoute(stop_name VARCHAR primary key not null, color_or_no VARCHAR, boundType VARCHAR , service VARCHAR)";
	
	public OpenHelperClass(Context context) 
	{		
		super(context, DB_NAME, null, 1);
		System.out.println("OpenHelper Constructor");
	}
	@Override
	public void onCreate(SQLiteDatabase db) 
	{		
		System.out.println("Creating Teble");
		db.execSQL(createTable);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		System.out.println("Upgrading table");
		onCreate(db);		
	}	
}

