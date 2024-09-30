package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Sale;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesRepository {
    private final File database;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Sale> sales;
    private SalesRepository salesRepository = null;

    private SalesRepository() throws IOException {
        this.database = new File("sales.json");
        createFileIfNotExists();
        this.sales = loadFile();
    }

    public static SalesRepository getInstance() throws IOException {
        return new SalesRepository();
    }

    public List<Sale> getAllSales() {
        return sales;
    }

    public Optional<Sale> getSaleById(String id) {
        return sales.stream().filter(sale -> sale.getId().equals(id)).findFirst();
    }

    public void save(Sale sale) throws IOException {
        sales.add(sale);
        saveFile();
    }

    private void createFileIfNotExists() throws IOException {
        if (!database.exists()) {
            boolean fileWasCreated = database.createNewFile(); // Crea el archivo
        }
    }

    public void saveFile() throws IOException {
        objectMapper.writeValue(database, sales);
    }

    public void updateList()  {
        try {
            sales = loadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Sale> loadFile() throws IOException {
        if (!database.exists() || database.length() == 0) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(database, new TypeReference<>() {
        });
    }
}
