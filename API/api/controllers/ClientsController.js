/**
 * ClientsController
 *
 * @description :: Server-side logic for managing Clients
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    register: function(req, res) {
        API(Registration.registerClient, req, res);
    },
    'verify/:email': function(req, res) {
        console.log('ClientsController');
        API(Registration.verifyClient, req, res);
    }
};