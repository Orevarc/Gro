package com.groteam4450.gro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
	public class FoodItemAdapter extends ArrayAdapter<Item> {
		private SparseBooleanArray mSelectedItemsIds;
		LayoutInflater inflater;
		Context context;

		public enum RowType {
			LIST_ITEM, HEADER_ITEM
		}
				    public FoodItemAdapter(Context context, int resourceId, ArrayList<Item> users) {	       super(context, resourceId, users);
	       inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	    }
	    
	    @Override
	    public int getViewTypeCount() {
	
	        return RowType.values().length;

	    }

	    @Override
	    public int getItemViewType(int position) {
	        return getItem(position).getViewType();
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	
	        return getItem(position).getView(inflater, convertView);
	    }
//	    @Override//	    public View getView(int position, View convertView, ViewGroup parent) {//	       // Get the data item for this position//	       Item food = getItem(position);    
//	       //	       // Check if an existing view is being reused, otherwise inflate the view//	       if (convertView == null) {//	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_list_layout, parent, false);//	       }
//	       /*if (selectedItems.get(position))
//	       {
//	    	   convertView.setBackgroundColor(Color.GREEN);
//	       }
//	       else
//	    	   convertView.setBackgroundColor(Color.BLUE);*///	       // Lookup view for data population//	       TextView foodName = (TextView) convertView.findViewById(R.id.foodName);
//	       TextView foodExpiry = (TextView) convertView.findViewById(R.id.foodExpiry);//	       // Populate the data into the template view using the data object//	       foodName.setText(food.itemName);
//	      // foodName.setTypeface("");
//	       foodExpiry.setText(parseDateToString(food.expiryDate));//	       // Return the completed view to render on screen//	       return convertView;//	   }
//	    
//	    public String parseDateToString (Date date) {
//	    	String dateString;
//	    	if (date == null)
//	    		dateString = "No expiry";
//	    	else
//	    	{
//	    		DateFormat df = new SimpleDateFormat("MM/dd/yyy");
//		    	
//		    	dateString = df.format(date);
//	    	}	    	
//	    	return dateString;
//	    }
//	    
//	    
//	    public void toggleSelection(int position) {
//	    	selectView(position, !mSelectedItemsIds.get(position));
//	    }
//	    
//	    public void removeSelection() {
//	    	mSelectedItemsIds = new SparseBooleanArray();
//	    	notifyDataSetChanged();
//	    }
//	    
//	    public SparseBooleanArray getSelectedIds() {
//	    	return mSelectedItemsIds;
//	    }
//	    
//	    public int getSelectedCount() {
//	    	return mSelectedItemsIds.size();
//	    }
//	    
//	    public void selectView(int position, boolean value) {
//	    	if(value)
//	    		mSelectedItemsIds.put(position, value);
//	    	else
//	    		mSelectedItemsIds.delete(position);
//	    	notifyDataSetChanged();
//	    }
	}		
	

