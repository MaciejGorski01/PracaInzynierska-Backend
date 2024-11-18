package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SharedNoteWithDetailsMapper implements RowMapper<SharedNoteWithDetailsDto> {
    public SharedNoteWithDetailsDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        String noteId = rs.getString("note_id");
        String sharedWithUserEmail = rs.getString("shared_with_user_email");

        String title = rs.getString("title");
        String tag = rs.getString("tag");
        Boolean favourite = rs.getBoolean("favourite");
        String content = rs.getString("content");
        String color = rs.getString("color");
        String fileUrl = rs.getString("fileUrl");

        return new SharedNoteWithDetailsDto(
                noteId,
                sharedWithUserEmail,
                title,
                tag,
                favourite,
                content,
                color,
                fileUrl
        );

    }
}