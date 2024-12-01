import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserToken;
import models.UserWithCreds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserLoginTest {
    private UserClient userClient;
    private String token;
    private final String email;
    private final String password;
    private final int expectedStatusCode;
    private final String expectedKey;
    private final Boolean expectedMessage;

    public UserLoginTest(String email, String password, int expectedStatusCode, String expectedKey, Boolean expectedMessage) {
        this.email = email;
        this.password = password;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedKey = expectedKey;
        this.expectedMessage = expectedMessage;
    }

    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200);
        token = response.as(UserToken.class).getAccessToken();
        System.out.println(token);
    }

    @Parameterized.Parameters
    public static Object[][] userCreds() {
        return new Object[][] {
                {"test999999@test.com", "999999", 200, "success", true},
                {"test909090@test.com", "909090", 401, "success", false},
                {"test909090@test.com", "999999", 401, "success", false},
                {"test999999@test.com", "909090", 401, "success", false},
        };
    }

    @DisplayName("Send variable POST requests to /api/auth/login and compare Status Code with 200 or 401")
    @Description("Test for login of already existing user with correct data and incorrect data")
    @Test
    public void loginOfUserTest () {
        UserWithCreds userWithCreds = new UserWithCreds(email, password);
        userClient = new UserClient();

        Response response = userClient.login(userWithCreds);
        response.then().assertThat().statusCode(expectedStatusCode)
                .and()
                .body(expectedKey, equalTo(expectedMessage));

    }

    @After
    public void deleteOfUser () {
        System.out.println(token);
        Response response = userClient.delete(token);
        System.out.println(response.body().asString());
        response.then().assertThat().statusCode(202);

    }
}