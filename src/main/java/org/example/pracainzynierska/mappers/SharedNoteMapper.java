package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.models.SharedNote;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SharedNoteMapper implements RowMapper<List<SharedNote>> {

    JsonValidator jsonValidator = new JsonValidator("schemas/sharedNote_schema.json");

    public List<SharedNote> mapRow(ResultSet rs, int rowNum) throws SQLException {


        String json = rs.getString("sharedNotes_json");
        List<SharedNote> sharedNotes = new ArrayList<>();

        if (json != null) {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonValidator.validator(jsonObject);

                SharedNote sharedNote = new SharedNote();
                sharedNote.setId(jsonObject.optString("id"));
                sharedNote.setNote_id(jsonObject.optString("note_id"));
                sharedNote.setShared_with_user_email(jsonObject.optString("shared_with_user_email"));
                sharedNotes.add(sharedNote);
            }
        }

        return sharedNotes;

    }
}