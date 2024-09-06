package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.models.Note;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteMapper implements RowMapper<Note> {
    public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();
        note.setId(rs.getString("id"));
        note.setTitle(rs.getString("title"));
        note.setTag(rs.getString("tag"));
        note.setFavourite(rs.getBoolean("favourite"));
        note.setContent(rs.getString("content"));
        note.setImageUrl(rs.getString("imageUrl"));
        note.setFileUrl(rs.getString("fileUrl"));
        note.setNote_owner_id(rs.getString("note_owner_id"));
        return note;
    }
}
