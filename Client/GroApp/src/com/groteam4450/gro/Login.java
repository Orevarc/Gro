package com.groteam4450.gro;

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

	HttpClient httpClient = new HttpClient();
	HttpResponseParser httpParser = new HttpResponseParser();
	EditText username;
	EditText password;
	String loginResponse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
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
	
	//Method used to verify login. The onclick sends a post request to the server using HttpClient.java 
	//and then parses the response for a successful login
	public void onLoginClick() throws Exception {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		loginResponse = httpClient.loginSendPost("VCuiPfDx0OjgJFMQZF5m3se78Mu0TZMh", username.getText().toString(), password.getText().toString());
		
		if (httpParser.parseLoginResponse(loginResponse)) 
		{
			Intent myIntent = new Intent(Login.this, PhotoCaptureExample.class);
			startActivity(myIntent);
		}
		else
		{
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
