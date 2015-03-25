var Promise = require('bluebird'),
    promisify = Promise.promisify;

addAllItems = function(options) {
    var items = options.items,
        user_id = options.user_id;
    return new Promise(function(resolve, reject) {
        for (var i = 0; i < items.length; i++) {
            requests++;
            var item = '' + items[i]["upc"];
            console.log("UPC " + upc);
            ShoppingList.create({
                user_id: user_id,
                itemName: item
            });
        }
    });

};

module.exports = {

    addShoppingItem: function(data, context) {
        //console.log("DATA " + JSON.stringify(data));
        //var item = JSON.parse(data.item);
        return API.Model(ShoppingList).create({
            user_id: context.identity.id,
            itemName: data.item
        }).then(function(shoppingitem) {
            return {
                success: true,
                id: shoppingitem.id,
                itemName: shoppingitem.itemName
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Unable to add item to Shopping List",
                error: e
            };
        });

    },

    addShoppingItems: function(data, context) {
        JSON.stringify(data);
        var items = JSON.parse(data.items);
        return getAllItems({
            items: items,
            user_id: context.identity.id
        }).then(function(fooditems) {
            console.log('KKK ' + fooditems);
            return fooditems;
        });

    },

    deleteShoppingItem: function(data, context) {
        return API.Model(ShoppingList).destroy({
            id: data.id
        }).then(function(success) {
            return {
                success: true
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Unable to delete item to Shopping List",
                error: e
            };
        });

    },
    getShoppingItems: function(data, context) {
        query1 = "SELECT * FROM ShoppingList WHERE user_id = " + context.identity.id;
        var queryAsync = Promise.promisify(ShoppingList.query)
        return queryAsync(query1).then(function(shoppinglist) {
            console.log("ShoppingList" + shoppinglist);
            return {
                success: true,
                items: shoppinglist
            };
        }).catch(function(e) {
            return {
                success: false,
                message: "Could not retireve user's Shopping List",
                error: e
            };
        });

    },

};