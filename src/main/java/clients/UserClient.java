package clients;

import io.restassured.response.Response;
import models.User;
import models.UserWithCreds;
import models.UserWithCredsForChange;

import static io.restassured.RestAssured.given;

public class UserClient {
    public Response create (User user) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    public Response delete (String token) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .and()
                .delete("/api/auth/user");
    }

    public Response login (UserWithCreds userWithCreds) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(userWithCreds)
                .when()
                .post("/api/auth/login");
    }

    public Response changeCreds (String token, UserWithCredsForChange userWithCredsForChange) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .body(userWithCredsForChange)
                .when()
                .patch("/api/auth/user");
    }

}
