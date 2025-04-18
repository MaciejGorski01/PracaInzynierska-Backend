package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.SharedNoteDto;
import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.models.SharedNote;
import org.example.pracainzynierska.repositories.NoteRepository;
import org.example.pracainzynierska.repositories.SharedNoteRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
        SharedNote sharedNote = sharedNoteRepository.findSharedNote(id).getFirst();
        return mapToSharedNoteDto(sharedNote);
    }


    public List<SharedNoteWithDetailsDto> findAllUserSharedNote(String email){
        List<SharedNoteWithDetailsDto> sharedNotes = sharedNoteRepository.findAllUserSharedNote(email);

        return sharedNotes.stream().toList();
    }

    public SharedNoteWithDetailsDto findUserSharedNoteWithDetails(String email, String id){
        SharedNoteWithDetailsDto sharedNoteWD = sharedNoteRepository.findUserSharedNoteWithDetails(email, id).getFirst();
        return sharedNoteWD;
    }

    public void shareNote(JSONObject jsonObject){
        sharedNoteRepository.create(UUID.randomUUID().toString(), jsonObject);
    }

    public void deleteSharedNote(String id, String email){
        sharedNoteRepository.delete(id, email);
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
