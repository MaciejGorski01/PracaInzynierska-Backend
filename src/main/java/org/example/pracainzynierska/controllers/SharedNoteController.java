package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.SharedNoteDto;
import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.models.SharedNote;
import org.example.pracainzynierska.services.SharedNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sharednotes")
public class SharedNoteController {
    SharedNoteService sharedNoteService;

    public SharedNoteController(SharedNoteService sharedNoteService) { this.sharedNoteService = sharedNoteService; }

    @GetMapping("/{email}")
    public ResponseEntity<List<SharedNoteWithDetailsDto>> getAllUserSharedNotes(@PathVariable String email){
        List<SharedNoteWithDetailsDto> userSharedNotes = sharedNoteService.findAllUserSharedNote(email);
        List<SharedNoteWithDetailsDto> userSharedNotesImmutable = List.of(userSharedNotes.toArray(new SharedNoteWithDetailsDto[]{}));

        if (userSharedNotesImmutable.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userSharedNotesImmutable);
    }

    @PostMapping
    public ResponseEntity<?> shareNote(@RequestBody SharedNote sharedNote){
        try{
            sharedNoteService.shareNote(sharedNote);
            return ResponseEntity.status(HttpStatus.CREATED).body(sharedNote);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSharedNote(@PathVariable String id){
        try{
            try{
                SharedNoteDto sharedNote = sharedNoteService.findSharedNote(id);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Shared note not found"));
            }
            sharedNoteService.deleteSharedNote(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


}
