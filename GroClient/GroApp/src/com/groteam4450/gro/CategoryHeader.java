package com.groteam4450.gro;

import com.groteam4450.gro.FoodItemAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CategoryHeader implements Item {

	public final String name;
	
	public CategoryHeader (String name) {
		this.name = name;
	}
	
	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() == obj.getClass())
			return false;
		
		final CategoryHeader other = (CategoryHeader) obj;
		
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) 
			return false;
		
		if (this.name != other.name)
			return false;
		
		return true;
	}
	

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.food_category_layout, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.categoryHeader);
        text.setText(name);

        return view;
	}
	

}
