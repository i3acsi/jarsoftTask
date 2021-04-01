package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.model.Category;

import java.util.List;

public interface CategoryRepo extends CrudRepository<Category, Integer> {
    List<Category> findAllByDeletedFalse();

    List<Category> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

}
