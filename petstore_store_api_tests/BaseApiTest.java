package petstore_store_api_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.springframework.web.client.RestTemplate;
import petstore_store_api_tests.models.Order;

import java.util.Locale;

import static io.restassured.RestAssured.*;

public abstract class BaseApiTest {
    //Bu classta testlerde ortak kullanılacak değişkenler mevcut. Hepsini tekrar tekrar yazmak yerine buradan inherit edebiliriz.
    //Bu classtan nesne oluşturmak gereksiz bu yüzden abstract tanımladım.

    static FakeValuesService faker = new FakeValuesService(new Locale("en-GB"),new RandomService());
    static String orderId = faker.bothify("#");//Sipariş için fake id oluşturma
    // static yaparak sadece bir kere initiate olmasını sağlarız. Böylece bir class ın metotlarını çalıştırdığımızda hepsinde farklı id olmaz.
    //siparişin her parametresini fakerlarla yapabilirdik . Şimdilik sadece id sini yaptım.

    Order order;
    String orderEndPoint;
    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    public BaseApiTest(){
        baseURI = "https://petstore.swagger.io/v2";

        this.order = new Order(Integer.parseInt(this.orderId)+1,0,0,"2022-07-25T20:31:50.794Z","placed",true);
        //order modelini bütün parametreli yapıcı metoduyla oluşturduk.Değerleri tek tek set etmek yerine daha kısa bir çözüm.
        //id nin +1 olmasının nedeni id 0 olursa rastgele büyük bir sayı atanıyor.

        this.orderEndPoint = "/store/order/";
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        //Yapıcı metotta null değişkenlere değer atıyoruz.
    }



}
