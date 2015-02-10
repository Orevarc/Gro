package com.groteam4450.gro;


import java.util.Date;


public class FoodItem extends BasicFoodItem{
	//Name and UPC are in base class
	String category;
	Date expiryDate;
	Boolean used;
	
	public FoodItem() {
		
	}
	
	public FoodItem (String name, String upcCode, String category, Date expiryDate, Boolean used) {
		this.name = name;
		this.upcCode = upcCode;
		this.category = category;
		this.expiryDate = expiryDate;
		this.used = used;
	}

}
