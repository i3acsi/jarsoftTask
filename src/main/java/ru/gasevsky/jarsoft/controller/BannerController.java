package ru.gasevsky.jarsoft.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("banner")
@AllArgsConstructor
public class BannerController {
    private final ApplicationService service;

    @GetMapping("/")
    public List<BannerDto> findAll() {
        return service.findAllActiveBanners();
    }

    @GetMapping("/search/{name}")
    public List<BannerDto> findByName(@PathVariable String name) {
        return service.findBannersByNameContains(name);
    }

    @PostMapping("/")
    public ResponseEntity<BannerDto> create(@RequestBody BannerDto bannerDto) {
        return service.create(bannerDto);
    }


    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody BannerDto bannerDto) {
        return service.update(bannerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.deleteBanner(id);
    }
}