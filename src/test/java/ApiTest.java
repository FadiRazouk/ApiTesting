import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    static {
        RestAssured.baseURI = "https://pay2.foodics.dev";
    }

    @Test
    public void testValidLoginAndWhoAmI() {
        // Step 1: Perform Login
        Response loginResponse = given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{ \"email\": \"merchant@foodics.com\", \"password\": \"123456\" }")
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(200) // Verify login success
                .extract()
                .response();
        String token = (String) loginResponse.path("token");

        // Step 2: Verify 'Otp' with valid token
        Response Otp = given()
                .contentType(io.restassured.http.ContentType.JSON)
                .header("Accept", "application/json")
                .body("{\"otp\":\"1234\", \"token\": \"" + token + "\"}")
                .when()
                .post("/cp_internal/otp")
                .then()
                .statusCode(200)
                .extract().response(); // Capture the response

        String tokenAfterOtp = (String) Otp.path("token");
        // Step 3: Verify 'whoami' with valid token
        Response whoamiResponse = given()
                .header("Authorization", "Bearer " + tokenAfterOtp) // Use the extracted token
                .when()
                .get("/cp_internal/whoami")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Validate the email in the response
        whoamiResponse
                .then()
                .body("user.merchant.owner.email", equalTo("merchant@foodics.com"));
    }

    @Test
    public void testInvalidLogin() {
        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{ \"email\": \"wrong@foodics.com\", \"password\": \"wrongpass\" }")
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(500);
    }

    @Test
    public void testInvalidEmailFormat() {
        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{ \"email\": \"thisIsNotAValidEmailFormat\", \"password\": \"123456\" }")
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(302);
    }

    @Test
    public void testExpiredOtp() {
        Response loginResponse = given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{ \"email\": \"merchant@foodics.com\", \"password\": \"123456\" }")
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(200) // Verify login success
                .extract()
                .response();
        String token = (String) loginResponse.path("token");

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .header("Accept", "application/json")
                .body("{\"otp\":\"expiredOtp\", \"token\": \"" + token + "\"}")
                .when()
                .post("/cp_internal/otp")
                .then()
                .statusCode(400) // Bad Request or 401 Unauthorized, depending on implementation
                .body("message", containsString("The entered OTP is incorrect. Please try again. You have 2 attempt"));

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .header("Accept", "application/json")
                .body("{\"otp\":\"expiredOtp\", \"token\": \"" + token + "\"}")
                .when()
                .post("/cp_internal/otp")
                .then()
                .statusCode(400) // Bad Request or 401 Unauthorized, depending on implementation
                .body("message", containsString("The entered OTP is incorrect. Please try again. You have 1 attempt"));
    }

    @Test
    public void testWhoAmIWithoutAuth() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/cp_internal/whoami")
                .then()
                .statusCode(401) // Unauthorized
                .body("message", containsString("Unauthenticated"));
    }

    @Test
    public void testLoginWithoutEmailAndPassword() {
        given()
                .header("Accept", "application/json")
                .contentType(io.restassured.http.ContentType.JSON)
                .body("{}") // Empty body
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(422) // Bad Request
                .body("message", equalTo("The Email field is required. (and 1 more error)"))
                .body("errors.email[0]", equalTo("The Email field is required."))
                .body("errors.password[0]", equalTo("The password field is required."));
    }
}
