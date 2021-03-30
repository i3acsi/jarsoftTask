package ru.gasevsky.jarsoft.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gasevsky.jarsoft.model.Category;
import ru.gasevsky.jarsoft.repo.CategoryRepo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("category")
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private CategoryRepo categoryRepo;


    @GetMapping("/")
    public List<Category> findAll(){
        return StreamSupport.stream(
                this.categoryRepo.findAllByDeletedFalse().spliterator(), false
        ).collect(Collectors.toList());
    }
}
