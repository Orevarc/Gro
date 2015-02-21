var Promise = require('bluebird'),
    promisify = Promise.promisify;

lookupUPC = function(options) {
    var user_id = options.user_id;
    var upc = options.upc;
    var upcString = '' + upc
    var query2 = "SELECT FoodItem.foodItemID, FoodItem.upcCode, FoodItem.itemName, FoodCategory.factualCategory, FoodCategory.generalCategory, FoodCategory.expiryTime FROM FoodItem INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE CAST(upcCode as numeric(13,0)) = " + upcString;
    var queryAsync = Promise.promisify(FoodItem.query);
    return queryAsync(query2).then(function(fooditem) {
        //var item = JSON.parse(fooditem);
        var date = new Date();
        var expiry = new Date();
        var foodID = fooditem[0].foodItemID;
        console.log('----FoodItem1' + fooditem[0].foodItemID);
        console.log(fooditem);

        if (fooditem[0].expiryTime != null) {
            expiry.setDate(expiry.getDate() + fooditem[0].expiryTime);
        } else {
            console.log('NULL EXPIRY');
            expiry = null;
        }
        OwnedFoodItem.create({
            user_id: user_id,
            foodItemID: fooditem[0].foodItemID,
            dateBought: date,
            expiryDate: expiry,
            used: 0
        }).then(function(ownedfooditem) {
            console.log('----FoodItem2');
            console.log(fooditem);
            console.log('----OWNEDFOODItem----------');
            console.log(ownedfooditem);
            return fooditem[0];
        });

    });
};

module.exports = {
    getUserItems: function(data, context) {
        var user_id = context.identity.id;
        console.log(user_id);
        query1 = "SELECT * FROM OwnedFoodItem INNER JOIN FoodItem ON OwnedFoodItem.foodItemID = FoodItem.foodItemID INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE user_id = " + user_id + " AND used = 0";
        var queryAsync = Promise.promisify(OwnedFoodItem.query);

        return queryAsync(query1).then(function(ownedfooditem) {
            console.log(ownedfooditem);
            return ownedfooditem;
        });
    },

    postListItems: function(data, context) {
        console.log(JSON.parse(data.items));
        console.log('USER');
        console.log(context.identity.id);
        var items = JSON.parse(data.items);
        var index = [];
        var masterList;
        for (var i = 0; i < items.length; i++) {
            var upc = items[i]["upc"];
            console.log(upc);
            var test = lookupUPC({
                upc: upc,
                user_id: context.identity.id
            });
            console.log("TESTING");
            console.log(test);

        }
    }
}