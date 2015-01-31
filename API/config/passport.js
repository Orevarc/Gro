var passport = require('passport'),
    LocalStrategy = require('passport-local').Strategy,
    bcrypt = require('bcrypt');

// passport.use(new LocalStrategy(
//     function(username, password, done) {
//         User.findByUsername(username).done(function(err, user) {
//             if (err) {
//                 return done(null, err);
//             }
//             if (!user || user.length < 1) {
//                 console.log('User');
//                 return done(null, false, {
//                     message: 'Incorrect User'
//                 });
//             }

//             bcrypt.compare(password, user[0].password, function(err, res) {
//                 if (err || !res) {
//                     return done(null, false, {
//                         message: 'Invalid Password'
//                     });
//                 } else {
//                     return done(null, user);
//                 }
//             });
//         });
//     }));

module.exports = {
    http: {
        customMiddleware: function(app) {
            console.log('Express middleware for passport');
            app.use(passport.initialize());
            app.use(passport.session());
        }
    }
};