USE [gro_db]
GO

/****** Object:  Table [dbo].[Test]    Script Date: 1/29/2015 1:15:37 PM ******/
SET ANSI_NULLS OFF
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[User](
	id int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	username nvarchar(50) NOT NULL,
	password nvarchar(512) NOT NULL,
	email nvarchar(50) NOT NULL,
	createdAt Date,
	updatedAt Date
)

CREATE TABLE [dbo].[OwnedFoodItem](
	ownershipID int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	userID int NOT NULL,
	foodItemID int NOT NULL,
	dateBought Date NOT NULL,
	expiryDate Date NOT NULL,
	amount int NOT NULL,
	used Bit
);

/*foodCategoryID will help us derive 2 nvarchar values from the FoodCategory Table, factualCategory and generalCategory*/
/*generalCategories are used to group factualCategories*/

CREATE TABLE [dbo].[FoodItem](
	foodItemID int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	upcCode nvarchar(13) NOT NULL,
	itemName nvarchar(60) NOT NULL,
	foodCategoryID int,
);

CREATE TABLE [dbo].[FavoritedRecipe](
	favoriteRecipeID int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	recipeID int NOT NULL,
	userID int NOT NULL
)

CREATE TABLE [dbo].[Recipe](
	recipeID int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	recipeName nvarchar(20),
	ingredients nvarchar(20),
	amounts nvarchar(20)
)

CREATE TABLE [dbo].[FoodCategory](
	foodCategoryID int IDENTITY (1,1) NOT NULL PRIMARY KEY,
	factualCategory nvarchar(50),
	generalCategory nvarchar(50),
	expiryTime int
)

CREATE TABLE [dbo].[Tokens](
	[access_token] [nvarchar](256) NOT NULL PRIMARY KEY,
	[refresh_token] [nvarchar](256) NOT NULL,
	[code] [nvarchar](256) NULL,
	[user_id] [int] NULL,
	[expiration_date] [datetime] NULL,
	[client_id] [nvarchar](256) NULL,
	[security_level] [nvarchar](max) NULL,
	[scope] [nvarchar](max) NULL,
)

CREATE TABLE [dbo].[Clients](
	[name] [nvarchar](50) NULL,
	[organization] [nvarchar](20) NULL,
	[email] [nvarchar](max) NULL,
	[client_id] [nvarchar](256) NOT NULL PRIMARY KEY,
	[client_secret] [nvarchar](max) NOT NULL,
	[trust_level] [nvarchar](max) NULL,
	[redirect_uri] [nvarchar](max) NULL,
	[date_registered] [nvarchar](max) NULL,
	[date_verified] [nvarchar](max) NULL,
)