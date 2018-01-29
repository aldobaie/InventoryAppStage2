package com.akaldobaie.udacity.abnd.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
	
	private final String LOG_TAG = "MainActivity";
	private ProductDbHelper mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDbHelper = new ProductDbHelper(this);
		
		insertData();
		insertData();
		insertData();
		
		Cursor cursor = queryData();
		
		Log.i(LOG_TAG, Arrays.toString(cursor.getColumnNames()));
		
		while (cursor.moveToNext())
		{
			String row = "";
			row += String.valueOf(cursor.getInt(0)) + '\t';
			row += cursor.getString(1) + '\t';
			row += cursor.getString(2) + '\t';
			row += String.valueOf(cursor.getInt(3)) + '\t';
			row += cursor.getString(4) + '\t';
			row += cursor.getString(5) + '\t';
			row += cursor.getString(6) + '\t';
			row += cursor.getString(7) + '\t';
			
			Log.i(LOG_TAG, row);
		}
		
		cursor.close();
	}
	
	private void insertData()
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Product_1");
		values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "$9.99");
		values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 1);
		values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, "imageUri");
		values.put(ProductEntry.COLUMN_SUPPLIER_NAME, "SuperSupply");
		values.put(ProductEntry.COLUMN_SUPPLIER_EMAIL, "super@supply.com");
		values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "(911)234-8877");
		
		long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
		
		if (newRowId == -1)
		{
			Toast.makeText(this, "Error with adding product", Toast.LENGTH_SHORT).show();
		} else
		{
			Log.i(LOG_TAG, "Producted added with row id: " + newRowId);
			Toast.makeText(this, "Product added with row id: " + newRowId, Toast.LENGTH_SHORT).show();
		}
	}
	
	private Cursor queryData()
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		String[] projection = {
			 ProductEntry._ID,
			 ProductEntry.COLUMN_PRODUCT_NAME,
			 ProductEntry.COLUMN_PRODUCT_PRICE,
			 ProductEntry.COLUMN_PRODUCT_QUANTITY,
			 ProductEntry.COLUMN_PRODUCT_IMAGE,
			 ProductEntry.COLUMN_SUPPLIER_NAME,
			 ProductEntry.COLUMN_SUPPLIER_EMAIL,
			 ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER};
		
		return db.query(ProductEntry.TABLE_NAME, projection, null, null, null, null, null);
	}
}
