package com.e_commerce_creator.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/v1/owner")
@Slf4j
public class OwnerController {


    @GetMapping
    public String get() {
        return "GET:: owner controller";
    }


    @PostMapping
    public String post() {
        return "POST:: owner controller";
    }


    @PutMapping
    public String put() {
        return "PUT:: owner controller";
    }


    @DeleteMapping
    public String delete() {
        return "DELETE:: owner controller";
    }

}
