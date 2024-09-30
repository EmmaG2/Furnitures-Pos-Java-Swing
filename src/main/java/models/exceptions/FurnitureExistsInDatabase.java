package models.exceptions;

public class FurnitureExistsInDatabase extends Exception {
    public FurnitureExistsInDatabase(String message) {
        super(message);
    }
}
