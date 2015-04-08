package com.groteam4450.gro;

public class FoodCategory {
	//Name and UPC are in base class
	int categoryID;
	String categoryName;
	
	public FoodCategory () {
		
	}
	
	public FoodCategory (int categoryID, String categoryName) {
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}

}
