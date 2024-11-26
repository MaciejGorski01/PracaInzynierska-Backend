package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.NoteService;
import org.json.JSONObject;
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
    JsonValidator jsonValidator = new JsonValidator("schemas/note_schema.json");

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<NoteDto>> getAllUserNotes(@PathVariable String userid){
        List<NoteDto> userNotes = noteService.findAllUserNotes(userid);
        List<NoteDto> userNotesImmutable = List.of(userNotes.toArray(new NoteDto[]{}));

        if (userNotesImmutable.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userNotesImmutable);
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(){
        List<NoteDto> allNotes = noteService.findAllNotes();
        List<NoteDto> allNotesImmutable = List.of(allNotes.toArray(new NoteDto[]{}));

        if (allNotesImmutable.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allNotesImmutable);
    }


    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody String json){
        try{
            JSONObject jsonObject = new JSONObject(json);

            jsonValidator.validator(jsonObject);

            noteService.addNote(jsonObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());
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
    public ResponseEntity<?> editNote(@PathVariable String id, @RequestBody String json){
        try{
            NoteDto existingNoteDto = noteService.findNoteById(id);
            JSONObject jsonObject = new JSONObject(json);

//            NoteDto updatedNoteDto = new NoteDto(
//                    id,
//                    jsonObject.has("title") ? jsonObject.getString("title") : existingNoteDto.title(),
//                    jsonObject.has("tag") ? jsonObject.getString("tag") : existingNoteDto.tag(),
//                    jsonObject.has("favourite") ? jsonObject.getBoolean("favourite") : existingNoteDto.favourite(),
//                    jsonObject.has("content") ? jsonObject.getString("content") : existingNoteDto.content(),
//                    jsonObject.has("color") ? jsonObject.getString("color") : existingNoteDto.color(),
//                    jsonObject.has("fileUrl") ? jsonObject.getString("fileUrl") : existingNoteDto.fileUrl()
//            );

            JSONObject updatedNote = new JSONObject();

            updatedNote.put("title", jsonObject.has("title") ? jsonObject.getString("title"): existingNoteDto.title());
            updatedNote.put("tag", jsonObject.has("tag") ? jsonObject.getString("tag"): existingNoteDto.tag());
            updatedNote.put("favourite", jsonObject.has("favourite") ? jsonObject.getBoolean("favourite"): existingNoteDto.favourite());
            updatedNote.put("content", jsonObject.has("content") ? jsonObject.getString("content"): existingNoteDto.content());
            updatedNote.put("color", jsonObject.has("color") ? jsonObject.getString("color"): existingNoteDto.color());
            updatedNote.put("fileUrl", jsonObject.has("fileUrl") ? jsonObject.getString("fileUrl"): existingNoteDto.fileUrl());

            jsonValidator.validator(updatedNote);

            noteService.updateNote(updatedNote, id);

            return ResponseEntity.ok(updatedNote.toMap());

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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Note not found"));
            }

            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }

    }



}
