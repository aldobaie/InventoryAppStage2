package com.akaldobaie.udacity.abnd.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;

public class DetailActivity extends AppCompatActivity
	 implements LoaderManager.LoaderCallbacks<Cursor>
{
	
	private static final int EXISTING_PRODUCT_LOADER = 0;
	
	private Uri currentProductUri;
	
	TextView productNameView;
	TextView productPriceView;
	TextView productQuantityView;
	TextView productImageView;
	TextView supplierNameView;
	TextView supplierEmailView;
	TextView supplierPhoneView;
	
	ImageButton decreaseQuantityButton;
	ImageButton increaseQuantityButton;
	ImageButton emailSupplierButton;
	ImageButton callSupplierButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		final Intent intent = getIntent();
		currentProductUri = intent.getData();
		
		getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
		
		productNameView = findViewById(R.id.add_product_name_edit_text);
		productPriceView = findViewById(R.id.add_product_price_edit_text);
		productQuantityView = findViewById(R.id.add_product_quantity_edit_text);
		productImageView = findViewById(R.id.add_product_image_edit_text);
		supplierNameView = findViewById(R.id.add_supplier_name_edit_text);
		supplierEmailView = findViewById(R.id.add_supplier_email_edit_text);
		supplierPhoneView = findViewById(R.id.add_supplier_phone_number_edit_text);
		
		decreaseQuantityButton = findViewById(R.id.quantity_decrease_button);
		decreaseQuantityButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int productQuantityInt = Integer.parseInt(productQuantityView.getText().toString());
				if (productQuantityInt > 0)
					updateQuantity(productQuantityInt - 1);
			}
		});
		
		increaseQuantityButton = findViewById(R.id.quantity_increase_button);
		increaseQuantityButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				updateQuantity(Integer.parseInt(productQuantityView.getText().toString()) + 1);
			}
		});
		
		emailSupplierButton = findViewById(R.id.email_button);
		emailSupplierButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, supplierEmailView.getText().toString());
				intent.putExtra(Intent.EXTRA_SUBJECT, productNameView.getText().toString());
				
				startActivity(Intent.createChooser(intent, "Send Email"));
			}
		});
		
		callSupplierButton = findViewById(R.id.call_button);
		callSupplierButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + supplierPhoneView.getText().toString()));
				startActivity(intent);
			}
		});
	}
	
	void updateQuantity(int productQuantityInt)
	{
		ContentValues values = new ContentValues();
		values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantityInt);
		getContentResolver().update(currentProductUri, values, null, null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_detail, menu);
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
			case R.id.action_edit:
				Intent intent = new Intent(DetailActivity.this, EditActivity.class);
				intent.setData(currentProductUri);
				startActivity(intent);
				return true;
			case R.id.action_delete:
				showDeleteDialog();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
	{
		return new CursorLoader(this, currentProductUri,
			 null, null, null, null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		if (cursor == null || cursor.getCount() < 1)
		{
			return;
		}
		
		if (cursor.moveToFirst())
		{
			int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
			int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
			int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
			int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);
			int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
			int supplierEmailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_EMAIL);
			int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
			
			String productName = cursor.getString(nameColumnIndex);
			String productPrice = cursor.getString(priceColumnIndex);
			String productQuantity = cursor.getString(quantityColumnIndex);
			String productImage = cursor.getString(imageColumnIndex);
			String supplierName = cursor.getString(supplierNameColumnIndex);
			String supplierEmail = cursor.getString(supplierEmailColumnIndex);
			String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
			
			productNameView.setText(productName);
			productPriceView.setText(productPrice);
			productQuantityView.setText(productQuantity);
			productImageView.setText(productImage);
			supplierNameView.setText(supplierName);
			supplierEmailView.setText(supplierEmail);
			supplierPhoneView.setText(supplierPhone);
		}
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
	
	}
	
	private void showDeleteDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_dialog_msg);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				deleteProduct();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if (dialog != null)
				{
					dialog.dismiss();
				}
			}
		});
		
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	private void deleteProduct()
	{
		if (currentProductUri != null)
		{
			int deletedUri = getContentResolver().delete(currentProductUri, null, null);
			
			if (deletedUri == 0)
			{
				Toast.makeText(this, getString(R.string.deleting_failed), Toast.LENGTH_SHORT).show();
			} else
			{
				Toast.makeText(this, getString(R.string.deleting_successful), Toast.LENGTH_SHORT).show();
			}
		}
		
		finish();
	}
}