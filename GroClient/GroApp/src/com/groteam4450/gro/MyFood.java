package com.groteam4450.gro;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.*;

import com.google.gson.Gson;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

public class MyFood extends Activity implements SwipeActionAdapter.SwipeActionListener {
	
	AsyncHttpClient client = new AsyncHttpClient();

	HTTPRestClient httpClient = new HTTPRestClient();
	
	protected ReceiptParser receiptParser = new ReceiptParser();
	
	protected ImageButton button;
	protected ImageButton addItemButton;
	protected ImageButton addToShoppingListButton;
	protected ImageButton queryRecipeButton;
	
	//protected ImageView _image;
	protected String _path;
	protected boolean _taken;
	protected Bitmap bitmap;
	protected ExifInterface exif = null;
	FoodItemAdapter arrayAdapter;
	protected ArrayList<FoodItem> ownedFoodList;
	protected ArrayList<BasicFoodItem> addedFoodList; //converts food list items into basic items to send with HTTP Post
	protected ArrayList<FoodCategory> foodCategoryList;
	protected ArrayList<String> categoryNameList;
	protected ArrayList<SelectedFoodItem> selectedItemList;
	protected ArrayList<Item> categoryViewItemList;
	protected ArrayList<RecipeItem> recipeList;
	
	protected ArrayList<Integer> headerPositions;
	protected ListView foodListView;
	
	protected List<Integer> selected;
	
	protected SwipeActionAdapter mAdapter;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);
       
        initButtons();
        
        _path = "/storage/sdcard0/images/make_machine_example.jpg";
        
        foodListView = (ListView) findViewById(R.id.listView1);
        //foodListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        ownedFoodList = new ArrayList<FoodItem>();
        selectedItemList = new ArrayList<SelectedFoodItem>();
        
        try {
			getFoodItems();
			getCategoryItems();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        initFoodList();
    }
    
    public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
    		startCameraActivity();
    	}
    }
    
    protected void startCameraActivity()
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( _path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	startActivityForResult( intent, 0 );
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken();
    			break;
    	}
    }
    
    protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	//_image.setImageBitmap(bitmap);
    	
		try {
			exif = new ExifInterface(_path);
		} catch (IOException e) {
			System.out.println("Caught ExifInterface exception");
			e.printStackTrace();
		}
    	int exifOrientation = exif.getAttributeInt(
    	        ExifInterface.TAG_ORIENTATION,
    	        ExifInterface.ORIENTATION_NORMAL);

    	int rotate = 0;

    	switch (exifOrientation) {
    	case ExifInterface.ORIENTATION_ROTATE_90:
    	    rotate = 90;
    	    break;
    	case ExifInterface.ORIENTATION_ROTATE_180:
    	    rotate = 180;
    	    break;
    	case ExifInterface.ORIENTATION_ROTATE_270:
    	    rotate = 270;
    	    break;
    	}

    	if (rotate != 0) {
    	    int w = bitmap.getWidth();
    	    int h = bitmap.getHeight();

    	    // Setting pre rotate
    	    Matrix mtx = new Matrix();
    	    mtx.preRotate(rotate);

    	    // Rotating Bitmap & convert to ARGB_8888, required by tess
    	    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    	}
    	bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    	performOCR();
    }
    
    @Override 
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( MyFood.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
   
    @Override
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( MyFood.PHOTO_TAKEN, _taken );
    }
    
	public void OCR(View view) {
		onPhotoTaken();
	}
	
    public void performOCR()
    {
    	String filepath = "/storage/sdcard0/InitialOCR/OCRTextCaptured.txt";
    	System.out.println("Performing OCR");
    	TessBaseAPI baseApi = new TessBaseAPI();
    	
    	// DATA_PATH = Path to the storage
    	// lang = for which the language data exists, usually "eng"
    	baseApi.init("/storage/sdcard0/OCR", "eng");
    	baseApi.setImage(bitmap);
    	String recognizedText = baseApi.getUTF8Text();
    	baseApi.end();
    	System.out.println(recognizedText);
    	try {
    		File file = new File(filepath);
			PrintWriter out = new PrintWriter(file);
			out.println(recognizedText);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			afterOCR(filepath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    //This function performs all relevant tasks after OCR
    //Various lists are reset, and the view is also reset
    public void afterOCR(String filepath) throws Exception 
    {
    	addedFoodList = receiptParser.parseTextToArray(filepath);
    	postFoodItems();
    	//getFoodItems();
    	//addedFoodList = new ArrayList<BasicFoodItem>();
    	//initFoodList();
    }
    
    
    //ALL RELEVANT POST/GET REQUEST METHODS HERE
    //
    //
    public void postItemsToShoppingList() {
    	String url = "addshoppingitems";
    	Gson gson = new Gson();
    	 	
    	String authString = getAuthToken();
    	
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.add("items", gson.toJson(selectedItemList));
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 		
	            }
	        });	
    }
    
    public void postItemsToQueryRecipe() {
    	String url = "recipesbyingredients";
    	Gson gson = new Gson();
    	 	
    	String authString = getAuthToken();
    	
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.add("items", gson.toJson(selectedItemList));
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	try {

				 		JSONArray itemArray = response.getJSONArray("matches");
						populateRecipeList(itemArray);
						
//						Intent myIntent = new Intent(PhotoCaptureExample.this, SpecificRecipeActivity.class);
//						startActivity(myIntent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });	
    }
    
    public void postItemUsed (String ownershipID, int position) {
    	String url = "markused";
    	Gson gson = new Gson();
		
    	final int itemPos = position;
    	
		String authString = getAuthToken();
		
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.put("ownershipID", ownershipID);
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	
				 	ownedFoodList.remove(itemPos);
				 	initFoodList();
	            }
	        });	
    }
    
    public void postItemWasted (String ownershipID, int position) {
    	String url = "markused";
    	Gson gson = new Gson();
		
    	final int itemPos = position;
    	
		String authString = getAuthToken();
		
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.put("ownershipID", ownershipID);
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	
				 	ownedFoodList.remove(itemPos);
				 	initFoodList();
	            }
	        });	
    }
    
    //This is done on manual entry of food items
    //This function is called after manual entry is OK'd by the user
    public void postFoodItem(String addedItemUPC) throws Exception
    {
    	String url = "additem";
		Gson gson = new Gson();
		
		String authString = getAuthToken();
		
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.put("upc", addedItemUPC);
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	Boolean successValue = null;
				 	String returnUPCCode = null;
				 	
				 	try {
						successValue = response.getBoolean("success");
						returnUPCCode = response.getString("upc");
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				 	
				 	if (successValue)
				 	{
				 		String addedItemOwnerID = null;
				 		String addedItemName = null;
				 		String addedItemUPC = null;
				 		String addedItemGeneralCategory = null;
				 		String addedItemFactualCategory = null;
				 		Long addedItemExpiryDate = null; 
					
				 		try {
				 			addedItemOwnerID = response.getString("ownershipID");
				 			addedItemName = response.getString("itemName");
				 			addedItemUPC = response.getString("upcCode");
				 			addedItemGeneralCategory = response.getString("generalCategory");
				 			addedItemFactualCategory = response.getString("factualCategory");
				 			addedItemExpiryDate = response.getLong("expiryDate");
				 		} catch (JSONException e) {
						// TODO Auto-generated catch block
				 			e.printStackTrace();
				 		}
					
				 		Date expiryDate = new Date (addedItemExpiryDate);
				 		FoodItem addedItem = new FoodItem(addedItemOwnerID, expiryDate, addedItemUPC,
						addedItemName, addedItemFactualCategory, addedItemGeneralCategory);
				 		try {
							getFoodItems();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				 	}
				 	else
				 	{
				 		upcNotFoundDialog(returnUPCCode);
				 	}	 
					
	            }
	        });	
    }
    
    public void postManualFoodItem (String upcCode, String itemName, String foodCategoryName, String tag) {
    	String url = "manualadditem";
    	Gson gson = new Gson();
		
		String authString = getAuthToken();
		
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.put("upcCode", upcCode);
    	params.put("itemName", itemName);
    	params.put("factualCategory", foodCategoryName);
    	params.put("generalTag", tag);
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 	try {
						getFoodItems();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });	
    }
    
    //This is done after the OCR has parsed the receipt photo into text.
    //This function will only be called when OCR is done.
    //Manual entry is handled by postFoodItem()
    public void postFoodItems() throws Exception 
    {
    	String url = "additems";
    	Gson gson = new Gson();
    	
    	String authString = getAuthToken();
    	
    	Header[] headers = {
    			new BasicHeader("Authorization", "Bearer " + authString)
    	};
    	
    	RequestParams params = new RequestParams();
    	params.add("items", gson.toJson(addedFoodList));
    	
    	httpClient.postAuth(url, headers, params, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 		try {
							getFoodItems();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            }
	        });	
    }
    
	public void getFoodItems() throws Exception {
		String url = "useritems";	
		String authString = getAuthToken();
		
		Header[] headers = {
				new BasicHeader("Authorization","Bearer " + authString)
		};
		
		httpClient.getAuth(url, headers, null, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray itemArray = response.getJSONArray("items");
						
						populateOwnedFoodItems(itemArray);
						
						initFoodList();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });	
	}
	
	public void getCategoryItems() throws Exception {
		String url = "foodcategories";
		String authString = getAuthToken();
		
		foodCategoryList = new ArrayList<FoodCategory>();
		categoryNameList = new ArrayList<String>();
		
		Header[] headers = {
				new BasicHeader("Authorization","Bearer " + authString)
		};
		
		httpClient.getAuth(url, headers, null, new JsonHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray itemArray = response.getJSONArray("foodcategories");
						
						populateCategoryList(itemArray);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });	
	}
	
	public void populateCategoryList (JSONArray itemArray) throws JSONException {
		for (int i = 0; i<itemArray.length(); i++) {
			JSONObject object = itemArray.getJSONObject(i);
			
			FoodCategory category = new FoodCategory(object.getInt("foodCategoryID"), object.get("factualCategory").toString());
			String categoryName = category.categoryName;
			
			foodCategoryList.add(category);
			categoryNameList.add(categoryName);
		}
	}
	
	public void populateOwnedFoodItems (JSONArray itemArray) throws JSONException {
		ownedFoodList = new ArrayList<FoodItem>();
		for (int i = 0; i<itemArray.length(); i++) {
			JSONObject object = itemArray.getJSONObject(i);
			Date expiryDate = new Date();
			
			long expiryTime = 0;
			
			if (object.getLong("expiryDate") != 0) {
				expiryTime = object.getLong("expiryDate");
				expiryDate = new Date (expiryTime);
			}
			else
				expiryDate = null;
			
			FoodItem item = new FoodItem(object.get("ownershipID").toString(), expiryDate, 
					object.get("upcCode").toString(), object.get("itemName").toString(), object.get("factualCategory").toString(),
					object.get("generalCategory").toString(), object.getString("generalTag"));
			System.out.println(item.itemName);
			
			ownedFoodList.add(item);
		}
	}
	
	public void populateRecipeList (JSONArray itemArray) throws JSONException {
		recipeList = new ArrayList<RecipeItem>();
		ArrayList<String> ingredientsArrayList = new ArrayList<String>();
		
		JSONObject object = itemArray.getJSONObject(0);
		
		String recipeName = object.getString("recipeName");
		int totalTime = object.getInt("totalTimeInSeconds");
		JSONArray ingredientsArray = object.getJSONArray("ingredients");
		int rating = object.getInt("rating");
		
		for (int i=0; i < ingredientsArray.length(); i++) {
			//JSONObject ingredientObject = ingredientsArray.getJSONObject(i);
			
			ingredientsArrayList.add(ingredientsArray.getString(0));
		}
		
		RecipeItem newRecipe = new RecipeItem (recipeName, totalTime, ingredientsArrayList, rating);
		recipeList.add(newRecipe);
		Globals.globalRecipeItem = newRecipe;
		//JSONObject recipeNameObject = newItemArray.getJSONObject()
	}
	
	public String getAuthToken() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String authString = settings.getString ("token", "");
		return authString;
	}	
	
	public void initButtons() {
        button = ( ImageButton ) findViewById( R.id.button );
        button.setOnClickListener( new ButtonClickHandler() );
        
        addItemButton = (ImageButton) findViewById (R.id.manualEntryButton);
        addItemButton.setOnClickListener( new View.OnClickListener () {
        	@Override
        	public void onClick(View v) {
        		onAddFoodItemClickDialog();
        	}
        });
        
        addToShoppingListButton = (ImageButton) findViewById (R.id.addToShoppingListButton);
        addToShoppingListButton.setOnClickListener( new View.OnClickListener () {
        	@Override
        	public void onClick(View v) {
        		onAddToShoppingListClick();
        	}
        });
        
        queryRecipeButton = (ImageButton) findViewById (R.id.findRecipeButton);
        queryRecipeButton.setOnClickListener( new View.OnClickListener () {
        	@Override
        	public void onClick(View v) {
        		onQueryForRecipeClick();
        	}
        });
	}
	
	public void initFoodList() {
		ArrayList<Item> itemList = convertFoodItemsToItems();
        arrayAdapter = new FoodItemAdapter(this, R.layout.food_list_layout, itemList);
        
        mAdapter = new SwipeActionAdapter(arrayAdapter);
        mAdapter.setSwipeActionListener(this).setListView(foodListView);
        foodListView.setAdapter(mAdapter);
        
        mAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left)
        		.addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right)
        		.addBackground(SwipeDirections.DIRECTION_FAR_RIGHT, R.layout.row_bg_right_far)
        		.addBackground(SwipeDirections.DIRECTION_FAR_LEFT, R.layout.row_bg_left_far);       
	}
	
	public ArrayList<Item> convertFoodItemsToItems () {
		categoryViewItemList = new ArrayList<Item>();
		ArrayList<CategoryHeader> tempHeader = new ArrayList<CategoryHeader>();
		ArrayList<CategoryHeader> testList = new ArrayList<CategoryHeader>();
		ArrayList<FoodItem> tempItems = new ArrayList<FoodItem>();
		headerPositions = new ArrayList<Integer>();
		
		if (ownedFoodList.size() != 0)
		{
			tempHeader.add(new CategoryHeader(ownedFoodList.get(0).generalCategory));
			
	    	outerLoop: for (FoodItem i: ownedFoodList)
	    	{
	    		for(CategoryHeader c: tempHeader) {
	    			if(i.generalCategory.equals(c.name)) { continue outerLoop; }
	    		}
	    		tempHeader.add(new CategoryHeader(i.generalCategory));
	    	}
			int count = 0;
			outerLoop: for (CategoryHeader i: tempHeader)
			{
				categoryViewItemList.add(i);
				headerPositions.add(count);
				count++;
				for (FoodItem c: ownedFoodList) {
					if (i.name.equals(c.generalCategory)){
						categoryViewItemList.add(c);
						count++;
					}
				}					
			}
	    	
		}		
		return categoryViewItemList;	
	}
	
	public void onAddFoodItemClickDialog () {		
		LayoutInflater li = LayoutInflater.from(this);
		View enterPromptsView = li.inflate(R.layout.enter_upc_prompt, null);		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		alertDialogBuilder.setView(enterPromptsView);
		
		final EditText userInput = (EditText) enterPromptsView.findViewById(R.id.editTextDialogUserInput);
		
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            
	        	String newItemText = userInput.getText().toString();
	        	try {
						postFoodItem(newItemText);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	if (newItemText.length() != 12 || newItemText.length() != 13 || newItemText.length() != 4){
	        		
	        	}
	        	else if (newItemText.matches("[0-9]+")) {
	        		
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
		
		//initFoodList();
	}	
	
	public void onAddToShoppingListClick () {
		LayoutInflater li = LayoutInflater.from(this);
		final View addToShoppingListPrompt = li.inflate(R.layout.confirm_shopping_list_add_prompt, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		String addedItemsString = "";
		
		alertDialogBuilder.setView(addToShoppingListPrompt);
		
		final TextView addedItemsText = (TextView) addToShoppingListPrompt.findViewById(R.id.addedItemsTextView);
		
		if (selectedItemList.size() == 0) {
			addedItemsString = "No Items To Add";
		}
		else {
			for (int i = 0; i < selectedItemList.size(); i++) {
				addedItemsString += selectedItemList.get(i).itemName + "\n";
			}
		}
		
		System.out.println(addedItemsString);
		
		addedItemsText.setText (addedItemsString);
		
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	postItemsToShoppingList();
	        }
	     });
		alertDialogBuilder.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
		    	selectedItemList = new ArrayList<SelectedFoodItem>();
		    	dialog.cancel();
		    }
		  });
		
		alertDialogBuilder.show();
	}
	
	public void onQueryForRecipeClick () {
		LayoutInflater li = LayoutInflater.from(this);
		final View addToShoppingListPrompt = li.inflate(R.layout.confirm_query_recipe_prompt, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		String addedItemsString = "";
		
		alertDialogBuilder.setView(addToShoppingListPrompt);
		
		final TextView addedItemsText = (TextView) addToShoppingListPrompt.findViewById(R.id.addedItemsTextView);
		
		if (selectedItemList.size() == 0) {
			addedItemsString = "No Items To Add";
		}
		else {
			for (int i = 0; i < selectedItemList.size(); i++) {
				addedItemsString += selectedItemList.get(i).itemName + "\n";
			}
		}
		
		System.out.println(addedItemsString);
		
		addedItemsText.setText (addedItemsString);
		
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	postItemsToQueryRecipe();
	        }
	     });
		alertDialogBuilder.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
		    	selectedItemList = new ArrayList<SelectedFoodItem>();
		    	dialog.cancel();
		    }
		  });
		
		alertDialogBuilder.show();
	}
	
	public void upcNotFoundDialog (String returnUPCCode) {
		LayoutInflater li = LayoutInflater.from(this);
		final View enterPromptsView = li.inflate(R.layout.enter_food_item_prompt, null);		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		alertDialogBuilder.setView(enterPromptsView);
		
		final EditText upcText = (EditText) enterPromptsView.findViewById(R.id.upcEditText);
		final EditText nameText = (EditText) enterPromptsView.findViewById(R.id.itemNameEditText);
		final EditText tagText = (EditText) enterPromptsView.findViewById(R.id.itemTagEditText);
		
		upcText.setText(returnUPCCode);
		
		Spinner spinner = (Spinner) enterPromptsView.findViewById(R.id.categorySpinner);	
		ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categoryNameList);	
		categorySpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item);	
		spinner.setAdapter(categorySpinnerAdapter);
		
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	String upc = upcText.getText().toString();
	            if (upc == "" || nameText.getText().toString() == "" || tagText.getText().toString() == "") {
	            	
	            }
	            else if (upc.matches("[0-9]+") && (upc.length() == 4 || upc.length() == 12 || upc.length() == 13))
	            {
	            	Spinner spinner = (Spinner) enterPromptsView.findViewById(R.id.categorySpinner);
	            	postManualFoodItem (upc, nameText.getText().toString(), spinner.getSelectedItem().toString(), tagText.getText().toString());
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
	}
	
	public void goToSpecificRecipe(View view) {
		Intent myIntent = new Intent(MyFood.this, SpecificRecipeActivity.class);
		startActivity(myIntent);
	}
    
	public void goToRecipe(View view) {
		Intent myIntent = new Intent(MyFood.this, RecipeActivity.class);
		startActivity(myIntent);
	}
	
	public void goToShopping(View view) {
		Intent myIntent = new Intent(MyFood.this, ShoppingList.class);
		startActivity(myIntent);
	}
    
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(MyFood.this, Login.class);
	   startActivity(setIntent);
	}
	
	public void requestAlert (String info) {
		new AlertDialog.Builder(this)
	    .setTitle("Mike...is gay")
	    .setMessage(info)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}

	@Override
	public boolean hasActions(int position) {
		Integer pos = (Integer) position;
		
		if (headerPositions.contains(pos))
			return false;

		return true;
	}

	@Override
	public void onSwipe(int[] positionList, int[] directionList) {
		
		
		FoodItem tempItem;
		int tempPosition;
		
		for(int i=0;i<positionList.length;i++) {
            int direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                	tempItem = (FoodItem) categoryViewItemList.get(position);
                	String wastedOwnershipID = tempItem.ownershipID;
                	tempPosition = 0;
                	
                	for (int j = 0; j < ownedFoodList.size(); j++) {
                		if (ownedFoodList.get(j).ownershipID.equals(wastedOwnershipID))
                			tempPosition = j;
                	}
                	
                	postItemWasted(wastedOwnershipID, tempPosition);
                    dir = "Far left";
                    break;
                    
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                    
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                	tempItem = (FoodItem) categoryViewItemList.get(position);
                	String usedOwnershipID = tempItem.ownershipID;
                	tempPosition = 0;
                	
                	for (int j = 0; j < ownedFoodList.size(); j++) {
                		if (ownedFoodList.get(j).ownershipID.equals(usedOwnershipID))
                			tempPosition = j;
                	}
                	
                	postItemUsed(usedOwnershipID, tempPosition);
                	
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Test Dialog").setMessage("GET FUCKED" + position).create().show();
                    dir = "Right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                	tempItem = (FoodItem) categoryViewItemList.get(position);
                	String tempOwnershipID = tempItem.ownershipID;
                	tempPosition = 0;
                	
                	for (int j = 0; j < ownedFoodList.size(); j++) {
                		if (ownedFoodList.get(j).ownershipID.equals(tempOwnershipID))
                			tempPosition = j;
                	}
                	
                	SelectedFoodItem selectedItem = new SelectedFoodItem (ownedFoodList.get(tempPosition).itemName, ownedFoodList.get(tempPosition).generalTag);
                	selectedItemList.add(selectedItem);
                	
                    dir = "Right";
                    break;
            }

            mAdapter.notifyDataSetChanged();
        }
		
	}

	@Override
	public boolean shouldDismiss(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}