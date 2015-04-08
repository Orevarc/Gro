package com.groteam4450.gro;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShoppingListItemAdapter extends ArrayAdapter<ShoppingListItem>{
    public ShoppingListItemAdapter(Context context, ArrayList<ShoppingListItem> users) {
	       super(context, 0, users);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
	       ShoppingListItem item = getItem(position);    
	       
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_layout, parent, false);
	       }
	       
	       // Lookup view for data population
	       TextView itemName = (TextView) convertView.findViewById(R.id.itemName);

	       itemName.setText(item.itemName);
	       
	       return convertView;
	   }
}
