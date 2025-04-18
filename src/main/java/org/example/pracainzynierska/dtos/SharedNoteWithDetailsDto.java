package org.example.pracainzynierska.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SharedNoteWithDetailsDto(

        @NotEmpty(message = "This field can't be empty!")
        String note_id,

        @NotEmpty(message = "This field can't be empty!")
        String shared_with_user_email,

        @NotEmpty(message = "This field can't be empty!")
        String title,

        @NotEmpty(message = "This field can't be empty!")
        @Size(min = 2, max = 11, message = "Tag's length must be between 3 and 10 characters")
        String tag,

        Boolean favourite,

        @NotEmpty(message = "This field can't be empty!")
        String content,

        String color,

        String fileUrl
){ }
