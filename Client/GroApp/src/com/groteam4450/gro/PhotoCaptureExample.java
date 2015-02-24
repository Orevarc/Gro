package com.groteam4450.gro;


import android.app.Activity;
import android.app.AlertDialog;
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
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhotoCaptureExample extends Activity {
	
	AsyncHttpClient client = new AsyncHttpClient();

	HTTPRestClient httpClient = new HTTPRestClient();
	
	protected ImageButton _button;
	//protected ImageView _image;
	protected String _path;
	protected boolean _taken;
	protected Bitmap bitmap;
	protected ExifInterface exif = null;
	protected ArrayList<FoodItem> ownedFoodList;
	protected ListView foodListView;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);
       
        _button = ( ImageButton ) findViewById( R.id.button );
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _path = "/storage/sdcard0/images/make_machine_example.jpg";
        
        foodListView = (ListView) findViewById(R.id.listView1);
        
        ownedFoodList = new ArrayList<FoodItem>();
        
        FoodItem foodA = new FoodItem("Egg", "12345678910", "Meat, Poultry, Seafood", "11/02/2015", false);
        FoodItem foodB = new FoodItem("Riceroni", "12345678910", "Grains", "10/02/2014", false);
        FoodItem foodC = new FoodItem("Haggis", "12345678910", "Meat, Poultry, Seafood", "11/02/2015", false);
        FoodItem foodD = new FoodItem("Corn", "12345678910", "Meat, Poultry, Seafood", "11/02/2017", false);
        FoodItem foodE = new FoodItem("Peas", "12345678910", "Meat, Poultry, Seafood", "11/02/2016", false);
        FoodItem foodF = new FoodItem("Corrosive acid", "12345678910", "Meat, Poultry, Seafood", "13/02/2015", false);
        FoodItem foodG = new FoodItem("Waffles", "12345678910", "Meat, Poultry, Seafood", "10/02/2015", false);
        FoodItem foodH = new FoodItem("Burgers", "12345678910", "Meat, Poultry, Seafood", "11/02/2015", false);
        FoodItem foodI = new FoodItem("Fuzzy Peaches", "12345678910", "Meat, Poultry, Seafood", "31/04/2015", false);
        FoodItem foodJ = new FoodItem("Human meat", "12345678910", "Meat, Poultry, Seafood", "11/02/2098", false);
        FoodItem foodK = new FoodItem("Yams", "12345678910", "Meat, Poultry, Seafood", "11/02/2015", false);
        FoodItem foodL = new FoodItem("Salt", "12345678910", "Meat, Poultry, Seafood", "11/02/2015", false);
        ownedFoodList.add(foodA);
        ownedFoodList.add(foodB);
        ownedFoodList.add(foodC);
        ownedFoodList.add(foodD);
        ownedFoodList.add(foodE);
        ownedFoodList.add(foodF);
        ownedFoodList.add(foodG);
        ownedFoodList.add(foodH);
        ownedFoodList.add(foodI);
        ownedFoodList.add(foodJ);
        ownedFoodList.add(foodK);
        ownedFoodList.add(foodL);
        FoodItemAdapter arrayAdapter = new FoodItemAdapter(this, ownedFoodList);
        
        foodListView.setAdapter(arrayAdapter);
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
    	if( savedInstanceState.getBoolean( PhotoCaptureExample.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( PhotoCaptureExample.PHOTO_TAKEN, _taken );
    }
    
	public void OCR(View view) {
		onPhotoTaken();
	}
    
    public void performOCR()
    {
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
    		File file = new File("/storage/sdcard0/InitialOCR/OCRTextCaptured.txt");
			PrintWriter out = new PrintWriter(file);
			out.println(recognizedText);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
	public void onRequestClick(View view) throws Exception {
		String url = "oauth/token";
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String authString = settings.getString ("token", "");
		
		Header[] headers = {
				new BasicHeader("Authorization","Bearer " + authString)
		};
		
		httpClient.getAuth(url, headers, null, new AsyncHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				 	requestAlert(response.toString());
	            }
	            
	            @Override
	            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable e) {
	            	
	            }
	        });	
	}
	
	public void requestAlert (String info) {
		new AlertDialog.Builder(this)
	    .setTitle("Login Unsuccessful")
	    .setMessage("Please check your information and try again")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
    
	public void goToRecipe(View view) {
		Intent myIntent = new Intent(PhotoCaptureExample.this, Recipe.class);
		startActivity(myIntent);
	}
	
	public void goToShopping(View view) {
		Intent myIntent = new Intent(PhotoCaptureExample.this, ShoppingList.class);
		startActivity(myIntent);
	}
    
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(PhotoCaptureExample.this, Login.class);
	   startActivity(setIntent);
	}
	
}