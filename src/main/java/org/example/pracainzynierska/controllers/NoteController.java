package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.exceptions.EntityNotFoundException;
import org.example.pracainzynierska.exceptions.ForeignKeyViolationException;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.NoteService;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/notes")
public class NoteController {

    NoteService noteService;
    JsonValidator jsonValidator = new JsonValidator("schemas/note_schema.json");

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<NoteDto>> getAllUserNotes(@PathVariable String userid) {
        List<NoteDto> userNotes = noteService.findAllUserNotes(userid);
        List<NoteDto> userNotesImmutable = List.of(userNotes.toArray(new NoteDto[]{}));

        if (userNotesImmutable.isEmpty()) {
            throw new EntityNotFoundException("User's notes");
        }

        return ResponseEntity.ok(userNotesImmutable);
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        List<NoteDto> allNotes = noteService.findAllNotes();
        List<NoteDto> allNotesImmutable = List.of(allNotes.toArray(new NoteDto[]{}));

        if (allNotesImmutable.isEmpty()) {
            throw new EntityNotFoundException("Notes");
        }

        return ResponseEntity.ok(allNotesImmutable);
    }


    @PostMapping
    public ResponseEntity<?> createNote(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("data") String json) {
        JSONObject jsonObject = new JSONObject(json);
        jsonValidator.validator(jsonObject);

        try {
            String userId = jsonObject.getString("note_owner_id");

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User ID is missing"));
            }

            String fileUrl = null;
            if (file != null){
                Path uploadPath = Paths.get("src/main/resources/static/uploaded-files", userId);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename().replace(" ", "_");
                String filePath = uploadPath + File.separator + fileName;
                var targetFile = new File(filePath);
                Files.copy(file.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Path fileUrlPath = Paths.get("uploaded-files/", userId);
                fileUrl = fileUrlPath + File.separator + fileName;

            }

            if (fileUrl != null) {
                jsonObject.put("fileUrl", fileUrl);
            }

            noteService.addNote(jsonObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Fk_user_id")){
                throw new ForeignKeyViolationException("Invalid foreign key for note_owner_id");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Data integrity violation"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "File upload failed"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable String id) {
        try {

            NoteDto note = noteService.findNoteById(id);
            return ResponseEntity.ok(note);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Note");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editNote(@PathVariable String id, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("data") String json) {
        NoteDto existingNoteDto;

        try {
            existingNoteDto = noteService.findNoteById(id);
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("Note");
        }

        JSONObject jsonObject = new JSONObject(json);
        //JSONObject updatedNote = new JSONObject();



//        updatedNote.put("title", jsonObject.has("title") ? jsonObject.getString("title") : existingNoteDto.title());
//        updatedNote.put("tag", jsonObject.has("tag") ? jsonObject.getString("tag") : existingNoteDto.tag());
//        updatedNote.put("favourite", jsonObject.has("favourite") ? jsonObject.getBoolean("favourite") : existingNoteDto.favourite());
//        updatedNote.put("content", jsonObject.has("content") ? jsonObject.getString("content") : existingNoteDto.content());
//        updatedNote.put("color", jsonObject.has("color") ? jsonObject.getString("color") : existingNoteDto.color());
//        updatedNote.put("fileUrl", jsonObject.has("fileUrl") ? jsonObject.getString("fileUrl") : existingNoteDto.fileUrl());


        String newFileUrl = null;
        if (file != null && !file.isEmpty()){
            if (existingNoteDto.fileUrl() != null && !existingNoteDto.fileUrl().isEmpty()){
                Path existingFilePath = Paths.get("src/main/resources/static", existingNoteDto.fileUrl());
                try{
                    Files.deleteIfExists(existingFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("Faile to delete existing file: " + existingFilePath, e);
                }
            }

            String userId = jsonObject.getString("note_owner_id");
            Path uploadPath = Paths.get("src/main/resources/static/uploaded-files", userId);

            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create directories: " + uploadPath, e);
                }
            }

            String fileName = file.getOriginalFilename().replace(" ", "_");
            String filePath = uploadPath + File.separator + fileName;
            var targetFile = new File(filePath);
            try {
                Files.copy(file.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + filePath, e);
            }

            Path fileUrlPath = Paths.get("uploaded-files/", userId);
            newFileUrl = fileUrlPath + File.separator + fileName;
            //updatedNote.put("fileUrl", newFileUrl);
            jsonObject.put("fileUrl", newFileUrl);

        } else {
//            updatedNote.put("fileUrl", existingNoteDto.fileUrl());
            jsonObject.put("fileUrl", existingNoteDto.fileUrl());
        }



        jsonValidator.validator(jsonObject);

        noteService.updateNote(jsonObject, id);

        return ResponseEntity.ok(jsonObject.toMap());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id, @RequestBody(required = false) String filePath) {
        try {
            NoteDto note = noteService.findNoteById(id);

            if (filePath != null && !filePath.isBlank()) {
                JSONObject filePathName = new JSONObject(filePath);

                if (filePathName.has("filePath")) {
                    String filePathString = filePathName.getString("filePath").replace("\\", "/");
                    Path path = Paths.get("src/main/resources/static/", filePathString);

                    if (Files.exists(path)) {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete file: " + filePathString, e);
                        }
                    }
                }
            }
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new EntityNotFoundException("Note to delete");
        }

    }


}
