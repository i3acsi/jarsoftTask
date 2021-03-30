package ru.gasevsky.jarsoft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.repo.BannerRepo;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("banner")
@AllArgsConstructor
@Slf4j
public class BannerController {
//    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
//    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
//    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
//    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final BannerRepo bannerRepo;
    private final ObjectMapper mapper;

    @GetMapping("/")
    public List<BannerDto> findAll() {
        return StreamSupport.stream(
                this.bannerRepo.findAllByDeletedFalse().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerDto> findById(@PathVariable int id) {
        var banner = this.bannerRepo.findById(id);
        return new ResponseEntity<BannerDto>(
                banner.orElse(new BannerDto()),
                banner.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<BannerDto> create(@RequestBody BannerDto bannerDto) throws JsonProcessingException {
        System.out.println(bannerDto);
        return new ResponseEntity<BannerDto>(bannerDto,
//                this.bannerRepo.save(banner),
                HttpStatus.CREATED
        );
    }

//
//    @PutMapping("/")
//    public ResponseEntity<Void> update(@RequestBody Banner banner) {
//        this.bannerRepo.save(banner);
//        return ResponseEntity.ok().build();
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable int id) {
//        Banner banner = new Banner();
//        banner.setId(id);
//        this.bannerRepo.delete(banner);
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("{id}")
//    public Announcement getOne(@PathVariable("id") Announcement announcement) {
//        return announcement;
//    }


//
//    //    @RequestMapping(value = {"id"}, method = RequestMethod.PUT)
//    @PutMapping("{id}")
//    public Announcement update(
//            @PathVariable("id") Announcement announcementFromDb,
//            @RequestBody Announcement announcement) throws IOException {
//        //из announcement в announcementFromDb перекладываем все значения кроме  id и created
//        BeanUtils.copyProperties(announcement, announcementFromDb, "id", "created");
//        fillMeta(announcementFromDb);
//        Announcement updatedA = aRepo.save(announcementFromDb);
//        wsSender.accept(EventType.UPDATE, updatedA);
//        return updatedA;
//    }
//
//    @DeleteMapping("{id}")
//    public void delete(@PathVariable("id") Announcement announcement) {
//        aRepo.delete(announcement);
//        wsSender.accept(EventType.REMOVE, announcement);
//    }
}

