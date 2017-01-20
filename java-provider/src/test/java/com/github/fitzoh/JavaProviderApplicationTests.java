package com.github.fitzoh;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(PactRunner.class)
@Provider("auth-service")
@PactFolder("../js-consumer/pacts")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class JavaProviderApplicationTests {

    @TestTarget
    public static final HttpTarget target = new HttpTarget(8080);

    private static TestContextManager testContextManager;

    @BeforeClass
    public static void beforeClass() throws Exception {
        testContextManager = new TestContextManager(JavaProviderApplicationTests.class);
        testContextManager.beforeTestClass();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        testContextManager.afterTestClass();
    }

    @Before
    public void setUp() throws Throwable {
        testContextManager.prepareTestInstance(this);
    }

    @Autowired
    UserRepository userRepository;

    @State("a user with username 'username' doesn't exists")
    public void ensureUserDoesNotExists() {
        userRepository.remove("username");
    }

    @State("a user with username 'username' and password 'password' exists")
    public void ensureUserExists(){
        User user = new User(1, "username", "password", "fitz");
        userRepository.put(user.getUsername(), user);
    }
}
