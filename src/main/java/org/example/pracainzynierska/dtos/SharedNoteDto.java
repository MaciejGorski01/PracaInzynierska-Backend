package org.example.pracainzynierska.dtos;

import jakarta.validation.constraints.NotEmpty;

public record SharedNoteDto(

        String id,

        @NotEmpty(message = "This field can't be empty!")
        String note_id,

        @NotEmpty(message = "This field can't be empty!")
        String shared_with_user_email
){ }
