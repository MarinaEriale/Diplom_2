package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import models.UserWithCreds;
import models.UserWithCredsForChange;

import static io.restassured.RestAssured.given;
import static constants.UserEndpoints.*;

public class UserClient {
    @Step("Get response for creation of a new user")
    public Response create (User user) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER);
    }
    @Step("Get response for delete of existing user")
    public Response delete (String token) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .and()
                .delete(USER);
    }

    @Step("Get response for login of existing user")
    public Response login (UserWithCreds userWithCreds) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(userWithCreds)
                .when()
                .post(LOGIN);
    }

    @Step("Get response for change of data of existing user")
    public Response changeCreds (String token, UserWithCredsForChange userWithCredsForChange) {
        return given()
                .log().all()
                .headers(
                        "Content-type", "application/json",
                        "Authorization", token)
                .body(userWithCredsForChange)
                .when()
                .patch(USER);
    }

}
