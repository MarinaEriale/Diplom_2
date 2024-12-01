import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class ParameterizedClientTest {
    private UserClient userClient;
    private final String email;
    private final String password;
    private final String name;

    public ParameterizedClientTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Parameterized.Parameters
    public static Object[][] userData() {
        return new Object[][] {
                {null, "888888", "Testovy_8"},
                {"test888888@test.com", null, "Testovy_8"},
                {"test888888@test.com", "888888", null},
        };
    }

    @DisplayName("Send POST request to /api/auth/register and compare Status Code with 400")
    @Description("Test for impossible creation of a new user with invalid data")
    @Test
    public void creationOfExistingUser () {
        User user = new User(email, password, name);
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

}