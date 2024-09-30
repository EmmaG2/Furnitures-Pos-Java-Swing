package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Sale {
    private final String id;

    @Setter
    private String date;

    @Setter
    private List<FurnitureSold> furnituresSold;

    @Setter
    private double totalPrice;


    public Sale() {
        this.id = UUID.randomUUID().toString().split("-")[1];
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.date = today.format(formatter);
    }
}
