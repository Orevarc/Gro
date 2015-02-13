/**
 * OAuthController
 *
 * @description :: Server-side logic for managing Oauths
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    token: function(req, res) {
        API(OAuth.sendToken, req, res);
    },

    'token-info': function(req, res) {
        API(OAuth.tokenInfo, req, res);
    }

};