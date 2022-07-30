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

public class GetOrderApiTest extends BaseApiTest {

    public GetOrderApiTest(){

    }

    @Test
    public void getOrderUniRestMethod() throws UnirestException { //UniRest

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get(baseURI+ orderEndPoint +orderId)
                .header("accept", "application/json")
                .asString();

        assertEquals(200,response.getStatus()); //status kontrolü
        assertTrue(response.getBody().contains(orderId));   //bodynin sipariş id sini içermesinin kontrolü

    }

    @Test
    public void getOrderRestAssured(){ //RestAssured

        Response response = given()
                .header("Content-Type","application/json")
                .when()
                .get(baseURI+orderEndPoint+orderId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();    //response ta dönen id yi elde etmek için response u jsonPath e çevirdik
        String idFromResponse = jsonPath.getString("id");   //jsonpath den id yi bulup string e atadık

        assertEquals(200,response.statusCode());    //status kontrolü
        assertEquals(orderId,idFromResponse);   //response taki id ile sorguda verdiğimiz id nin karşılaştırması

    }

    @Test
    public void getOrderHttpEntity() throws JsonProcessingException { //HttpEntity

        ResponseEntity<String> response
                = restTemplate.getForEntity(baseURI+orderEndPoint+orderId , String.class);

        JsonNode root = objectMapper.readTree(response.getBody());

        assertNotNull(root.path("id"));     //response taki id pathinin boş olup olmadığının kontrolü
        assertEquals(response.getStatusCode(), HttpStatus.OK);  //status kontrolü
        assertEquals(orderId,root.path("id").asText());     //response taki id ile sorguda verdiğimiz id nin karşılaştırması

    }

}
