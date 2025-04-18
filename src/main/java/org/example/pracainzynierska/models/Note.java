package org.example.pracainzynierska.models;

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

    Boolean favourite;

    @NotEmpty(message = "This field can't be empty!")
    String content;

    @NotEmpty(message = "This field can't be empty!")
    String color;

    String fileUrl;

    String note_owner_id;

}
