package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.functions.JsonValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SharedNoteWithDetailsMapper implements RowMapper<List<SharedNoteWithDetailsDto>> {


    JsonValidator jsonValidator = new JsonValidator("schemas/sharedNoteWithDetails_schema.json");


    public List<SharedNoteWithDetailsDto> mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("sharedNotesWithDetails_json");
        List<SharedNoteWithDetailsDto> sharedNotesWD = new ArrayList<>();

        if (json != null) {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonValidator.validator(jsonObject);

                String noteId = jsonObject.optString("note_id");
                String sharedWithUserEmail = jsonObject.optString("shared_with_user_email");

                String title = jsonObject.optString("title");
                String tag = jsonObject.optString("tag");
                Boolean favourite = jsonObject.optBoolean("favourite");
                String content = jsonObject.optString("content");
                String color = jsonObject.optString("color");
                String fileUrl = jsonObject.optString("fileUrl");

                SharedNoteWithDetailsDto sharedNoteWD = new SharedNoteWithDetailsDto(
                        noteId,
                        sharedWithUserEmail,
                        title,
                        tag,
                        favourite,
                        content,
                        color,
                        fileUrl
                );

                sharedNotesWD.add(sharedNoteWD);
            }
        }
        return sharedNotesWD;

    }
}