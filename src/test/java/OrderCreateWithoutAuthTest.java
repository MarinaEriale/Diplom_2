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

public class OrderCreateWithoutAuthTest extends RootTest{

    private String token;
    private String loginToken;
    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp () {

        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200);
        token = response.as(UserToken.class).getAccessToken();
        System.out.println(token);
    }

    @DisplayName("Send POST request to /api/orders and compare Status Code with 400")
    @Description("Test for creation of an order for not logged user without ingredients")
    @Test
    public void orderCreateTest () {
        orderClient = new OrderClient();
        Order order = new Order(Collections.emptyList());

        loginToken = null;

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
