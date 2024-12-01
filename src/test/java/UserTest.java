import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class UserTest {
    private UserClient userClient;
    private String token;

    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Send POST request to /api/auth/register and compare Status Code with 200")
    @Description("Test for creation of a new user")
    public void creationOfUser () {
        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

        token = response.as(UserToken.class).getAccessToken();
    }

    @After
    public void deleteOfUser () {
        Response response = userClient.delete(token);
        response.then().assertThat().statusCode(202);
    }
}