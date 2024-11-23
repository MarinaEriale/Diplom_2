import clients.UserClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ExistingUserTest {

    private UserClient userClient;
    private String token;

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

    @Test
    public void creationOfExistingUser () {
        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false));

    }

    @After
    public void deleteOfUser () {
        System.out.println("Токен в методе удаления " + token);
        Response response = userClient.delete(token);
        System.out.println(response.body().asString());
        response.then().assertThat().statusCode(202);
    }
}