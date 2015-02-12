package com.groteam4450.gro;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ShoppingList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
	}
	
	public void goToRecipe(View view) {
		Intent myIntent = new Intent(ShoppingList.this, Recipe.class);
		startActivity(myIntent);
	}
	
	public void goToMain(View view) {
		Intent myIntent = new Intent(ShoppingList.this, PhotoCaptureExample.class);
		startActivity(myIntent);
	}
}
