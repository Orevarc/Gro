package com.groteam4450.gro;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


	       
	       
	       TextView foodExpiry = (TextView) convertView.findViewById(R.id.foodExpiry);
	      // foodName.setTypeface("");
	       foodExpiry.setText(food.expiryDate);
	}
	
