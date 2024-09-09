package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.models.Note;
import org.example.pracainzynierska.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public List<NoteDto> findAllUserNotes(String userid){
        List<Note> notes = noteRepository.findAllUserNotes(userid);
        return notes.stream().map(this::mapToNoteDto).toList();
    }

    public List<NoteDto> findAllNotes(){
        List<Note> notes = noteRepository.findAllNotes();
        return notes.stream().map(this::mapToNoteDto).toList();
    }

    public NoteDto findNoteById(String id){
        Note note = noteRepository.findById(id);
        return mapToNoteDto(note);
    }

    public void addNote(Note note) {
        noteRepository.create(UUID.randomUUID().toString(), note.getTitle(), note.getTag(), note.getFavourite(), note.getContent(), note.getImageUrl(), note.getFileUrl(), note.getNote_owner_id());
    }

    public void deleteNote(String id){
        noteRepository.delete(id);
    }

    public void updateNote(NoteDto note){
        noteRepository.update(note.title(), note.tag(), note.favourite(), note.content(), note.imageUrl(), note.fileUrl(), note.id());
    }

    public NoteDto mapToNoteDto(Note note){
        NoteDto noteDto = new NoteDto(note.getId(), note.getTitle(), note.getTag(), note.getFavourite(), note.getContent(), note.getImageUrl(), note.getFileUrl());
        return noteDto;
    }
}
