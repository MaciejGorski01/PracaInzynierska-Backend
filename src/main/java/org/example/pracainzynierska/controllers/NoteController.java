package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.models.Note;
import org.example.pracainzynierska.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NoteController {

    NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<NoteDto>> getAllUserNotes(@PathVariable String userid){
        List<NoteDto> userNotes = noteService.findAllUserNotes(userid);

        if (userNotes.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userNotes);
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(){
        List<NoteDto> allNotes = noteService.findAllNotes();

        if (allNotes.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allNotes);
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
        try {

            NoteDto note = noteService.findNoteById(id);
            return ResponseEntity.ok(note);

        } catch (Exception e ){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editNote(@PathVariable String id, @RequestBody NoteDto noteDto){
        try{
            NoteDto existingNoteDto = noteService.findNoteById(id);

            NoteDto updatedNoteDto = new NoteDto(
                    id,
                    noteDto.title() != null ? noteDto.title() : existingNoteDto.title(),
                    noteDto.tag() != null ? noteDto.tag() : existingNoteDto.tag(),
                    noteDto.favourite() != null ? noteDto.favourite() : existingNoteDto.favourite(),
                    noteDto.content() != null ? noteDto.content() : existingNoteDto.content(),
                    noteDto.color() != null ? noteDto.color() : existingNoteDto.color(),
                    noteDto.fileUrl() != null ? noteDto.fileUrl() : existingNoteDto.fileUrl()
            );

            noteService.updateNote(updatedNoteDto);

            return ResponseEntity.ok(updatedNoteDto);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id){
        try{
            try{
                NoteDto note = noteService.findNoteById(id);

            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Note not found"));
            }

            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }

    }



}
