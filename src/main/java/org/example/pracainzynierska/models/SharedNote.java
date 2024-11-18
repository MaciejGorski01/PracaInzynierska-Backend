package org.example.pracainzynierska.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SharedNote {

    String id;

    @NotEmpty(message = "This field can't be empty!")
    String note_id;

    @NotEmpty(message = "This field can't be empty!")
    String shared_with_user_email;

}
