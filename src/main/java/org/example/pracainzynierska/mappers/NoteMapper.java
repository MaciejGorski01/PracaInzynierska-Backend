package org.example.pracainzynierska.mappers;

import org.aspectj.bridge.IMessage;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.models.Note;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteMapper implements RowMapper<List<Note>> {

    JsonValidator jsonValidator = new JsonValidator("schemas/note_schema.json");

    public List<Note> mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("notes_json");
        List<Note> notes = new ArrayList<>();


        if (json != null) {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonValidator.validator(jsonObject);

                Note note = new Note();
                note.setId(jsonObject.optString("id"));
                note.setTitle(jsonObject.optString("title"));
                note.setTag(jsonObject.optString("tag"));
                note.setFavourite(jsonObject.optBoolean("favourite"));
                note.setContent(jsonObject.optString("content"));
                note.setColor(jsonObject.optString("color"));
                note.setFileUrl(jsonObject.optString("fileUrl"));
                note.setNote_owner_id(jsonObject.optString("note_owner_id"));
                notes.add(note);
            }
        }

        return notes;

    }
}