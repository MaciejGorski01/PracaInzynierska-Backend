package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.mappers.NoteMapper;
import org.example.pracainzynierska.models.Note;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepository {

    private final JdbcTemplate jdbcTemplate;

    public NoteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Note> findAllUserNotes(String userid){
        String sql = "SELECT * from \"Note\" WHERE \"note_owner_id\" = ?";
        return jdbcTemplate.query(sql, new NoteMapper(), userid);
    }

    public List<Note> findAllNotes(){
        String sql = "SELECT * FROM \"Note\"";
        return jdbcTemplate.query(sql, new NoteMapper());
    }

    public Note findById(String id){
        String sql = "SELECT * FROM \"Note\" WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new NoteMapper(), id);
    }

    public void create(String id, String title, String tag, Boolean favourite, String content, String color, String fileUrl, String noteOwnerId) {
        String sql = "INSERT INTO \"Note\" (id, title, tag, favourite, content, \"color\", \"fileUrl\", \"note_owner_id\") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, id, title, tag, favourite, content, color, fileUrl, noteOwnerId);
    }

    public void delete(String id){
        String sql = "DELETE FROM \"Note\" WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(String title, String tag, Boolean favourite, String content, String color, String fileUrl, String id){
        String sql = "UPDATE \"Note\" SET title = ?, tag = ?, favourite = ?, content = ?, \"color\" = ?, \"fileUrl\" = ? WHERE id = ?";
        jdbcTemplate.update(sql, title, tag, favourite, content, color, fileUrl, id);
    }

}
