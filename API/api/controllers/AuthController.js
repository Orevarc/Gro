/**
 * AuthController
 *
 * @description :: Server-side logic for managing Auths
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */
var passport = require("passport");
var jwt = require('jsonwebtoken');
var secret = 'ewfn09qu43f09qfj94qf*&H#(R';

module.exports = {

    login: function(req, res) {
        res.view();
    },
    process: function(req, res) {
        sails.log("Processsing Login...");
        passport.authenticate('local', function(err, user, info) {
            if ((err) || (!user)) {
                res.send({
                    success: false,
                    message: 'invalidPassword'
                });
                return;
            } else {
                if (err) {
                    res.send({
                        success: false,
                        message: 'unknownError',
                        error: err
                    });
                } else {
                    sails.log("Creating token...");
                    var token = jwt.sign(user, secret, {
                        expiresInMinutes: 60 * 24
                    });
                    sails.log("Sending Login Response.");
                    res.send({
                        success: true,
                        user: user,
                        token: token
                    });
                }
            }
        })(req, res);
    },

    logout: function(req, res) {
        req.logOut();
        res.send({
            success: true,
            message: 'logout successful'
        });
    }
};

module.exports.blueprints = {
    actions: true,
    rest: true,
    shortcuts: true
};