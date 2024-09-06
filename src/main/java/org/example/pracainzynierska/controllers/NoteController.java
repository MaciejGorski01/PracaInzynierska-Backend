package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.models.Note;
import org.example.pracainzynierska.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NoteController {

    NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Note note){
        try{
            noteService.addNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable String id){
        NoteDto note = noteService.findNoteById(id);
        if (note == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(note);
    }

}
