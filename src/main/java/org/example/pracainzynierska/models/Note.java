package org.example.pracainzynierska.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Note {

    String id;

    @NotEmpty(message = "This field can't be empty!")
    String title;

    @NotEmpty(message = "This field can't be empty!")
    @Size(min = 2, max = 11, message = "Tag's length must be between 3 and 10 characters")
    String tag;

    @NotEmpty(message = "This field can't be empty!")
    Boolean favourite;

    @NotEmpty(message = "This field can't be empty!")
    String content;

    String imageUrl;

    String fileUrl;

    String note_owner_id;

}
