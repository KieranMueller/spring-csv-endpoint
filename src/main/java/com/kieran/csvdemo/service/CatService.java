package com.kieran.csvdemo.service;

import com.kieran.csvdemo.entity.Cat;
import com.kieran.csvdemo.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;

    public ResponseEntity<String> uploadCatsFromCsv(MultipartFile file) {
        log.info("CatService::uploadCatsFromCsv. Attempting to save file to DB");
        long start = System.currentTimeMillis();
        try {
            List<Cat> cats = new ArrayList<>();
            getCSVRecordsFromFile(file).forEach(record -> {
                log.info("Row: {}, {}, {}, {}, {}",
                        record.get(0), record.get(1), record.get(2), record.get(3), record.get(4));
                cats.add(Cat.builder()
                        .id(record.get(0))
                        .name(record.get(1))
                        .breed(record.get(2))
                        .color(record.get(3))
                        .age(record.get(4))
                        .build());
            });
            catRepository.saveAll(cats);
            log.info("End CatService::uploadCatsFromCsv. Successfully saved rows to DB in {}ms",
                    System.currentTimeMillis() - start);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return handleExceptions(e, "uploadCatsFromCsv");
        }
    }

    private List<CSVRecord> getCSVRecordsFromFile(MultipartFile file) throws IOException {
        log.info("CatService::getCSVRecordsFromFile");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        return csvParser.getRecords();
    }

    private ResponseEntity<String> handleExceptions(Exception e, String method) {
        log.error("CatService::handleExceptions. Received {} from {} method", e, method);
        if (e instanceof IOException) {
            return ResponseEntity.internalServerError().body("IOException occurred: " + e.getMessage());
        } else {
            return ResponseEntity.internalServerError().body("Exception occurred: " + e.getMessage());
        }
    }
}
