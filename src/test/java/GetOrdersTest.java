import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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


public class GetOrdersTest {
    private List<String> ingredients;
    private int expectedStatusCode;
    private Boolean expectedMessage;


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

        orderClient = new OrderClient();
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa7a",
                "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa72"));

        Response orderResponse = orderClient.create(loginToken, order);
        orderResponse.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
        System.out.println(orderResponse.body().asString());
    }


    @DisplayName("Send GET request to /api/orders and compare Status Code with 200")
    @Description("Test for getting of all orders of logged user")
    @Test
    public void getOrderTest () {
        Response response = orderClient.get(loginToken);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
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
