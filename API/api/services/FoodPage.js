var Promise = require('bluebird'),
    promisify = Promise.promisify;

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
        // return OwnedFoodItem.find({
        //     user_id: user_id,
        //     used: 0
        // }).populate('user_id');
        // console.log(items);
        // return items;
    }
}