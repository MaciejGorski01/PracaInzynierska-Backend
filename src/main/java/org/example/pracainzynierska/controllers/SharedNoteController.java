package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.SharedNoteDto;
import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.exceptions.EntityNotFoundException;
import org.example.pracainzynierska.exceptions.ForeignKeyViolationException;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.SharedNoteService;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/sharednotes")
public class SharedNoteController {
    SharedNoteService sharedNoteService;
    JsonValidator jsonValidator = new JsonValidator("schemas/sharedNote_schema.json");

    public SharedNoteController(SharedNoteService sharedNoteService) {
        this.sharedNoteService = sharedNoteService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<SharedNoteWithDetailsDto>> getAllUserSharedNotes(@PathVariable String email) {
        List<SharedNoteWithDetailsDto> userSharedNotes = sharedNoteService.findAllUserSharedNote(email);
        List<SharedNoteWithDetailsDto> userSharedNotesImmutable = List.of(userSharedNotes.toArray(new SharedNoteWithDetailsDto[]{}));

        if (userSharedNotesImmutable.isEmpty()) {
            throw new EntityNotFoundException("Shared notes");
        }

        return ResponseEntity.ok(userSharedNotesImmutable);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<SharedNoteDto> getSharedNoteById(@PathVariable String id) {
        try {
            SharedNoteDto sharedNote = sharedNoteService.findSharedNote(id);
            return ResponseEntity.ok(sharedNote);

        } catch (Exception e) {
            throw new EntityNotFoundException("Shared note");
        }
    }


    @GetMapping("/{email}/{id}")
    public ResponseEntity<SharedNoteWithDetailsDto> getUserSharedNoteWithDetails(@PathVariable String email, @PathVariable String id) {
        try {
            SharedNoteWithDetailsDto sharedNoteWD = sharedNoteService.findUserSharedNoteWithDetails(email, id);
            return ResponseEntity.ok(sharedNoteWD);

        } catch (Exception e) {
            throw new EntityNotFoundException("Shared note");
        }
    }


    @PostMapping
    public ResponseEntity<?> shareNote(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        jsonValidator.validator(jsonObject);

        try {
            sharedNoteService.shareNote(jsonObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Fk_note_id")) {
                throw new ForeignKeyViolationException("Invalid foreign key for note_id");
            } else if (e.getMessage().contains("SharedNote_shared_with_user_email_fkey")){
                throw new ForeignKeyViolationException("Invalid foreign key for shared_with_user_email");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Data integrity violation"));
        }
    }

    @DeleteMapping("/{note_id}/{email}")
    public ResponseEntity<?> deleteSharedNote(@PathVariable String note_id, @PathVariable String email) {
        try {
            SharedNoteWithDetailsDto sharedNote = sharedNoteService.findUserSharedNoteWithDetails(email, note_id);
            sharedNoteService.deleteSharedNote(note_id, email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new EntityNotFoundException("Shared note");
        }
    }


}
