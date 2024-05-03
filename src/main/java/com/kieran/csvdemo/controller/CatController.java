package com.kieran.csvdemo.controller;

import com.kieran.csvdemo.service.CatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CatController {

    private final CatService catService;

    @PostMapping("/upload-cats")
    public ResponseEntity<String> uploadCatsFromCsv(@RequestParam("cat_csv") MultipartFile file) {
        log.info("In CatController::uploadCatsFromCsv. Received file with name: {}", file.getOriginalFilename());
        return catService.uploadCatsFromCsv(file);
    }
}
