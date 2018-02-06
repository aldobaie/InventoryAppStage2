package com.akaldobaie.udacity.abnd.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.akaldobaie.udacity.abnd.inventoryapp.ProductContract.ProductEntry;

public class AddActivity extends AppCompatActivity
	 implements LoaderManager.LoaderCallbacks<Cursor>
{
	
	EditText productNameView;
	EditText productPriceView;
	EditText productQuantityView;
	EditText productImageView;
	EditText supplierNameView;
	EditText supplierEmailView;
	EditText supplierPhoneView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		productNameView = findViewById(R.id.add_product_name_edit_text);
		productPriceView = findViewById(R.id.add_product_price_edit_text);
		productQuantityView = findViewById(R.id.add_product_quantity_edit_text);
		productImageView = findViewById(R.id.add_product_image_edit_text);
		supplierNameView = findViewById(R.id.add_supplier_name_edit_text);
		supplierEmailView = findViewById(R.id.add_supplier_email_edit_text);
		supplierPhoneView = findViewById(R.id.add_supplier_phone_number_edit_text);
	}
	
	private boolean saveProduct()
	{
		String productName = productNameView.getText().toString().trim();
		String productPrice = productPriceView.getText().toString().trim();
		String productQuantity = productQuantityView.getText().toString().trim();
		String productImage = productImageView.getText().toString().trim();
		String supplierName = supplierNameView.getText().toString().trim();
		String supplierEmail = supplierEmailView.getText().toString().trim();
		String supplierPhone = supplierPhoneView.getText().toString().trim();
		
		if (productName.isEmpty())
		{
			productNameView.setError("Required");
			return false;
		}
		if (productPrice.isEmpty())
		{
			productPriceView.setError("Required");
			return false;
		}
		if (productQuantity.isEmpty())
		{
			productQuantityView.setError("Required");
			return false;
		}
		if (supplierName.isEmpty())
		{
			supplierNameView.setError("Required");
			return false;
		}
		if (supplierEmail.isEmpty())
		{
			supplierEmailView.setError("Required");
			return false;
		}
		if (supplierPhone.isEmpty())
		{
			supplierPhoneView.setError("Required");
			return false;
		}
		
		int quantity = Integer.parseInt(productQuantity);
		
		ContentValues values = new ContentValues();
		values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
		values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
		values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
		values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, productImage);
		values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
		values.put(ProductEntry.COLUMN_SUPPLIER_EMAIL, supplierEmail);
		values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhone);
		
		Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
		
		if (newUri == null)
		{
			Toast.makeText(this, R.string.saving_failed, Toast.LENGTH_SHORT).show();
			return false;
		} else
		{
			Toast.makeText(this, getString(R.string.saving_successful), Toast.LENGTH_SHORT).show();
			return true;
		}
	}
	
	boolean isFormEmpty()
	{
		String productName = productNameView.getText().toString().trim();
		String productPrice = productPriceView.getText().toString().trim();
		String productQuantity = productQuantityView.getText().toString().trim();
		String productImage = productImageView.getText().toString().trim();
		String supplierName = supplierNameView.getText().toString().trim();
		String supplierEmail = supplierEmailView.getText().toString().trim();
		String supplierPhone = supplierPhoneView.getText().toString().trim();
		
		return productName.isEmpty()
			 && productPrice.isEmpty()
			 && productQuantity.isEmpty()
			 && productImage.isEmpty()
			 && supplierName.isEmpty()
			 && supplierEmail.isEmpty()
			 && supplierPhone.isEmpty();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_add, menu);
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
			case R.id.action_save:
				if (saveProduct())
					finish();
				return true;
			case android.R.id.home:
				if (isFormEmpty())
					finish();
				else
					showUnsavedChangesDialog();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed()
	{
		if (isFormEmpty())
			finish();
		else
			showUnsavedChangesDialog();
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
	{
		return new CursorLoader(this, ProductEntry.CONTENT_URI,
			 null, null, null, null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
	
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
	
	}
	
	private void showUnsavedChangesDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.unsaved_dialog_msg);
		builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				finish();
			}
		});
		builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener()
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
}