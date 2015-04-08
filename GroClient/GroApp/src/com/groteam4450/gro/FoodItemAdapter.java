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

		private SparseBooleanArray mSelectedItemsIds;
		LayoutInflater inflater;
		Context context;

		public enum RowType {
			LIST_ITEM, HEADER_ITEM
		}
			
	       inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
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

//	       
//	       /*if (selectedItems.get(position))
//	       {
//	    	   convertView.setBackgroundColor(Color.GREEN);
//	       }
//	       else
//	    	   convertView.setBackgroundColor(Color.BLUE);*/
//	       TextView foodExpiry = (TextView) convertView.findViewById(R.id.foodExpiry);
//	      // foodName.setTypeface("");
//	       foodExpiry.setText(parseDateToString(food.expiryDate));
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
	
