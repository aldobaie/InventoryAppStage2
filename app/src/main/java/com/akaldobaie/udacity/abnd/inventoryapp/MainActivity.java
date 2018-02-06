package com.akaldobaie.udacity.abnd.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements
	 LoaderManager.LoaderCallbacks<Cursor>
{
	
	private static final int PRODUCT_LOADER = 0;
	
	ProductCursorAdapter productCursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		productCursorAdapter = new ProductCursorAdapter(this, null);
		
		ListView productListView = findViewById(R.id.product_list_view);
		View emptyListView = findViewById(R.id.empty_view);
		
		productListView.setEmptyView(emptyListView);
		productListView.setAdapter(productCursorAdapter);
		
		getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
	{
		String[] projection = {
			 ProductEntry._ID,
			 ProductEntry.COLUMN_PRODUCT_NAME,
			 ProductEntry.COLUMN_PRODUCT_PRICE,
			 ProductEntry.COLUMN_PRODUCT_QUANTITY};
		
		return new CursorLoader(this, ProductEntry.CONTENT_URI,
			 projection, null, null, null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		productCursorAdapter.swapCursor(data);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		productCursorAdapter.swapCursor(null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_add:
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}