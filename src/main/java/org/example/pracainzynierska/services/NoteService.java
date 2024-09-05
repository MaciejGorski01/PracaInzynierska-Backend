package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.models.Note;
import org.example.pracainzynierska.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public List<NoteDto> findAllUserNotes(Long userid){
        List<Note> notes = noteRepository.findAllUserNotes(userid);
        return notes.stream().map(this::mapToNoteDto).toList();
    }

    public NoteDto findNoteById(Long id){
        Note note = noteRepository.findById(id);
        return mapToNoteDto(note);
    }

    public void addNote(Note note) {
        noteRepository.create(note.getTitle(), note.getTag(), note.getFavourite(), note.getContent(), note.getImageUrl(), note.getFileUrl(), note.getNote_owner_id());
    }

    public void deleteNote(Long id){
        noteRepository.delete(id);
    }

    public void updateNote(Note note){
        noteRepository.update(note.getTitle(), note.getTag(), note.getFavourite(), note.getContent(), note.getImageUrl(), note.getFileUrl(), note.getId());
    }

    public NoteDto mapToNoteDto(Note note){
        NoteDto noteDto = new NoteDto(note.getId(), note.getTitle(), note.getTag(), note.getFavourite(), note.getContent(), note.getImageUrl(), note.getFileUrl());
        return noteDto;
    }
}
