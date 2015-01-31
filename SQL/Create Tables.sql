USE [gro_db]
GO

/****** Object:  Table [dbo].[Test]    Script Date: 1/29/2015 1:15:37 PM ******/
SET ANSI_NULLS OFF
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE TABLE [dbo].[User](
	userID int NOT NULL PRIMARY KEY,
	username varchar(20) NOT NULL,
	pass varchar(20) NOT NULL,
	email varchar(30) NOT NULL,
	token varchar(20) NOT NULL,
	createdAt Date NOT NULL,
	updatedAt Date NOT NULL
)

CREATE TABLE [dbo].[OwnedFoodItem](
	ownershipID int NOT NULL PRIMARY KEY,
	userID int NOT NULL,
	foodItemID int NOT NULL,
	dateBought Date NOT NULL,
	expiryDate Date NOT NULL,
	used Bit
);

CREATE TABLE [dbo].[FoodItem](
	foodItemID int NOT NULL PRIMARY KEY,
	upcCode varchar(15),
	itemName varchar(60) NOT NULL,
	brand varchar(50) NOT NULL,
	size varchar(25),
	foodCategory varchar(30),
	expectedExpiryDate int,
	imageURL varchar(50)
);

CREATE TABLE [dbo].[FavoritedRecipe](
	favoriteRecipeID int NOT NULL PRIMARY KEY,
	recipeID int NOT NULL,
	userID int NOT NULL
)

CREATE TABLE [dbo].[Recipe](
	recipeID int NOT NULL PRIMARY KEY,
	recipeName varchar(20),
	ingredients varchar(20),
	amounts varchar(20)
)
