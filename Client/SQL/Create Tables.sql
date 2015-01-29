USE [gro_db]
GO

/****** Object:  Table [dbo].[Test]    Script Date: 1/29/2015 1:15:37 PM ******/
SET ANSI_NULLS OFF
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[User](
	UserID int NOT NULL PRIMARY KEY,
	username varchar(20),
	token varchar(20),
	createdAt DateTime,
	updatedAt DateTime
)

CREATE TABLE [dbo].[OwnedFoodItem](
	OwnershipID int,
	UserID int,
	FoodItemID int,
	DateBought varchar(25),
	ExpiryDate DateTime,
	Used Bit
);

CREATE TABLE [dbo].[FoodItem](
	FoodItemID int NOT NULL PRIMARY KEY,
	UPCCode int,
	ItemName varchar(25),
	Details varchar(20),
	FoodCategory int,
	ExpectedExpiryDate int
);

CREATE TABLE [dbo].[FoodCategory](
	FoodCategoryID int,
	FoodCategoryName varchar(20)
);

CREATE TABLE [dbo].[FavoritedRecipe](
	RecipeID int,
	UserID int
)

CREATE TABLE [dbo].[Recipe](
	RecipeID int,
	RecipeName varchar(20),
	Ingredients varchar(20),
	Amounts varchar(20)
)

