/**
 * OwnedFoodItem.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/#!documentation/models
 */

module.exports = {

    attributes: {
        user_id: {
            model: 'users'
        },

        foodItemID: {
            model: 'fooditem'
        },

        dateBought: {
            type: 'date',
        },

        expiryDate: {
            type: 'date'
        },

        used: {
            type: 'int'
        },

        toJSON: function() {

            var obj = this.toObject();

            return obj;
        }

    },

    beforeCreate: function(ownedfooditem, next) {
        ownedfooditem.dateBought = new Date();
        ownedfooditem.used = 0;
        next();
    }
};