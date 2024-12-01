import clients.OrderClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.AllIngredientsResponse;
import models.DataElement;
import models.Order;
import models.UserToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static constants.OrderEndpoints.INGREDIENTS;
import static io.restassured.RestAssured.given;


public class GetHashTest {


    @Test
    public void getIngsTest () {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getIngredients();


       AllIngredientsResponse allIngredientsResponse = response.body().as(AllIngredientsResponse.class);

        List<String> ids = new ArrayList<>();

        List<DataElement> ingredients = allIngredientsResponse.getData();
        for (DataElement ingredient : ingredients) {
            ids.add(ingredient.get_id());
        }

        System.out.println(ids);

    }



}
