package com.groteam4450.gro;

import java.net.URL;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Login extends ActionBarActivity {
	
	AsyncHttpClient client = new AsyncHttpClient();
	

	HTTPRestClient httpClient = new HTTPRestClient();
	HttpResponseParser httpParser = new HttpResponseParser();
	EditText username;
	EditText password;
	String loginResponse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToRegister(View view) {
		Intent myIntent = new Intent(Login.this, Register.class);
		startActivity(myIntent);
	}
	
	//Method used to verify login. The onclick sends a post request to the server using HttpClient.java 
	//and then parses the response for a successful login
	public void onLoginClick(View view) throws Exception {
		String url = "oauth/token";
		RequestParams params = new RequestParams();
		params.put("client_id", "VCuiPfDx0OjgJFMQZF5m3se78Mu0TZMh");
		params.put("grant_type", "password");
		params.put("username", username.getText().toString());
		params.put("password", password.getText().toString());
		
		httpClient.post(url, params, new AsyncHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				 	Intent myIntent = new Intent(Login.this, PhotoCaptureExample.class);
					startActivity(myIntent);
	            }
	            
	            @Override
	            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable e) {
	            	badLoginAlert();
	            	System.out.println(e.getMessage());
	            }
	        });	
	}
	
	public void badLoginAlert () {
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
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		 intent.addCategory(Intent.CATEGORY_HOME);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
         startActivity(intent);
         finish();
         System.exit(0);
	}
}
