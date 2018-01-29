package com.akaldobaie.udacity.abnd.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;


/**
 * Created by Abdullah Aldobaie (akdPro) on 1/28/18 at 7:42 PM.
 */

public class ProductDbHelper extends SQLiteOpenHelper
{
	
	public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();
	
	private static final String DATABASE_NAME = "inventory.db";
	
	private static final int DATABASE_VERSION = 1;
	
	public ProductDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
			 + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
			 + ", " + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL"
			 + ", " + ProductEntry.COLUMN_PRODUCT_PRICE + " TEXT NOT NULL"
			 + ", " + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL"
			 + ", " + ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT"
			 + ", " + ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL"
			 + ", " + ProductEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL"
			 + ", " + ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL"
			 + ");";
		
		db.execSQL(SQL_CREATE_PETS_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//TODO: delete old table and create a new one
	}
}
