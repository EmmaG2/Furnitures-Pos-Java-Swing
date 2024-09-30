package controllers;

import models.Furniture;
import models.exceptions.FurnitureExistsInDatabase;
import services.FurnitureService;
import utils.PriceFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FurnituresController {

    private final FurnitureService furnitureService;

    public FurnituresController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    public List<Furniture> getAllFurnitures() {
        return furnitureService.getAllFurnitures();
    }

    public Optional<Furniture> getFurnitureById(String id) {
        try {
            return furnitureService.findById(id);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void updateById(String id, Furniture furniture) {
        try {
            furnitureService.updateById(id, furniture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(String id) {
        try {
            furnitureService.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Furniture furniture) {
        try {
            furnitureService.save(furniture);
        } catch (IOException | FurnitureExistsInDatabase e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
