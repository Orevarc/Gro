// var req = require('supertest');

// describe('when not authenticated', function() {
//     describe('list', function() {
//         it('returns Unauthorized and 401', function(done) {
//             req(sails.express.app)
//                 .get(/additem)
//                 .expect(401)
//                 .expect('Content-Type', /json / , done);
//         });
//     });
// });

var request = require('supertest');
var assert = require('assert');
var async = require('async');
var stubs = require('stubs.js');


describe('Post', function() {

        // // use after all to create custom stub data
        // var user;
        // before(function(done) {
        //   User.create(uStub)
        //   .exec( function(err, u){
        //     if( err ) {
        //       console.log(err);
        //       return done(err);
        //     }

        //     user = u;
        //     user.password = password;
        //     done();
        //   });
        // });

        describe('Authenticated', function() {
            // use supertest.agent for store cookies ...
            // logged in agent
            var agent;
            // after authenticated requests login the user
            // before(function(done) {
            //   agent = request.agent(sails.hooks.http.app);
            //   agent.post('/auth/login')
            //   .send({
            //     email: user.email,
            //     password: user.password
            //   })
            //   .end(function(err) {
            //     done(err);
            //   });
            // })

            describe('JSON Requests', function() {
                it('/relato should create one post with authenticated user as creator');
            });
        })

        describe('UnAuthenticated', function() {
                describe('JSON Requests', function() {
                        it('/should return 401 and Unautorized', function(done) {

                                var postStub = stubs.postStub();
                                request(sails.hooks.http.app)
                                    .get(/additem)
                    .send(postStub)
                    .set('Accept', 'application/json
                                        ')
                    .expect(401)
                    .expect('
                                        Content - Type ', /json / , done);
                    .end(function(err, res) {
                        if (err) return done(err);

                        assert.ok(res.body.title);
                        assert.ok(res.body.body);
                        assert.equal(res.body.title, postStub.title);
                        assert.equal(res.body.body, postStub.body);

                        done();
                    });
            });
        });
    })

})