package services;

import models.Furniture;
import models.exceptions.FurnitureExistsInDatabase;
import repositories.FurnitureRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FurnitureService {
    private final FurnitureRepository furnitureRepository;

    public FurnitureService(FurnitureRepository furnitureRepository) {
        this.furnitureRepository = furnitureRepository;
    }

    public List<Furniture> getAllFurnitures() {
        return this.furnitureRepository.getAllFurnitures();
    }

    public void updateById(String id, Furniture furniture) throws IOException {
        furnitureRepository.updateById(id, furniture);
    }

    public void deleteById(String id) throws IOException {
        furnitureRepository.deleteById(id);
    }

    public Optional<Furniture> findById(String id) throws IOException {
        return furnitureRepository.findById(id);
    }

    public void save(Furniture furniture) throws IOException, FurnitureExistsInDatabase {
        if (furnitureRepository.getByName(furniture.getName()).isPresent()) {
            throw new FurnitureExistsInDatabase("El mueble, ya existe en la base de datos");
        } else furnitureRepository.save(furniture);
    }
}
