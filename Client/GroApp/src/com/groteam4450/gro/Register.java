package com.groteam4450.gro;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

public class Register extends ActionBarActivity {

	AsyncHttpClient client = new AsyncHttpClient();

	HTTPRestClient httpClient = new HTTPRestClient();
	HttpResponseParser httpParser = new HttpResponseParser();
	EditText username;
	EditText password;
	EditText email;
	String loginResponse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
	
	public void onRegisterClick(View view) throws Exception {
		String url = "users/register";
		
		RequestParams params = new RequestParams();
		params.put("username", username.getText().toString());
		params.put("password", password.getText().toString());
		params.put("email", email.getText().toString());
		
		httpClient.post(url, params, new AsyncHttpResponseHandler() {
			 @Override
	            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				 	Intent myIntent = new Intent(Register.this, Login.class);
					startActivity(myIntent);
	            }
	            
	            @Override
	            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable e) {
	            	badRegisterAlert(); 	
	            }
	        });	
	}
	
	
	public void badRegisterAlert () {
		new AlertDialog.Builder(this)
	    .setTitle("Registration Unsuccessful")
	    .setMessage("Please check your information and try again")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
}