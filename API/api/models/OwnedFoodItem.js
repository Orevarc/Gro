/**
 * OwnedFoodItem.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/#!documentation/models
 */

module.exports = {

    attributes: {
        user_id: {
            type: 'int',
            required: true
        },

        foodItemId: {
            type: 'int',
            required: true
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
            delete obj.password;

            return obj;
        }

    },

    beforeCreate: function(ownedfooditem, next) {
        ownedfooditem.dateBought = new Date();
        ownedfooditem.used = 0;
        next();
    }
};