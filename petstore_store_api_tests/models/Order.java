package petstore_store_api_tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   //lombok kütüphanesinden gelen Data anotasyonu class içindeki fieldların getter setter metotlarını otomatik oluşturur
@AllArgsConstructor //bu anotasyon class a bütün fieldların olduğu yapıcı metodu otomatik oluşturur
@NoArgsConstructor  //bu anotasyon class a parametresiz yapıcı metot oluşturur

public class Order {    //kullanacağımız modelimiz ve gereken parametreler
    private int id;
    private int petId;
    private int quantity;
    private String shipDate;
    private String placed;
    private boolean complete;
}

