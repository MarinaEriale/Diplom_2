package clients;

import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public Response create (String token, Order order) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .body(order)
                .when()
                .post("/api/orders");
    }
    public Response get (String token) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .when()
                .get("/api/orders");
    }


}
