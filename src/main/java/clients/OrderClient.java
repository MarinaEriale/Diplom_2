package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static constants.OrderEndpoints.INGREDIENTS;
import static constants.OrderEndpoints.ORDERS;
import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Get response for creation of a new order")
    public Response create (String token, Order order) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .body(order)
                .when()
                .post(ORDERS);
    }
    @Step("Get response for getting a list of orders")
    public Response get (String token) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .when()
                .get(ORDERS);
    }

    @Step("Get response for getting a list of ingredients")
    public Response getIngredients () {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json")
                .when()
                .get(INGREDIENTS);
    }


}
