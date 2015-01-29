/**
* FoodItem.js
*
* @description :: TODO: You might write a short summary of how this model works and what it represents here.
* @docs        :: http://sailsjs.org/#!documentation/models
*/

module.exports = {

  attributes: {
  	UPCCode: {
  		type: 'int',
  	},
  	name: {
  		type: 'string',
  	},
  	brand: {
  		type: 'string',
  	},
  	category: {
  		type: 'string',
  	},
  	expectedExpiry: {
  		type: 'int',
  	}

  }
};

