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

public class ShoppingList extends Activity {
	
	AsyncHttpClient client = new AsyncHttpClient();
	HTTPRestClient httpClient = new HTTPRestClient();
	protected ArrayList<ShoppingListItem> chosenItemList;	
	protected ListView shoppingItemListView;
	
	protected ImageButton button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		
		button = (ImageButton) findViewById (R.id.addItemButton);
        button.setOnClickListener( new View.OnClickListener () {
        	@Override
        	public void onClick(View v) {
        		onAddListItemClick();
        	}
        });
		
		shoppingItemListView = (ListView) findViewById(R.id.shoppingListView);
		
		chosenItemList = new ArrayList<ShoppingListItem>();	
		
		getShoppingListItems();	
		
        ShoppingListItemAdapter arrayAdapter = new ShoppingListItemAdapter(this, chosenItemList);
        
        shoppingItemListView.setAdapter(arrayAdapter);
	}
	
	public void postShoppingListItem (String addedItemName) {
		String url = "addshoppingitem";
		Gson gson = new Gson();
		
		String authString = getAuthToken();
		
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.put("item", addedItemName);
    	
		httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	
					String addedItemID = null;
					String addedItemName = null;
					try {
						addedItemID = response.getString("id");
						addedItemName = response.getString("itemName");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					chosenItemList.add(new ShoppingListItem(addedItemID, addedItemName));
					initItemList();
	            }     
	        });
	}
    	
	public void getShoppingListItems () {
		String url = "shoppingitems";	
		String authString = getAuthToken();
		
		Header[] headers = {
				new BasicHeader("Authorization","Bearer " + authString)
		};
		
		httpClient.getAuth(url, headers, null, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray itemArray = response.getJSONArray("items");
						
						populateShoppingListItems(itemArray);
						
						initItemList();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });
	}
	
	public void populateShoppingListItems (JSONArray itemArray) throws JSONException {
		for (int i = 0; i<itemArray.length(); i++) {
			JSONObject object = itemArray.getJSONObject(i);
			
			ShoppingListItem item = new ShoppingListItem (object.get("id").toString(), object.get("itemName").toString());
			
			System.out.println(item.itemName);
			
			chosenItemList.add(item);
		}
	}
	
	//This is performed after the user presses OK in the manual entry dialog window
	public void onAddListItemClick () {

		LayoutInflater li = LayoutInflater.from(this);
		View enterPromptsView = li.inflate(R.layout.enter_shopping_item_prompt, null);		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		alertDialogBuilder.setView(enterPromptsView);
		
		final EditText userInput = (EditText) enterPromptsView.findViewById(R.id.editTextDialogUserInput);
		
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            
	        	String newItemText = userInput.getText().toString();
	        	
	        	if (newItemText.length() < 1 || newItemText.length() > 20){
	        		
	        	}
	        	else {
	        		postShoppingListItem(newItemText);
	        	}
	        }
	     });
		alertDialogBuilder.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
			dialog.cancel();
		    }
		  });
		
		alertDialogBuilder.show();
		
		initItemList();
	}
	
	public void initItemList () {
        ShoppingListItemAdapter arrayAdapter = new ShoppingListItemAdapter(this, chosenItemList);
        
        shoppingItemListView.setAdapter(arrayAdapter);
	}
	
	public String getAuthToken() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String authString = settings.getString ("token", "");
		return authString;
	}
	
	public void goToRecipe(View view) {
		Intent myIntent = new Intent(ShoppingList.this, RecipeActivity.class);
		startActivity(myIntent);
	}
	
	public void goToMain(View view) {
		Intent myIntent = new Intent(ShoppingList.this, MyFood.class);
		startActivity(myIntent);
	}
	
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(ShoppingList.this, MyFood.class);
	   startActivity(setIntent);
	}
}
