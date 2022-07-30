package petstore_store_api_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class GetInventoryApiTest extends BaseApiTest {
    String inventoryEndPoint;

    public GetInventoryApiTest(){
        inventoryEndPoint = "/store/inventory/";
    }

    @Test
    public void getInventoryUniRest() throws UnirestException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://petstore.swagger.io/v2/store/inventory/")
                .header("accept", "application/json")
                .asString();

        assertEquals(200,response.getStatus()); //status kontrolü
        assertFalse(response.getBody().isEmpty());  //body nin boş olup olmadığının kontrolü
    }

    @Test
    public void getInventoryRestAssured(){  //restAssured

        Response response = given()
                .header("Content-Type","application/json")
                .when()
                .get(baseURI+inventoryEndPoint)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()   //body yi konsola yazdırma
                .extract().response();

        JsonPath jsonPath = response.jsonPath();    //response ta dönen id yi elde etmek için response u jsonPath e çevirdik
        String statusFromResponse = jsonPath.getString("status");   //jsonpath den id yi bulup string e atadık

        assertEquals(200,response.statusCode());    //status kontrolü
        assertNotNull(statusFromResponse);      //response taki message ile sorguda verdiğimiz id nin karşılaştırması

    }

    @Test
    public void getInventoryHttpEntity() throws JsonProcessingException {   //HttpEntity

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseURI+inventoryEndPoint,String.class);

        JsonNode root = objectMapper.readTree(response.getBody());

        assertNotNull(root.path("status"));     //response taki status değişkeni boş mu değil mi kontrolü
        assertEquals(response.getStatusCode(), HttpStatus.OK);  //status kontrolü

    }

}
