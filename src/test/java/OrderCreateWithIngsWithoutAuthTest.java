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
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreateWithIngsWithoutAuthTest {


    private String token;
    private String loginToken;
    private UserClient userClient;
    private OrderClient orderClient;
    private static List<String> ids;


    @Before
    public void setUp () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        User user = new User("test999999@test.com", "999999", "Testovy_9");
        userClient = new UserClient();

        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200);
        token = response.as(UserToken.class).getAccessToken();
        System.out.println(token);
        OrderClient orderClient = new OrderClient();
        Response responseIngredients = orderClient.getIngredients();


        AllIngredientsResponse allIngredientsResponse = responseIngredients.body().as(AllIngredientsResponse.class);

        ids = new ArrayList<>();

        List<DataElement> ingredients = allIngredientsResponse.getData();
        for (DataElement ingredient : ingredients) {
            ids.add(ingredient.get_id());
        }
    }

//    @Parameterized.Parameters
//    public static Object[][] ingredientsData() {
//        return new Object[][] {
//                {List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa7a",
//                        "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa72"), 200, true},
//                {Collections.emptyList(), 400, false},
//        };
//    }

    @DisplayName("Send POST request to /api/orders and compare Status Code with 200")
    @Description("Test for creation of an order for not logged user with valid ingredients data")
    @Test
    public void orderCreateTest () {
        orderClient = new OrderClient();
        Order order = new Order(ids);

        loginToken = null;

        Response response = orderClient.create(loginToken, order);
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
