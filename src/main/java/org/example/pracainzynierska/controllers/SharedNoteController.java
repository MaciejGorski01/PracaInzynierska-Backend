package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.SharedNoteDto;
import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.SharedNoteService;
import org.json.JSONObject;
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
    JsonValidator jsonValidator = new JsonValidator("schemas/sharedNote_schema.json");

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


    @GetMapping("/id/{id}")
    public ResponseEntity<SharedNoteDto> getSharedNoteById(@PathVariable String id){
        try {
            SharedNoteDto sharedNote = sharedNoteService.findSharedNote(id);
            return ResponseEntity.ok(sharedNote);

        } catch (Exception e ){
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{email}/{id}")
    public ResponseEntity<SharedNoteWithDetailsDto> getUserSharedNoteWithDetails(@PathVariable String email, @PathVariable String id){
        try {
            SharedNoteWithDetailsDto sharedNoteWD = sharedNoteService.findUserSharedNoteWithDetails(email, id);
            return ResponseEntity.ok(sharedNoteWD);

        } catch (Exception e ){
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping
    public ResponseEntity<?> shareNote(@RequestBody String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            jsonValidator.validator(jsonObject);

            sharedNoteService.shareNote(jsonObject);

            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());
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
