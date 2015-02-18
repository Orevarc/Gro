/**
 * FoodCategory.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/#!documentation/models
 */

module.exports = {

    attributes: {
        factualCategory: {
            type: 'string',
        },
        generalCategory: {
            type: 'string',
        },
        expiryTime: {
            type: 'int',
        },
        toJSON: function() {

            var obj = this.toObject();
            return obj;
        }

    }
};