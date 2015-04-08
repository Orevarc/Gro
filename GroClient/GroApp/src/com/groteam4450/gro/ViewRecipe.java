package com.groteam4450.gro;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ViewRecipe extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		
		
	}
	
	public void goToShopping(View view) {
		Intent myIntent = new Intent(ViewRecipe.this, ShoppingList.class);
		startActivity(myIntent);
	}
	
	public void goToMain(View view) {
		Intent myIntent = new Intent(ViewRecipe.this, MyFood.class);
		startActivity(myIntent);
	}
	
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(ViewRecipe.this, MyFood.class);
	   startActivity(setIntent);
	}
}
