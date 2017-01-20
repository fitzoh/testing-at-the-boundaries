describe("LoginService", function () {
    "use strict";
    let provider;
    let loginService;
    const pactPort = 1234;


    //We need to have Pact start a mock service before we get started
    //so that the consumer has something to fire requests at
    beforeAll(function (done) {
        provider = Pact.mockService({
            consumer: 'web-app',
            provider: 'auth-service',
            port: pactPort,
            done: function (error) {
                expect(error).toBe(null);
            }
        });
        //create an instance of the object we're actually going to test
        loginService = new LoginService(`http://localhost:${pactPort}`);


        // This ensures your pact-mock-service is in a clean state before
        // running your test suite.
        provider.resetSession(done);
    });

    //async test setup
    beforeEach(function (done) {
        setTimeout(function () {
            done();
        }, 100);
    });


    const username = "username";
    const password = "password";
    const request = {
        method: 'post',
        path: "/login",
        body: {
            username: username,
            password: password
        },
        headers: {
            "Content-Type": "application/json"
        }

    }

    it("should return an error on invalid credentials", function (done) {
        provider
            .uponReceiving("a request with invalid credentials")
            .given(`a user with username '${username}' doesn't exists`)
            .withRequest(request)
            .willRespondWith(401, {
                "Content-Type": "application/json"
            }, {
                "error": "invalid_credentials"
            });


        provider.run(done, function (runComplete) {
            loginService.login(username, password)
                .then(function (response) {
                    expect(response.status).toEqual(401);
                    expect(response.error).toEqual("invalid_credentials");
                    runComplete();
                })
        });
    });


    it("should return a token on successful login", function (done) {
        provider
            .uponReceiving("a request with valid credentials")
            .given(`a user with username '${username}' and password '${password}' exists`)
            .withRequest(request)
            .willRespondWith(200, {
                "Content-Type": "application/json"
            }, {
                "token": Pact.Match.term({generate: "some-long-token-string", matcher : ".*-.*-.*-.*"}),
                "user": Pact.Match.somethingLike({
                    "id": 5,
                    "username": username
                })
            });

        provider.run(done, function (runComplete) {
            loginService.login(username, password)
                .then(function (response) {
                    expect(response.status).toEqual(200);
                    expect(response.user.id).toEqual(5)
                    expect(response.user.username).toEqual("username");
                    expect(response.token).toEqual("some-long-token-string");
                    runComplete();
                });
        });
    });

});




//don't mind me, just cheating in case I can't remember method names/signatures during the demo
// Pact.Match.somethingLike(5)
// Pact.Match.term({generate: "some-long-token-string", matcher: ".*-.*-.*-.*"})