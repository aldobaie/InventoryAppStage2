package com.akaldobaie.udacity.abnd.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/*
 * Created by Abdullah Aldobaie (akdPro) on 1/28/18 at 7:41 PM.
 *
 */
public final class ProductContract
{
	
	private ProductContract()
	{
	}
	
	public static final String CONTENT_AUTHORITY = "com.akaldobaie.udacity.abnd.inventoryapp";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_PRODUCTS = "products";
	
	
	public static final class ProductEntry implements BaseColumns
	{
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
		public static final String CONTENT_LIST_TYPE =
			 ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
		public static final String CONTENT_ITEM_TYPE =
			 ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
		
		public final static String TABLE_NAME = "products";
		public final static String _ID = BaseColumns._ID;
		
		public final static String COLUMN_PRODUCT_NAME = "product_name";
		public final static String COLUMN_PRODUCT_PRICE = "price";
		public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
		public final static String COLUMN_PRODUCT_IMAGE = "product_image";
		public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
		public final static String COLUMN_SUPPLIER_EMAIL = "supplier_email";
		public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
	}
}
