package services;

import models.Furniture;
import models.FurnitureSold;
import models.Sale;
import models.exceptions.FurnitureDoesNotExists;
import models.exceptions.InsufficientStock;
import repositories.FurnitureRepository;
import repositories.SalesRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SaleService {
    private final SalesRepository salesRepository;
    private final FurnitureRepository furnitureRepository;
    public SaleService(SalesRepository salesRepository, FurnitureRepository furnitureRepository) {
        this.salesRepository = salesRepository;
        this.furnitureRepository = furnitureRepository;
    }

    public void generateSale(List<FurnitureSold> order) throws IOException, InsufficientStock, FurnitureDoesNotExists {
        double totalPrice = 0;
        Sale sale = new Sale();

        // Crear una nueva lista de muebles vendidos para esta venta
        List<FurnitureSold> furnituresSoldCopy = new ArrayList<>();

        for (FurnitureSold furnitureSold : order) {
            Furniture furniture = furnitureRepository.getById(furnitureSold.getId())
                    .orElseThrow(() -> new FurnitureDoesNotExists("El mueble " + furnitureSold.getFurnitureName() + " no existe"));

            updateFurnitureStock(furnitureSold, furniture);
            furnitureRepository.updateById(furniture.getId(), furniture);

            double price = furnitureSold.getPrice();
            if (price <= 0) {
                throw new IllegalArgumentException("El precio del mueble no es válido: " + price);
            }
            totalPrice += price * furnitureSold.getQuantity();

            // Clonar el objeto FurnitureSold para esta venta
            FurnitureSold furnitureSoldCopy = new FurnitureSold(furnitureSold.getId(), furnitureSold.getFurnitureName(), furnitureSold.getQuantity(), furnitureSold.getPrice());
            furnituresSoldCopy.add(furnitureSoldCopy);
        }

        sale.setTotalPrice(totalPrice);

        sale.setFurnituresSold(furnituresSoldCopy);
        salesRepository.save(sale);
    }


    public List<Sale> getSaleBetweenDates(String date1, String date2)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(date1.substring(0, 10), formatter);
        LocalDate endDate = LocalDate.parse(date2.substring(0, 10), formatter);

        salesRepository.updateList();
        List<Sale> allSales = salesRepository.getAllSales();

        return allSales.stream()
                .filter(sale -> {
                    LocalDate saleDate = LocalDate.parse(sale.getDate().substring(0, 10), formatter); // Convertir el String a LocalDate
                    return (saleDate.isEqual(startDate) || saleDate.isAfter(startDate)) &&
                            (saleDate.isEqual(endDate) || saleDate.isBefore(endDate));
                })
                .collect(Collectors.toList());
    }

    public Optional<Sale> getSaleById(String id) {
        return salesRepository.getSaleById(id);
    }

    public List<Sale> getAllSales() {
        return salesRepository.getAllSales();
    }

    private static void updateFurnitureStock(FurnitureSold furnitureSold, Furniture furniture) throws InsufficientStock {
        if (furniture.getStock() < furnitureSold.getQuantity()) {
            throw new InsufficientStock(
                    "No hay suficientes unidades para realizar la compra. "
                            + "Stock disponible: " + furniture.getStock() + ", "
                            + "Cantidad solicitada: " + furnitureSold.getQuantity()
            );
        }
        furniture.setStock(furniture.getStock() - furnitureSold.getQuantity());
    }

}
