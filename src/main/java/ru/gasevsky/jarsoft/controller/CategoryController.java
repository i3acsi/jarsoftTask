package ru.gasevsky.jarsoft.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gasevsky.jarsoft.model.Category;
import ru.gasevsky.jarsoft.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {
    private final ApplicationService service;

    @GetMapping("/")
    public List<Category> findAll() {
        return service.findAllActiveCategories();
    }

    @GetMapping("/search/{name}")
    public List<Category> findByName(@PathVariable String name) {
        return service.findCategoriesByNameContains(name);
    }

    @PostMapping("/")
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return service.create(category);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Category category) {
        return service.update(category);
    }

    @DeleteMapping("/{id}")
    public List<Integer> delete(@PathVariable int id) {
        return service.deleteCategory(id);
    }
}