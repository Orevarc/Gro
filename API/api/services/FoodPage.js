var Promise = require('bluebird'),
    promisify = Promise.promisify;

lookupUPC = function(options) {};

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
        var items = JSON.parse(data.items);
        var index = [];

        for (var x in items) {
            index.push(x);
            console.log(x);
        }
        var masterList;
        for (var i = 0; i < items.length; i++) {
            var upc = items[i]["upc"];
            console.log(upc);
        }
    }
}