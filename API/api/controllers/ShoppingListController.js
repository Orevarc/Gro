/**
 * ShoppingListController
 *
 * @description :: Server-side logic for managing Shoppinglists
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    addShoppingItem: function(req, res) {
        API(Shopping.addShoppingItem, req, res);
    },
    addShoppingItems: function(req, res) {
        API(Shopping.addShoppingItems, req, res);
    },
    getShoppingItems: function(req, res) {
        API(Shopping.getShoppingItems, req, res);
    },
    deleteShoppingItem: function(req, res) {
        API(Shopping.deleteShoppingItem, req, res);
    },
};