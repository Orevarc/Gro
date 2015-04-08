package com.groteam4450.gro;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.groteam4450.gro.MyFood.ButtonClickHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeActivity extends Activity {
	
	AsyncHttpClient client = new AsyncHttpClient();
	HTTPRestClient httpClient = new HTTPRestClient();
	protected RecipeItem recipeItem = new RecipeItem();
	
	protected ImageButton button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_view);
		
		recipeItem = Globals.globalRecipeItem;
		
		initializeView();
		
		setContentView(R.layout.activity_recipe_view);
	}
	
	public void initializeView () {
		String ingredientsString = "";
		
		LayoutInflater li = LayoutInflater.from(this);
		final View recipeView = li.inflate(R.layout.activity_recipe_view, null);
		TextView recipeNameTextView = (TextView) recipeView.findViewById(R.id.recipeName);
		TextView totalTimeInSecondsView = (TextView) recipeView.findViewById(R.id.totalTime);
		TextView ingredientsView = (TextView) recipeView.findViewById(R.id.ingredients);
		//TextView ratingView = (TextView) recipeView.findViewById(R.id.ratin)
		TextView instructionsView = (TextView) recipeView.findViewById(R.id.instructions);
		
		recipeNameTextView.setText(recipeItem.recipeName);
		totalTimeInSecondsView.setText("3600");
		
		for (int i=0; i < recipeItem.ingredients.size();i++) {
			ingredientsString += recipeItem.ingredients.get(i) + "\n";
		}
		
		ingredientsView.setClickable(true);
		ingredientsView.setMovementMethod(LinkMovementMethod.getInstance());	
		String text = "<a href='http://www.yummly.com/recipe/Southwest-Casserole-1029095'> Preparation Steps </a>";
		ingredientsView.setText(Html.fromHtml(text));

	}

	
	public String getAuthToken() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String authString = settings.getString ("token", "");
		return authString;
	}
	
	public void goToMain(View view) {
		Intent myIntent = new Intent(RecipeActivity.this, MyFood.class);
		startActivity(myIntent);
	}
	
	public void goToShopping(View view) {
		Intent myIntent = new Intent(RecipeActivity.this, ShoppingList.class);
		startActivity(myIntent);
	}
	
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(RecipeActivity.this, MyFood.class);
	   startActivity(setIntent);
	}
}
