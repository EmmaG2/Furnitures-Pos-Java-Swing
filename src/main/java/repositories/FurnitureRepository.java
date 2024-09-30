package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Furniture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FurnitureRepository {

    private final File database;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Furniture> furnitureList;
    private static FurnitureRepository furnitureRepository = null;

    private FurnitureRepository(String path) throws IOException {
        this.database = new File(path);
        createFileIfNotExists();
        furnitureList = loadFile();
    }

    public static FurnitureRepository getInstance() throws IOException {
        if (furnitureRepository == null) {
            furnitureRepository = new FurnitureRepository("furnitures.json");
        }

        return furnitureRepository;
    }

    private void createFileIfNotExists() throws IOException {
        if (!database.exists()) {
            boolean fileWasCreated = database.createNewFile(); // Crea el archivo
        }
    }

    public List<Furniture> getAllFurnitures() {
        return furnitureList;
    }

    public Optional<Furniture> getByName(String name) {
        return furnitureList.stream().filter(f -> f.getName().equals(name)).findFirst();
    }

    public Optional<Furniture> getById(String id) {
        return furnitureList.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public void updateById(String id, Furniture furniture) throws IOException {
        Optional<Furniture> optionalFurniture = findById(id);

        if (optionalFurniture.isPresent()) {
            Furniture furnitureToUpdate = optionalFurniture.get();
            furnitureToUpdate.setName(furniture.getName());
            furnitureToUpdate.setStock(furniture.getStock());
            furnitureToUpdate.setPurchasePrice(furniture.getPurchasePrice());
            furnitureToUpdate.setSalePrice(furniture.getSalePrice());
            furnitureToUpdate.setPurchaseOrFabricationDate(furniture.getPurchaseOrFabricationDate());

            for (int i = 0; i < furnitureList.size(); i++) {
                if (furnitureList.get(i).getId().equals(id)) {
                    furnitureList.set(i, furnitureToUpdate);
                    saveFile();
                }
            }
        }
    }

    public Optional<Furniture> findById(String id) {
        return furnitureList.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    public void deleteById(String id) throws IOException {
        furnitureList.removeIf(f -> f.getId().equals(id));
        saveFile();
    }

    public void deleteByName(String name) throws IOException {
        furnitureList.removeIf(f -> f.getName().equals(name));
        saveFile();
    }

    public void save(Furniture furniture) throws IOException {
        furnitureList.add(furniture);
        saveFile();
    }

    private void saveFile() throws IOException {
        objectMapper.writeValue(database, furnitureList);
    }

    private List<Furniture> loadFile() throws IOException {

        if (!database.exists() || database.length() == 0) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(database, new TypeReference<List<Furniture>>() {});
    }
}
