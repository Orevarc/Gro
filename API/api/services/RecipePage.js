var Promise = require('bluebird'),
    promisify = Promise.promisify;
var rp = require('request-promise');

var appId = sails.config.yummly.appId;
var apiKey = sails.config.yummly.apiKey;
var yummlyEndpoint = "http://api.yummly.com/v1/api/recipes?_app_id=" + appId + "&_app_key=" + apiKey;
var addIngredient = "&allowedIngredient[]=";


lookupRecipes = function(options) {
    var ingredients = options.ingredients,
        user_id = options.user_id,
        requests = ingredients.length - 1,
        masterList = [];
    return new Promise(function(resolve, reject) {
        for (var i = 0; i < ingredients.length; i++) {
            //console.log('ING' + JSON.stringify(ingredients));
            var ingredient = ingredients[i]["generalTag"]; //["ingredient"];
            console.log("Ingredient " + ingredient);
            if (ingredient != null) {
                yummlyEndpoint += addIngredient;
                yummlyEndpoint += ingredient;
            }
            console.log('Yummly Endpoint ' + yummlyEndpoint);
        }
        var yummlyReq = {
            uri: yummlyEndpoint,
            method: 'GET'
        };
        rp(yummlyReq).then(function(recipes) {
            console.log('Yum Recipes: ' + recipes);
            var recipeList = JSON.parse(recipes);
            var matches = recipeList.matches;
            resolve({
                matches: matches,
            });
        }).catch(function(e) {
            console.log('ERROR RECIPES');
            resolve({
                success: false,
            });
        });
    });
};

lookupRecipe = function(options) {
    var ingredients = options.ingredients,
        user_id = options.user_id,
        requests = 0,
        masterList = [];
    return new Promise(function(resolve, reject) {


        var ingredient = '' + ingredients; //["ingredient"];
        console.log("Ingredient " + ingredient);
        yummlyEndpoint = yummlyEndpoint + addIngredient + ingredient;

        var yummlyReq = {
            uri: yummlyEndpoint,
            method: 'GET'
        };
        return rp(yummlyReq).then(function(recipes) {
            console.log('Yum Recipes: ' + recipes);
            var recipeList = JSON.parse(recipes);
            resolve({
                recipe: recipeList
            });
        }).catch(function(e) {
            console.log('ERROR RECIPES');
            resolve({
                recipe: recipes
            });
        });
    });
};

module.exports = {

    recipeByIngredients: function(data, context) {
        // console.log("DATA " + JSON.stringify(data));
        // var ingredients = JSON.parse(data.items);
        console.log('Hit Recipes');
        var ingredients = JSON.parse(data.items);
        return lookupRecipes({
            ingredients: ingredients,
            user_id: context.identity.id
        }).then(function(recipes) {
            //console.log('KKK ' + JSON.stringify(recipes));

            return {
                matches: recipes.matches
            };
        });
    },
};