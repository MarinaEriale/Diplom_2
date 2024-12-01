import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserToken;
import models.UserWithCreds;
import models.UserWithCredsForChange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserChangeCredsTest {
    private UserClient userClient;
    private String token;
    private String loginToken;

    private final String email;
    private final String name;

    public UserChangeCredsTest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200);
        token = response.as(UserToken.class).getAccessToken();

        UserWithCreds userWithCreds = new UserWithCreds("test999999@test.com", "999999");
        Response responseLogin = userClient.login(userWithCreds);
        responseLogin.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

        loginToken = responseLogin.as(UserToken.class).getAccessToken();
    }

    @Parameterized.Parameters
    public static Object[][] userCreds() {
        return new Object[][] {
                {"test900000@test.com", "Testovy_900000"},
                {"test900000@test.com", "Testovy_9"},
                {"test999999@test.com", "Testovy_900000"},
        };
    }

    @DisplayName("Send PATCH request to /api/auth/user and compare Status Code with 200")
    @Description("Test for change of data of existing user")
    @Test
    public void changeUserCredsTest () {
        UserWithCredsForChange userWithCredsForChange = new UserWithCredsForChange(email, name);
        userClient = new UserClient();

        Response response = userClient.changeCreds(loginToken, userWithCredsForChange);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @After
    public void deleteOfUser () {
        Response response = userClient.delete(token);
        response.then().assertThat().statusCode(202);
    }

}