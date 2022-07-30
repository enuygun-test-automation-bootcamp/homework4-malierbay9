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
import org.springframework.http.HttpEntity;
import petstore_store_api_tests.models.Order;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostOrderApiTest extends BaseApiTest {

    String body;    //post sorgularında body ye ihtiyacımız olacak.

    public PostOrderApiTest() throws JsonProcessingException {

        this.body = objectMapper.writeValueAsString(this.order);// model objemizi objectMapper la stringe çevirip body mizi elde ediyoruz.

    }

    @Test
    public void postOrderUniRestMethod() throws UnirestException {   //unirest ile test

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post(baseURI+ orderEndPoint)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(this.body)
                .asString();

        assertEquals(200,response.getStatus());//status kontrolü
        assertTrue(response.getBody().contains(orderId));//bodynin sipariş id sini içermesinin kontrolü

    }

    @Test
    public void postOrderRestAssuredMethod(){    //restAssured

        Response response = given()
                .header("Content-Type","application/json")
                .body(this.body)
                .when()
                .post(orderEndPoint)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()       // body yi konsola yazdırma
                .extract().response();

        JsonPath jsonPath = response.jsonPath();    //response ta dönen id yi elde etmek için response u jsonPath e çevirdik
        String idFromResponse = jsonPath.getString("id");   //jsonpath den id yi bulup string e atadık

        assertEquals(200,response.statusCode());    //status kontrolü
        assertEquals(orderId,idFromResponse);   //response taki id ile sorguda verdiğimiz id nin karşılaştırması

    }

    @Test
    public void postOrderHttpEntityMethod() throws JsonProcessingException { //HttpEntity

        HttpEntity<Order> request = new HttpEntity<Order>(order);

        String orderResultAsJsonStr =
                restTemplate.postForObject(baseURI+ orderEndPoint, request, String.class);

        JsonNode root = objectMapper.readTree(orderResultAsJsonStr);

        assertNotNull(orderResultAsJsonStr);
        assertNotNull(root);
        assertNotNull(root.path("id").asText());    //bu değişkenlerin ve id nin boş olup olmadığının kontrolü
        assertNotEquals("",root.path("id").asText());   //id "" şeklinde dönebiliyor bunun kontrolü
        assertEquals(orderId,root.path("id").asText());     //response taki id ile sorguda verdiğimiz id nin karşılaştırması

    }

}
