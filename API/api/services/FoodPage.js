var Promise = require('bluebird'),
    promisify = Promise.promisify;
// function lookupUPC(options) {

// };
var foodPage = function() {};


lookupUPC = function(options) {
    var items = options.items,
        user_id = options.user_id,
        requests = 0,
        masterList = [];
    return new Promise(function(resolve, reject) {
        for (var i = 0; i < items.length; i++) {
            requests++;
            var upc = '' + items[i]["upc"];
            console.log(upc);

            var query2 = "SELECT FoodItem.foodItemID, FoodItem.upcCode, FoodItem.itemName, FoodCategory.factualCategory, FoodCategory.generalCategory, FoodCategory.expiryTime FROM FoodItem INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE CAST(upcCode as numeric(13,0)) = " + upc;
            var queryAsync = Promise.promisify(FoodItem.query);
            queryAsync(query2).then(function(fooditem) {
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
                    console.log(fooditem[0]);
                    console.log('----OWNEDFOODItem----------');
                    console.log(ownedfooditem);
                    console.log('Before' + requests);
                    requests--;
                    console.log('After' + requests);
                    masterList.push(fooditem[0]);
                    console.log('Before Function ' + requests);
                    if (requests == 0) {
                        console.log('MASTER' + masterList);
                        resolve(masterList);
                    }
                });


            });


        }

    });

};

var fP = new foodPage();
module.exports = new foodPage;
module.exports = {

    getUserItems: function(data, context) {
        var user_id = context.identity.id;
        console.log(user_id);
        query1 = "SELECT * FROM OwnedFoodItem INNER JOIN FoodItem ON OwnedFoodItem.foodItemID = FoodItem.foodItemID INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE user_id = " + user_id + " AND used = 0";
        var queryAsync = Promise.promisify(OwnedFoodItem.query);

        return queryAsync(query1).then(function(ownedfooditem) {
            console.log(ownedfooditem);
            return ownedfooditem;
        }, function(failure) {
            return {
                success: false,
                data: "Error retreiving items"
            }
        });
    },


    postListItems: function(data, context) {
        console.log(JSON.parse(data.items));
        console.log('USER');
        console.log(context.identity.id);
        var items = JSON.parse(data.items);
        var index = [];
        var masterList = [];
        var requests = 0;
        return lookupUPC({
            items: items,
            user_id: context.identity.id
        }).then(function(fooditems) {
            console.log('KKK ' + fooditems);
            return fooditems;
        });
    }
};