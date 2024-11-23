import clients.OrderClient;
import clients.UserClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import models.User;
import models.UserToken;
import models.UserWithCreds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> ingredients;
    private final int expectedStatusCode;
    private final Boolean expectedMessage;

    public OrderCreateTest(List<String> ingredients, int expectedStatusCode, Boolean expectedMessage) {
        this.ingredients = ingredients;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
    }

    private String token;
    private String loginToken;
    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200);
        token = response.as(UserToken.class).getAccessToken();
        System.out.println(token);

        UserWithCreds userWithCreds = new UserWithCreds("test999999@test.com", "999999");
        Response responseLogin = userClient.login(userWithCreds);
        responseLogin.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

        loginToken = responseLogin.as(UserToken.class).getAccessToken();
        System.out.println(loginToken);
    }

    @Parameterized.Parameters
    public static Object[][] ingredientsData() {
        return new Object[][] {
                {List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa7a",
                        "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa72"), 200, true},
                {Collections.emptyList(), 400, false},
        };
    }

    @Test
    public void orderCreateTest () {
        orderClient = new OrderClient();
        Order order = new Order(ingredients);

        Response response = orderClient.create(loginToken, order);
        response.then().assertThat().statusCode(expectedStatusCode)
                .and()
                .body("success", equalTo(expectedMessage));
        System.out.println(response.body().asString());
    }

    @After
    public void deleteOfUser () {
        System.out.println(token);
        Response response = userClient.delete(token);
        System.out.println(response.body().asString());
        response.then().assertThat().statusCode(202);

    }
}
