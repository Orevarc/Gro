package com.groteam4450.gro;

import java.util.ArrayList;

public class RecipeItem {
	String recipeName;
	int totalTimeInSeconds;
	ArrayList<String> ingredients;
	int rating;
	
	public RecipeItem() {
		
	}
	
	public RecipeItem (String recipeName, int totalTimeInSeconds, ArrayList<String> ingredients, int rating) {
		this.recipeName = recipeName;
		this.totalTimeInSeconds = totalTimeInSeconds;
		this.ingredients = ingredients;
		this.rating = rating;
	}
}
