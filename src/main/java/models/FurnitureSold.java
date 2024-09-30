package models;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FurnitureSold {
    private String id;
    private String furnitureName;
    private int quantity;
    private double price;
}
