/**
 * FoodPageController
 *
 * @description :: Server-side logic for managing Foodpages
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    getAllItems: function(req, res) {
        API(FoodPage.getUserItems, req, res);
    },
    postListItems: function(req, res) {
        API(FoodPage.postListItems, req, res);
    },
    postItem: function(req, res) {
        API(FoodPage.postItem, req, res);
    },
    manualAddItem: function(req, res) {
        API(FoodPage.manualAddItem, req, res);
    },
    getFoodCategories: function(req, res) {
        API(FoodPage.getFoodCategories, req, res);
    },
    markUsed: function(req, res) {
        API(FoodPage.markUsed, req, res);
    },
    markWasted: function(req, res) {
        API(FoodPage.markWasted, req, res);
    }
};