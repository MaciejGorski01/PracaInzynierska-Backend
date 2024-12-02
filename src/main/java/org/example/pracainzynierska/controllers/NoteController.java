package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.exceptions.EntityNotFoundException;
import org.example.pracainzynierska.exceptions.ForeignKeyViolationException;
import org.example.pracainzynierska.exceptions.ValidationException;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.NoteService;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> createNote(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        jsonValidator.validator(jsonObject);

        try {
            noteService.addNote(jsonObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Fk_user_id")){
                throw new ForeignKeyViolationException("Invalid foreign key for note_owner_id");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Data integrity violation"));
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
    public ResponseEntity<?> editNote(@PathVariable String id, @RequestBody String json) {
        NoteDto existingNoteDto;

        try {
            existingNoteDto = noteService.findNoteById(id);
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("Note");
        }

        JSONObject jsonObject = new JSONObject(json);

        JSONObject updatedNote = new JSONObject();

        updatedNote.put("title", jsonObject.has("title") ? jsonObject.getString("title") : existingNoteDto.title());
        updatedNote.put("tag", jsonObject.has("tag") ? jsonObject.getString("tag") : existingNoteDto.tag());
        updatedNote.put("favourite", jsonObject.has("favourite") ? jsonObject.getBoolean("favourite") : existingNoteDto.favourite());
        updatedNote.put("content", jsonObject.has("content") ? jsonObject.getString("content") : existingNoteDto.content());
        updatedNote.put("color", jsonObject.has("color") ? jsonObject.getString("color") : existingNoteDto.color());
        updatedNote.put("fileUrl", jsonObject.has("fileUrl") ? jsonObject.getString("fileUrl") : existingNoteDto.fileUrl());

        jsonValidator.validator(updatedNote);

        noteService.updateNote(updatedNote, id);

        return ResponseEntity.ok(updatedNote.toMap());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {
        try {
            NoteDto note = noteService.findNoteById(id);

            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new EntityNotFoundException("Note to delete");
        }

    }


}
