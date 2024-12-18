package org.example.pracainzynierska.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    @GetMapping("/uploaded-files/{userId}/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String userId, @PathVariable String fileName) {
        try {
            Path filePath = Paths.get("src/main/resources/static/uploaded-files/" + userId + "/" + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
