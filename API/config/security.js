module.exports.security = {
    oauth: {
        version: '2.0',
        token: {
            length: 32,
            expiration: 100000
        }
    },
    admin: {
        email: {
            address: 'groappinfo@gmail.com',
            password: 'tottenham12'
        },

    },
    server: {
        url: 'http://groappvm.cloudapp.net:1336'
    }
};