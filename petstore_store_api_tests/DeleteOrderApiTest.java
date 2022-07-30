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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;


public class DeleteOrderApiTest extends BaseApiTest {

    public DeleteOrderApiTest() {

    }

    @Test
    public void uniRestDeleteOrder() throws UnirestException {  //UniRest

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.delete(baseURI + orderEndPoint + orderId)
                .header("accept", "application/json")
                .asString();

        assertEquals(response.getStatus(),200 );    //status kontrolü
        assertTrue(response.getBody().contains(orderId));   //bodynin sipariş id sini içermesinin kontrolü

    }

    @Test
    public void restAssuredDeleteOrder() {  //restAssured

        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete(baseURI + orderEndPoint + orderId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();    //response ta dönen id yi elde etmek için response u jsonPath e çevirdik
        String messageFromBody = jsonPath.getString("message");     //jsonpath den id yi bulup string e atadık

        assertEquals(200, response.statusCode());   //status kontrolü
        assertEquals(orderId, messageFromBody);     //response taki message ile sorguda verdiğimiz id nin karşılaştırması
    }

    @Test
    public void httpEntityDeleteOrder() throws JsonProcessingException {    //HttpEntity

        String entityUrl = baseURI + orderEndPoint + orderId;

        ResponseEntity<String> response =
                restTemplate.exchange(entityUrl, HttpMethod.DELETE, new HttpEntity<>(this.order), String.class, this.order.getId());

        JsonNode root = objectMapper.readTree(response.getBody());

        assertNotNull(root.path("message"));    //message pathi boş mu değil mi kontrolü
        assertEquals(response.getStatusCode(), HttpStatus.OK);  //status kontrolü
        assertEquals(orderId, root.path("message").asText());   //response taki id ile sorguda verdiğimiz id nin karşılaştırması

    }
}
