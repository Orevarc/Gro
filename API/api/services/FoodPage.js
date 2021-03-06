var Promise = require('bluebird'),
    promisify = Promise.promisify;

var foodPage = function() {};


lookupUPC = function(options) {
    var items = options.items,
        user_id = options.user_id,
        requests = 0,
        masterList = [];
    return new Promise(function(resolve, reject) {
        for (var i = 0; i < items.length; i++) {
            requests++;
            var upc = '' + items[i]["upcCode"];
            //console.log("UPC " + upc);

            var query2 = "SELECT FoodItem.foodItemID, FoodItem.upcCode, FoodItem.itemName, FoodCategory.factualCategory, FoodCategory.generalCategory, FoodCategory.expiryTime FROM FoodItem INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE CAST(upcCode as numeric(13,0)) = " + upc;
            var queryAsync = Promise.promisify(FoodItem.query);
            queryAsync(query2).then(function(fooditem) {
                if (fooditem == "") {
                    //console.log('DNE');
                    requests--;
                    return;
                }
                var date = new Date();
                var expiry = new Date();
                var expiryT;
                var foodID = fooditem[0].foodItemID;
                if (fooditem[0].expiryTime != null) {
                    expiry.setDate(expiry.getDate() + fooditem[0].expiryTime);
                    expiryT = expiry.getTime();
                } else {
                    expiryT = 0;
                }
                OwnedFoodItem.create({
                    user_id: user_id,
                    foodItemID: fooditem[0].foodItemID,
                    dateBought: date,
                    expiryDate: expiryT,
                    used: 0
                }).then(function(ownedfooditem) {
                    requests--;
                    fooditem[0].ownershipID = ownedfooditem.ownershipID;
                    var item = {
                        ownershipID: 2,
                        expiryDate: expiryT,
                        itemName: fooditem[0].itemName,
                        factualCategory: ownedfooditem.factualCategory,
                        generalCategory: ownedfooditem.generalCategory,
                        generalTag: fooditem[0].generalTag
                    };
                    masterList.push(item);
                    if (requests == 0) {
                        resolve({
                            success: true,
                            items: masterList
                        });
                    }
                }).catch(function(e) {
                    resolve({
                        success: false,
                        message: "Error adding item to User's list",
                        error: e
                    });
                });


            }).catch(function(e) {
                resolve({
                    success: false,
                    message: "Error finding item with upc",
                    error: e
                });
            });


        }

    });

};

addOwnedItem = function(options) {
    var fooditem = options.fooditem[0],
        user_id = options.user_id;
    var date = new Date();
    var expiry = new Date();
    var expiryT;
    //console.log('FoodI ' + JSON.stringify(fooditem));
    if (fooditem.expiryTime != null) {
        expiry.setDate(expiry.getDate() + fooditem.expiryTime);
        console.log("Ecpiry " + expiry.getTime());
        expiryT = expiry.getTime();
        console.log("Time " + expiryT);
    } else {

        expiryT = 0;
    }
    return OwnedFoodItem.create({
        user_id: user_id,
        foodItemID: fooditem.foodItemID,
        dateBought: date,
        expiryDate: expiryT,
        used: 0
    }).then(function(ownedfooditem) {
        ownedfooditem.ownershipID = ownedfooditem.id;
        return {
            success: true,
            ownershipID: ownedfooditem.ownershipID,
            itemName: fooditem.itemName,
            upcCode: fooditem.upcCode,
            expiryDate: expiryT,
            factualCategory: fooditem.factualCategory,
            generalCategory: fooditem.generalCategory
        };
    });

};

addFoodItem = function(options) {
    var queryCategory = "SELECT foodCategoryID, expiryTime FROM FoodCategory WHERE factualCategory = '" + options.factualCategory + "'";
    var queryAsync = Promise.promisify(FoodCategory.query);
    return queryAsync(queryCategory).then(function(category) {
        cat = category[0];
        return FoodItem.create({
            upcCode: options.upcCode,
            itemName: options.itemName,
            foodCategoryID: cat.foodCategoryID,
            generalTag: options.generalTag
        }).then(function(fooditem) {
            console.log(JSON.stringify(fooditem));
            var date = new Date();
            var expiry = new Date();
            var expiryT;
            if (cat.expiryTime != null) {
                expiry.setDate(expiry.getDate() + cat.expiryTime);
                expiryT = expiry.getTime();
            } else {
                expiryT = 0;
            }
            return OwnedFoodItem.create({
                user_id: options.user_id,
                foodItemID: fooditem.id,
                dateBought: date,
                expiryDate: expiryT,
                used: 0
            }).then(function(ownedfooditem) {
                ownedfooditem.ownershipID = ownedfooditem.id;
                return {
                    success: true,
                    ownershipID: ownedfooditem.ownershipID,
                    itemName: fooditem.itemName,
                    upcCode: fooditem.upcCode,
                    expiryDate: expiryT,
                    factualCategory: fooditem.factualCategory,
                    generalCategory: fooditem.generalCategory
                };
            });
        });

    });

};

module.exports = {

    getFoodCategories: function(data, context) {
        queryCategory = "SELECT foodCategoryID, factualCategory FROM FoodCategory ORDER BY factualCategory ASC";
        var queryAsync = Promise.promisify(FoodCategory.query);
        return queryAsync(queryCategory).then(function(foodcategories) {
            //console.log('Food Categories: ' + foodcategories);
            return {
                success: true,
                foodcategories: foodcategories
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could not retrieve Food Categories",
                error: e
            };
        });

    },

    getUserItems: function(data, context) {
        var user_id = context.identity.id;
        console.log(user_id);
        query1 = "SELECT * FROM OwnedFoodItem INNER JOIN FoodItem ON OwnedFoodItem.foodItemID = FoodItem.foodItemID INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE user_id = " + user_id + " AND used = 0";
        var queryAsync = Promise.promisify(OwnedFoodItem.query);

        return queryAsync(query1).then(function(ownedfooditem) {
            console.log("TEST" + ownedfooditem);
            return {
                success: true,
                items: ownedfooditem
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could not retireve user's items",
                error: e
            };
        });
    },


    postListItems: function(data, context) {
        var items = JSON.parse(data.items);
        return lookupUPC({
            items: items,
            user_id: context.identity.id
        }).then(function(fooditems) {
            console.log('KKK ' + fooditems);
            return fooditems;
        });
    },

    postItem: function(data, context) {
        console.log("YOU GOT HERE!");
        var queryItem = "SELECT FoodItem.foodItemID, FoodItem.upcCode, FoodItem.itemName, FoodCategory.factualCategory, FoodCategory.generalCategory, FoodCategory.expiryTime FROM FoodItem INNER JOIN FoodCategory ON FoodItem.foodCategoryID = FoodCategory.foodCategoryID WHERE CAST(upcCode as numeric(13,0)) = " + data.upc;
        var queryAsync = Promise.promisify(FoodItem.query);
        return queryAsync(queryItem).then(function(fooditem) {
            if (fooditem == "") {
                return {
                    success: false,
                    upc: data.upc
                };
            }
            return addOwnedItem({
                user_id: context.identity.id,
                fooditem: fooditem
            }).then(function(ownedfooditem) {
                return ownedfooditem;
            });
        });
    },
    manualAddItem: function(data, context) {
        var user_id = context.identity.id;
        console.log('GUHS ' + JSON.stringify(data));
        return addFoodItem({
            user_id: user_id,
            upcCode: data.upcCode,
            itemName: data.itemName,
            factualCategory: data.factualCategory,
            generalTag: data.generalTag
        });

    },
    markUsed: function(data, context) {
        var queryUsed = "UPDATE OwnedFoodItem SET used = 1 WHERE ownershipID = " + data.ownershipID;
        var queryAsync = Promise.promisify(OwnedFoodItem.query);
        return queryAsync(queryUsed).then(function(used) {
            return {
                success: true
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could not mark used",
                error: e
            };
        });
    },
    markWasted: function(data, context) {
        var queryWasted = "UPDATE OwnedFoodItem SET used = 2 WHERE ownershipID = " + data.ownershipID;
        var queryAsync = Promise.promisify(OwnedFoodItem.query);
        return queryAsync(queryWasted).then(function(wasted) {
            return {
                success: true
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could not mark wasted",
                error: e
            };
        });
    },
    deleteItem: function(data, context) {
        var items = JSON.parse(data.items);
        var queryDelete = "DELETE FROM OwnedFoodItem WHERE ownershipID = " + data.ownershipID;
        var queryAsync = Promise.promisify(OwnedFoodItem.query);
        return queryAsync(queryDelete).then(function(deleted) {
            return {
                success: true
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could delete item",
                error: e
            };
        });
    }
};