module.exports.security = {
    oauth: {
        version: '2.0',
        token: {
            length: 32,
            expiration: 10000
        }
    },
    admin: {
        email: {
            address: 'mjcravero@gmail.com',
            password: 'founders1'
        },

    },
    server: {
        url: 'http://localhost:1336'
    }
};