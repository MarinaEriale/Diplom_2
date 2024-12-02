import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreateWithoutIngsTest extends RootTest {

    private String token;
    private String loginToken;
    private UserClient userClient;
    private OrderClient orderClient;
    private static List<String> ids;

    @Before
    public void setUp () {

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

    @DisplayName("Send POST request to /api/orders and compare Status Code with 400")
    @Description("Test for creation of an order for logged user without ingredients")
    @Test
    public void orderCreateTest () {
        orderClient = new OrderClient();
        Order order = new Order(Collections.emptyList());

        Response response = orderClient.create(loginToken, order);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", equalTo(false));
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
