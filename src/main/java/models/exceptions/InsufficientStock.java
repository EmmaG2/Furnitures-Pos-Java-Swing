package models.exceptions;

public class InsufficientStock extends Exception {
    public InsufficientStock(String message) {
        super(message);
    }
}
