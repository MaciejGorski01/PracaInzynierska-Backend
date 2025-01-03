package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.mappers.NoteMapper;
import org.example.pracainzynierska.models.Note;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NoteRepository {

    private final JdbcTemplate jdbcTemplate;

    public NoteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Note> findAllUserNotes(String userid){
        String sql = "SELECT json_agg(row_to_json(\"Note\")) AS notes_json FROM \"Note\" WHERE \"note_owner_id\" = ?";
        return jdbcTemplate.queryForObject(sql, new NoteMapper(), userid);
    }

    public List<Note> findAllNotes(){
        String sql = "SELECT json_agg(row_to_json(\"Note\")) AS notes_json FROM \"Note\";";
        return jdbcTemplate.queryForObject(sql, new NoteMapper());
    }

    public List<Note> findById(String id){
        String sql = "SELECT json_agg(row_to_json(\"Note\")) AS notes_json FROM \"Note\" WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new NoteMapper(), id);
    }

//    public void create(String id, JSONObject jsonObject) {
//        String sql = "INSERT INTO \"Note\" (id, title, tag, favourite, content, color, \"fileUrl\", note_owner_id) " +
//                "SELECT ?, title, tag, favourite, content, color, \"fileUrl\", note_owner_id " +
//                "FROM json_to_record(?::json) AS temp(id text, title text, tag text, favourite boolean, content text, color text, \"fileUrl\" text, note_owner_id text)";
//        jdbcTemplate.update(sql, id, jsonObject.toString());
//    }

    public int create(String id, JSONObject jsonObject) {
        String sql = "SELECT insert_note_from_json(?::json, ?);";
        Optional<Integer> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Integer.class, jsonObject.toString(), id)
        );

        return result.orElse(0);
    }

    public void delete(String id){
        String sql = "DELETE FROM \"Note\" WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

//    public void update(JSONObject jsonObject, String id){
//        String sql = "UPDATE \"Note\" " +
//                "SET title = temp.title, tag = temp.tag, favourite = temp.favourite, content = temp.content, color = temp.color, \"fileUrl\" = temp.\"fileUrl\" \n " +
//                "FROM json_to_record(?::json) AS temp(title text, tag text, favourite boolean, content text, color text, \"fileUrl\" text) " +
//                "WHERE \"Note\".id = ?; ";
//        jdbcTemplate.update(sql, jsonObject.toString(), id);
//    }

    public int update(JSONObject jsonObject, String id) {
        String sql = "SELECT update_note_from_json(?::json, ?);";
        Optional<Integer> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Integer.class, jsonObject.toString(), id)
        );

        return result.orElse(0);
    }

    public void deleteFileUrl(String id){
        String sql = "UPDATE \"Note\" " +
                "SET \"fileUrl\" = NULL " +
                "WHERE \"Note\".id = ?;";
        jdbcTemplate.update(sql, id);
    }

}
