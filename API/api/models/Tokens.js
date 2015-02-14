/**
 * Tokens.js
 */

var Promise = require('bluebird'),
    promisify = Promise.promisify,
    randToken = require('rand-token');


module.exports = {


    attributes: {

        access_token: {
            type: 'string',
            required: true,
            unique: true
        },

        refresh_token: {
            type: 'string',
            required: true,
            unique: true
        },

        code: {
            type: 'string',
            unique: true
        },

        user_id: {
            type: 'string'
        },

        expiration_date: {
            type: 'date'
        },

        client_id: {
            type: 'string',
            required: true
        },

        calc_expires_in: function() {
            return Math.floor(new Date(this.expiration_date).getTime() / 1000 - new Date().getTime() / 1000);
        },

        toJSON: function() {
            var hiddenProperties = ['access_token', 'refresh_token', 'code', 'user_id', 'client_id'],
                obj = this.toObject();

            obj.expires_in = this.expires_in();

            hiddenProperties.forEach(function(property) {
                delete obj[property];
            });

            return obj;
        }

    },


    authenticate: function(criteria) {
        var tokenInfo,
            $Tokens = API.Model(Tokens),
            $Users = API.Model(Users),
            $Clients = API.Model(Clients),
            $result;

        if (criteria.access_token) {
            $result = $Tokens.findOne({
                access_token: criteria.access_token
            });
        } else if (criteria.code) {
            $result = $Tokens.findOne({
                code: criteria.code
            });
        } else {
            //Bad Token Criteria
            return Promise.reject("Unauthorized");
        }

        return $result.then(function(token) {

            if (!token) return null;

            // Handle expired token
            if (token.expiration_date && new Date() > token.expiration_date) {
                return $Tokens.destroy({
                    access_token: token
                }).then(function() {
                    return null
                });
            }

            tokenInfo = token;
            if (token.user_id != null) {
                return $Users.findOne({
                    id: token.user_id
                });
            } else {
                //The request came from a client only since userID is null
                //therefore the client is passed back instead of a user
                return $Clients.findOne({
                    client_id: token.client_id
                });
            }

        }).then(function(identity) {

            // to keep this example simple, restricted scopes are not implemented,
            // and this is just for illustrative purposes


            if (!identity) return null;
            else if (criteria.type == 'verification') {
                if (identity.email != criteria.email) return null;
            }
            // Otherwise if criteria.type != 'verfication'
            else if (!identity.date_verified) return null;

            return {
                identity: identity,
                authorization: {
                    scope: tokenInfo.scope,
                    token: tokenInfo
                }
            };
        });
    },

    generateTokenString: function() {
        return randToken.generate(sails.config.security.oauth.token.length);
    },
    generateToken: function(criteria) {

        //if (err) return next(err);

        loadClient({
            client_id: criteria.client_id
        }, function(check) {
            if (check.status) {

            } else {
                loadUser({
                    user_id: criteria.user_id
                }, function(obj) {
                    if (obj.status) {
                        Clients.create({
                            client_id: criteria.client_id,
                            client_secret: Tokens.generateTokenString(),
                            email: obj.msg.email
                        }).then(function(client) {
                            console.log('CLIENT___________');
                            console.log(client);
                        });
                    } else {
                        console.log('FAILED');
                    }
                });
            }
        });

        var token = {},
            accessToken,
            $Tokens = API.Model(Tokens);

        if (!criteria.client_id) return Promise.resolve(null);

        token.client_id = criteria.client_id;
        token.user_id = criteria.user_id || undefined;


        token.access_token = accessToken = Tokens.generateTokenString();

        token.refresh_token = Tokens.generateTokenString();
        token.code = Tokens.generateTokenString();

        if (!criteria.expiration_date) {
            token.expiration_date = new Date();
            token.expiration_date.setTime(token.expiration_date.getTime() + sails.config.security.oauth.token.expiration * 1000 + 999);
        }


        return $Tokens.findOrCreate(criteria, token).then(function(retrievedToken) {
            // console.log('RETRIEVED---------');
            // console.log(retrievedToken);
            // console.log('-=-=-=-=-=-=-ACCESS-==-=-=-=-=-=');
            // console.log(accessToken);
            // console.log('CRITERA---------');
            // console.log(criteria);
            console.log('TOKEN-=-+-+-+-+-+-+--');
            console.log(token);
            if (retrievedToken.access_token != accessToken) {
                return $Tokens.update(criteria, token).then(function(updatedTokens) {
                    // console.log('-------------UPDATED---------');
                    //console.log(updatedTokens[0]);
                    return Tokens.findOne({
                        user_id: criteria.user_id
                    }).then(function(token) {
                        return token;
                    });
                    //return updatedTokens[0];
                });
            }
            return retrievedToken;
        });

    }
};

function loadUser(user_id, cb) {
    // console.log('USER_ID: ');
    // console.log(user_id.user_id);
    Users.findOne({
        id: user_id.user_id
    }).then(function(user) {
        // console.log('USER--------: ');
        // console.log(user);
        if (user) {
            cb({
                status: true,
                msg: user
            });
        }
    });
}

function loadClient(client_id, cb) {
    // console.log('CLIENT_ID: ');
    // console.log(client_id.client_id);
    Clients.findOne({
        client_id: client_id.client_id
    }).then(function(client) {
        // console.log('CLIENT--------: ');
        // console.log(client);
        if (client) {
            cb({
                status: true,
                msg: client
            });
        } else {
            cb({
                status: false
            });
        }
    });
}