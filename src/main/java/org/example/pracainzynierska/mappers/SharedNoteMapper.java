package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.models.SharedNote;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SharedNoteMapper implements RowMapper<SharedNote> {
    public SharedNote mapRow(ResultSet rs, int rowNum) throws SQLException {

        SharedNote sharedNote = new SharedNote();
        sharedNote.setId(rs.getString("id"));
        sharedNote.setNote_id(rs.getString("note_id"));
        sharedNote.setShared_with_user_email(rs.getString("shared_with_user_email"));

        return sharedNote;

    }
}