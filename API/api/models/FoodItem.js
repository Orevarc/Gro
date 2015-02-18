/**
 * FoodItem.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/#!documentation/models
 */

module.exports = {

    attributes: {
        foodItemID: {

        },
        upcCode: {
            type: 'string',
            required: true
        },
        itemName: {
            type: 'string',
            required: true
        },
        foodCategoryID: {
            type: 'string',
        },
        toJSON: function() {

            var obj = this.toObject();

            return obj;
        }
    }
};