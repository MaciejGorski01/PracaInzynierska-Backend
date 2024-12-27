package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.services.NoteService;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/uploaded-files")
public class FileController {

    NoteService noteService;

    public FileController(NoteService noteService) {
        this.noteService = noteService;
    }


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


    @PutMapping("/remove/{id}")
    public ResponseEntity<String> removeFile(@PathVariable String id, @RequestBody String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()){
            Path existingFilePath = Paths.get("src/main/resources/static/", fileUrl);

            try{
                Files.deleteIfExists(existingFilePath);

                noteService.deleteFileUrl(id);

                return ResponseEntity.ok("File deleted and fileUrl removed from the database successfully.");
            } catch (IOException e) {
                throw new RuntimeException("File to delete existing file: " + existingFilePath, e);
            }
        } else {
            return ResponseEntity.badRequest().body("File URL is required.");
        }
    }
}
