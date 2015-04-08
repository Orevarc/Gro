package com.groteam4450.gro;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.groteam4450.gro.FoodItemAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;


public class FoodItem extends BasicFoodItem implements Item{
	//Name and UPC are in base class
	String ownershipID;
	Date expiryDate;
	String generalCategory;
	String factualCategory;
	String generalTag;
	int used;
	
	TextView itemNameText;
	TextView itemExpiry;
	
	public FoodItem() {
		
	}
	
	public FoodItem (String ownershipID, Date expiryDate, String upcCode, String itemName, String factualCategory, String generalCategory) {
		this.ownershipID = ownershipID;
		this.itemName = itemName;
		this.upcCode = upcCode;
		this.generalCategory = generalCategory;
		this.factualCategory = factualCategory;
		this.expiryDate = expiryDate;
	}
	
	public FoodItem (String ownershipID, Date expiryDate, String upcCode, String itemName, String factualCategory, String generalCategory, String generalTag) {
		this.ownershipID = ownershipID;
		this.itemName = itemName;
		this.upcCode = upcCode;
		this.generalCategory = generalCategory;
		this.factualCategory = factualCategory;
		this.expiryDate = expiryDate;
		this.generalTag = generalTag;
	}

	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.food_list_layout, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        itemNameText = (TextView) view.findViewById(R.id.foodName);
        itemExpiry = (TextView) view.findViewById(R.id.foodExpiry);
        
        itemNameText.setText(itemName);     
        itemExpiry.setText(parseDateToString(expiryDate));

        return view;
	}
	
    public String parseDateToString (Date date) {
    	String dateString;
    	if (date == null)
    		dateString = "No expiry";
    	else
    	{
    		DateFormat df = new SimpleDateFormat("MM/dd/yyy");
	    	
	    	dateString = df.format(date);
    	}	    	
    	return dateString;
    }

}
