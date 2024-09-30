package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ToString
@Getter
public class Furniture {
    private final String id;

    @Setter
    private String purchaseOrFabricationDate;

    @Setter
    private String name;

    @Setter
    private int stock;

    @Setter
    private double salePrice;

    @Setter
    private double purchasePrice;

    public Furniture() {
        this.id = UUID.randomUUID().toString().split("-")[0];
    }
}
