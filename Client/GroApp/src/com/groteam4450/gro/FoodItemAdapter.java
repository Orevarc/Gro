package com.groteam4450.gro;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
	public class FoodItemAdapter extends ArrayAdapter<FoodItem> {	    public FoodItemAdapter(Context context, ArrayList<FoodItem> users) {	       super(context, 0, users);	    }
	    @Override	    public View getView(int position, View convertView, ViewGroup parent) {	       // Get the data item for this position	       FoodItem food = getItem(position);    	       // Check if an existing view is being reused, otherwise inflate the view	       if (convertView == null) {	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_list_layout, parent, false);	       }	       // Lookup view for data population	       TextView foodName = (TextView) convertView.findViewById(R.id.foodName);
	       TextView foodExpiry = (TextView) convertView.findViewById(R.id.foodExpiry);	       // Populate the data into the template view using the data object	       foodName.setText(food.name);
	       foodExpiry.setText(food.expiryDate);	       // Return the completed view to render on screen	       return convertView;	   }
	}		
	

