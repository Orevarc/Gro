	USE [gro_db]
	GO

	/* OTHER */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Alcoholic Beverages', 'Other', null),
	('Baby Diapers & Diaper Care', 'Other', null),
	('Baby Food', 'Other', null),
	('Bath Products', 'Other', null),
	('Bird Food', 'Other', null),
	('Body Lotions, Oils, Creams, Sprays', 'Other', null),
	('Body Soaps & Gels', 'Other', null),
	('Cat Food', 'Other', null),
	('Cat Treats', 'Other', null),
	('Cleaning Supplies', 'Other', null),
	('Cold, Flu, Cough, Sore Throat', 'Other', null),
	('Cosmetics', 'Other', null),
	('Dog Food', 'Other', null),
	('Dog Treats', 'Other', null),
	('Eye & Eyebrow Makeup', 'Other', null),
	('Face Makeup', 'Other', null),
	('Face Treatments', 'Other', null),
	('Fragrances', 'Other', null),
	('Hair Care', 'Other', null),
	('Hair Conditioners', 'Other', null),
	('Hair Shampoo', 'Other', null),
	('Hair Styling Aids', 'Other', null),
	('Health & Medicine', 'Other', null),
	('Lip Makeup', 'Other', null),
	('Nail Makeup', 'Other', null),
	('Personal Care', 'Other', null),
	('Pet Care', 'Other', null),
	('Weight Loss Products & Supplements', 'Other', null),
	('Sexual Wellness', 'Other', null),
	('Skin Care', 'Other', null),
	('Vitamins & Supplements', 'Other', null)

	/* DRINKS */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Soda', 'Drinks', null),
	('Tea & Coffee', 'Drinks', null)

	/* DAIRY */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Dairy & Dairy-Substitute Products', 'Dairy', 21),
	('Milk & Milk Substitues', 'Dairy', 21),
	('Yogurt', 'Dairy', 14),
	('Cheeses', 'Dairy', 21)

	/* FRUITS & VEG */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Vegetables','Fruits & Vegetables', 5)

	/* CONDIMENTS, SPICES & SAUCES */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Salad Dressings', 'Condiments, Spices & Sauces', null),
	('Condiments', 'Condiments, Spices & Sauces', null),
	('Sauces', 'Condiments, Spices & Sauces', null),
	('Dips', 'Condiments, Spices & Sauces', null),
	('Extracts, Herbs & Spices', 'Condiments, Spices & Sauces', null),
	('Seasonings','Condiments, Spices & Sauces')

	/* SNACKS */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Cookies', 'Snacks', null),
	('Candy', 'Snacks', null),
	('Chocolate', 'Snacks', null),
	('Crackers', 'Snacks', null),
	('Snacks', 'Snacks', null),
	('Nuts', 'Snacks', null)

	/* GRAINS */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Noodles & Pasta', 'Grains', null),
	('Bread', 'Grains', 8)

	/* MEAT, POULTRY, SEAFOOD */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Eggs', 'Meat, Poultry & Seafood', 21),
	('Meat, Poultry, Seafood Products', 'Meat, Poultry & Seafood', 3)

	/* SOUP & CANNED FOOD */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Soups & Stocks', 'Soup & Canned Food', null),
	('Canned Food', 'Soup & Canned Food', null)

	/* MISC. */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Baking Ingredients', 'Miscellaneous', null),
	('Cooking Oils & Sprays', 'Miscellaneous', null),
	('Crusts, Shells, Stuffing', 'Miscellaneous', null),
	('Nutritional Bars, Drinks, and Shakes', 'Miscellaneous', null)

	/* DESSERTS */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Cakes', 'Desserts', null),
	('Pastries, Desserts & Pastry Products', 'Desserts', null),
	('Ice Cream & Frozen Desserts', 'Desserts', null),
	('Pudding', 'Desserts', null)

	/* Prepared Food */
	INSERT INTO [dbo].[FoodCategory] VALUES
	('Prepared Meals', 'Prepared Food', null),
	('Packaged Foods', 'Packaged Food', null)

