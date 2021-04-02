package ru.gasevsky.jarsoft.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gasevsky.jarsoft.service.BidService;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class BidController {
    private final BidService bidService;

    @GetMapping(value = "/bid")
    public ResponseEntity<String> bannerContent(@RequestParam("category") String reqName) {
        return bidService.getBanner(reqName);
    }
}
