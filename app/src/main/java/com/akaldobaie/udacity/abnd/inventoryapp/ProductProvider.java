package com.akaldobaie.udacity.abnd.inventoryapp;

/*
 * Created by Abdullah Aldobaie (akdPro) on 2/4/18 at 5:55 PM.
 *
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider
{
	
	public static final String LOG_TAG = ProductProvider.class.getSimpleName();
	
	private static final int PRODUCTS = 100;
	private static final int PRODUCT_ID = 101;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static
	{
		// MULTIPLE rows
		uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
		
		// ONE single row
		uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
	}
	
	private ProductDbHelper dbHelper;
	
	@Override
	public boolean onCreate()
	{
		dbHelper = new ProductDbHelper(getContext());
		return true;
	}
	
	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor;
		
		int match = uriMatcher.match(uri);
		switch (match)
		{
			case PRODUCTS:
				cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
					 null, null, sortOrder);
				break;
			case PRODUCT_ID:
				selection = ProductEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
					 null, null, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Cannot query unknown URI " + uri);
		}
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	@Override
	public Uri insert(@NonNull Uri uri, ContentValues contentValues)
	{
		final int match = uriMatcher.match(uri);
		switch (match)
		{
			case PRODUCTS:
				return insertProduct(uri, contentValues);
			default:
				throw new IllegalArgumentException("Insertion is not supported for " + uri);
		}
	}
	
	private Uri insertProduct(Uri uri, ContentValues values)
	{
		// Check if Non-null values are not null
		String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
		if (name == null)
		{
			throw new IllegalArgumentException("Product requires a name");
		}
		
		String price = values.getAsString(ProductEntry.COLUMN_PRODUCT_PRICE);
		if (price == null)
		{
			throw new IllegalArgumentException("Product requires a price");
		}
		
		Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
		if (quantity == null)
		{
			throw new IllegalArgumentException("Product requires a valid quantity");
		}
		
		String supplierName = values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME);
		if (supplierName == null)
		{
			throw new IllegalArgumentException("Product requires a supplier name");
		}
		
		String supplierEmail = values.getAsString(ProductEntry.COLUMN_SUPPLIER_EMAIL);
		if (supplierEmail == null)
		{
			throw new IllegalArgumentException("Product requires a supplier email");
		}
		
		String supplierPhoneNumber = values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
		if (supplierPhoneNumber == null)
		{
			throw new IllegalArgumentException("Product requires a supplier phone number");
		}
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		long id = database.insert(ProductEntry.TABLE_NAME, null, values);
		if (id == -1)
		{
			Log.e(LOG_TAG, "Failed to insert row for " + uri);
			return null;
		}
		
		// Notify all listeners
		getContext().getContentResolver().notifyChange(uri, null);
		
		// Return the new URI with the ID
		return ContentUris.withAppendedId(uri, id);
	}
	
	@Override
	public int update(@NonNull Uri uri, ContentValues contentValues, String selection,
	                  String[] selectionArgs)
	{
		final int match = uriMatcher.match(uri);
		switch (match)
		{
			case PRODUCTS:
				return updateProduct(uri, contentValues, selection, selectionArgs);
			case PRODUCT_ID:
				selection = ProductEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				return updateProduct(uri, contentValues, selection, selectionArgs);
			default:
				throw new IllegalArgumentException("Update is not supported for " + uri);
		}
	}
	
	private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		
		// Check if Non-null values are not null
		if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME))
		{
			String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
			if (name == null)
			{
				throw new IllegalArgumentException("Product requires a name");
			}
		}
		
		if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE))
		{
			String price = values.getAsString(ProductEntry.COLUMN_PRODUCT_PRICE);
			if (price == null)
			{
				throw new IllegalArgumentException("Product requires a price");
			}
		}
		
		if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY))
		{
			Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
			if (quantity == null)
			{
				throw new IllegalArgumentException("Product requires a valid quantity");
			}
		}
		
		if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_NAME))
		{
			String supplierName = values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME);
			if (supplierName == null)
			{
				throw new IllegalArgumentException("Product requires a supplier name");
			}
		}
		
		if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_EMAIL))
		{
			String supplierEmail = values.getAsString(ProductEntry.COLUMN_SUPPLIER_EMAIL);
			if (supplierEmail == null)
			{
				throw new IllegalArgumentException("Product requires a supplier email");
			}
		}
		
		if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER))
		{
			String supplierPhoneNumber = values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
			if (supplierPhoneNumber == null)
			{
				throw new IllegalArgumentException("Product requires a supplier phone number");
			}
		}
		
		if (values.size() == 0)
		{
			return 0;
		}
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		int rowsUpdated = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
		
		if (rowsUpdated != 0)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return rowsUpdated;
	}
	
	@Override
	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		int rowsDeleted;
		final int match = uriMatcher.match(uri);
		
		switch (match)
		{
			case PRODUCTS:
				rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case PRODUCT_ID:
				selection = ProductEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Deletion is not supported for " + uri);
		}
		
		if (rowsDeleted != 0)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return rowsDeleted;
	}
	
	@Override
	public String getType(@NonNull Uri uri)
	{
		final int match = uriMatcher.match(uri);
		switch (match)
		{
			case PRODUCTS:
				return ProductEntry.CONTENT_LIST_TYPE;
			case PRODUCT_ID:
				return ProductEntry.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
		}
	}
}