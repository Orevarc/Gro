var Promise = require('bluebird'),
    promisify = Promise.promisify;

module.exports = {
    getUserItems: function(data, context) {
        var user_id = context.identity.id;
        console.log(user_id);
        var items = OwnedFoodItem.find({
            userid: user_id,
            used: 0
        });
        console.log(items);
        return items;
    }
}