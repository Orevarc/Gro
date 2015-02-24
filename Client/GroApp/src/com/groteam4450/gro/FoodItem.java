package com.groteam4450.gro;


import java.util.Date;


public class FoodItem extends BasicFoodItem{
	//Name and UPC are in base class
	String ownershipID;
	String expiryDate;
	String generalCategory;
	String factualCategory;
	Boolean used;
	
	
	public FoodItem() {
		
	}
	
	public FoodItem (String ownershipID, String expiryDate, String upcCode, String itemName, String factualCategory, String generalCategory) {
		this.ownershipID = ownershipID;
		this.itemName = itemName;
		this.upcCode = upcCode;
		this.generalCategory = generalCategory;
		this.factualCategory = factualCategory;
		this.expiryDate = expiryDate;
	}

}
