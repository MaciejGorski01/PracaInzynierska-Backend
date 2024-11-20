package org.example.pracainzynierska.mappers;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.example.pracainzynierska.models.Note;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteMapper implements RowMapper<List<Note>> {

    private final JsonSchema jsonSchema;
    public NoteMapper() {
        try {
            InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/note_schema.json");
            if (schemaStream == null) {
                throw new IllegalStateException("Brak schematu: schemas/note_schema.json");
            }

            String schemaString = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);

            JSONObject rawSchema = new JSONObject(schemaString);
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            this.jsonSchema = schemaFactory.getSchema(rawSchema.toString());

        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas ładowania JSON Schema", e);
        }
    }


    public List<Note> mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("notes_json");
        List<Note> notes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i=0; i< jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
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

        return notes;

    }
}