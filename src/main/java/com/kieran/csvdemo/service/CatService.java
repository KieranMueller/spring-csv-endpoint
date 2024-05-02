package com.kieran.csvdemo.service;

import com.kieran.csvdemo.entity.Cat;
import com.kieran.csvdemo.repository.CatRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;
    private final String FILE_PATH = "C:\\Users\\kiera\\Desktop\\Projects\\";

    public ResponseEntity<String> uploadCatsFromCsv(MultipartFile file) {
        log.info("In CatService::uploadCatsFromCsv. Attempting to save file to DB");
        try {

            CSVReader reader = new CSVReader(new FileReader(FILE_PATH + file.getOriginalFilename()));
            List<String[]> rows = reader.readAll();
            List<Cat> cats = new ArrayList<>();
            for (String[] row : rows) {
                log.info("Row: {}, {}, {}, {}, {}", row[0], row[1], row[2], row[3], row[4]);
                cats.add(Cat.builder()
                        .id(row[0])
                        .name(row[1])
                        .breed(row[2])
                        .color(row[3])
                        .age(row[4])
                        .build());
            }

            catRepository.saveAll(cats);
            log.info("End of CatService::uploadCatsFromCsv. Successfully saved rows to DB");
            return ResponseEntity.ok().body("Success");

        } catch (CsvException e) {
            log.error("CatService::uploadCatsFromCsv. Encountered CsvException: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Encountered CsvException: " + e.getMessage());
        } catch (IOException e) {
            log.error("CatService::uploadCatsFromCsv. Encountered IOException: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Encountered IOException: " + e.getMessage());
        } catch (Exception e) {
            log.error("CatService::uploadCatsFromCsv. Encountered unknown exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Encountered unknown exception: " + e.getMessage());
        }
    }
}
