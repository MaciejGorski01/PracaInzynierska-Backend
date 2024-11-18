package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.SharedNoteDto;
import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.models.Note;
import org.example.pracainzynierska.models.SharedNote;
import org.example.pracainzynierska.repositories.SharedNoteRepository;
import org.springframework.stereotype.Service;
import org.example.pracainzynierska.repositories.NoteRepository;

import java.util.List;
import java.util.UUID;

@Service
public class SharedNoteService {

    SharedNoteRepository sharedNoteRepository;
    NoteRepository noteRepository;

    public SharedNoteService(SharedNoteRepository sharedNoteRepository, NoteRepository noteRepository) {
        this.sharedNoteRepository = sharedNoteRepository;
        this.noteRepository = noteRepository;
    }


    public SharedNoteDto findSharedNote(String id){
        SharedNote sharedNote = sharedNoteRepository.findSharedNote(id);
        return mapToSharedNoteDto(sharedNote);
    }


    public List<SharedNoteWithDetailsDto> findAllUserSharedNote(String email){
        List<SharedNoteWithDetailsDto> sharedNotes = sharedNoteRepository.findAllUserSharedNote(email);

        return sharedNotes.stream().toList();
    }

    public void shareNote(SharedNote sharedNote){
        sharedNoteRepository.create(UUID.randomUUID().toString(), sharedNote.getNote_id(), sharedNote.getShared_with_user_email());
    }

    public void deleteSharedNote(String id){
        sharedNoteRepository.delete(id);
    }


    public SharedNoteDto mapToSharedNoteDto(SharedNote sharedNote){

        SharedNoteDto sharedNoteDto = new SharedNoteDto(
                sharedNote.getId(),
                sharedNote.getNote_id(),
                sharedNote.getShared_with_user_email()
        );
        return sharedNoteDto;
    }

}
