package org.example.pracainzynierska.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/uploaded-files")
public class FileController {

    @GetMapping("/{userId}/{fileName}")
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


//    @DeleteMapping("/")
//    public ResponseEntity<String> removeFile(@PathVariable String userId, @PathVariable String fileName) {
//        try {
//            Path filePath = Paths.get("src/main/resources/static/uploaded-files/" + userId + "/" + fileName);
//            File file = filePath.toFile();
//
//            if (file.exists() && file.isFile()) {
//                boolean deleted = file.delete();
//
//                if (deleted) {
//                    return ResponseEntity.ok("File deleted successfully.");
//                } else {
//                    return ResponseEntity.status(500).body("Failed to delete the file.");
//                }
//            } else {
//                return ResponseEntity.notFound().body("File not found.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error occurred while deleting the file.");
//        }
//    }
}
