import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserToken;
import models.UserWithCredsForChange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
@RunWith(Parameterized.class)
public class UserChangeCredsWithoutAuthTest extends RootTest{
        private UserClient userClient;
        private String token;

        private final String email;
        private final String name;

        public UserChangeCredsWithoutAuthTest(String email, String name) {
            this.email = email;
            this.name = name;
        }

        @Before
        public void setUp () {

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
                    {"test900000@test.com", "Testovy_900000"},
                    {"test900000@test.com", "Testovy_9"},
                    {"test999999@test.com", "Testovy_900000"},
            };
        }

        @DisplayName("Send PATCH request to /api/auth/user and compare Status Code with 401")
        @Description("Test for impossibility of change of data of not logged user")
        @Test
        public void changeUserCredsTest () {
            UserWithCredsForChange userWithCredsForChange = new UserWithCredsForChange(email, name);
            userClient = new UserClient();

            Response response = userClient.changeCreds(null, userWithCredsForChange);
            response.then().assertThat().statusCode(401)
                    .and()
                    .body("success", equalTo(false));
        }

    @After
    public void deleteOfUser () {
        System.out.println(token);
        Response response = userClient.delete(token);
        System.out.println(response.body().asString());
        response.then().assertThat().statusCode(202);

    }
}
