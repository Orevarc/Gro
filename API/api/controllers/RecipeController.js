/**
 * RecipeController
 *
 * @description :: Server-side logic for managing recipes
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    getAllRecipes: function(req, res) {
        API(RecipePage.getAllRecipes, req, res);
    },
    recipeByIngredient: function(req, res) {
        API(RecipePage.recipeByIngredient, req, res);
    }
};