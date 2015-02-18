/**
 * FoodPageController
 *
 * @description :: Server-side logic for managing Foodpages
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    getAllItems: function(req, res) {
        API(FoodPage.getUserItems, req, res);
    }
};