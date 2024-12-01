import io.restassured.RestAssured;
import org.junit.Before;

public abstract class RootTest {
    @Before
    public void rootSetUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}