package controllers;

import models.Furniture;
import models.FurnitureSold;
import models.Sale;
import models.exceptions.FurnitureDoesNotExists;
import models.exceptions.InsufficientStock;
import services.SaleService;
import utils.PriceFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SalesController {

    private final SaleService saleService;

    public SalesController(SaleService saleService) {
        this.saleService = saleService;
    }

    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    public Optional<Sale> getSaleById(String id) {
        return saleService.getSaleById(id);
    }

    public List<Sale> getSalesBetweenDates(String startDate, String endDate) throws IOException {
        return saleService.getSaleBetweenDates(startDate, endDate);
    }

    public void generateSale(List<FurnitureSold> order) {
        try {
            saleService.generateSale(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InsufficientStock | FurnitureDoesNotExists e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
