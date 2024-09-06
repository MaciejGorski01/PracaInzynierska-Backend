package org.example.pracainzynierska.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NoteDto(
        Long id,

        @NotEmpty(message = "This field can't be empty!")
        String title,

        @NotEmpty(message = "This field can't be empty!")
        @Size(min = 2, max = 11, message = "Tag's length must be between 3 and 10 characters")
        String tag,

        @NotEmpty(message = "This field can't be empty!")
        Boolean favourite,

        @NotEmpty(message = "This field can't be empty!")
        String content,

        String imageUrl,

        String fileUrl
) { }
