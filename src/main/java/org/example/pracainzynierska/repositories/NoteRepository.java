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

    public List<Note> findAllUserNotes(Long userid){
        String sql = "SELECT n from Note n WHERE n.note_owner_id = ?";
        return jdbcTemplate.query(sql, new NoteMapper(), userid);
    }

    public Note findById(Long id){
        String sql = "SELECT n from Note n WHERE n.id = ?";
        return jdbcTemplate.queryForObject(sql, new NoteMapper(), id);
    }

    public void create(String title, String tag, Boolean favourite, String content, String imageUrl, String fileUrl, Long note_owner_id){
        String sql = "INSERT INTO Note n (n.title, n.tag, n.favourite, n.content, n.imageUrl, n.fileUrl, n.note_owner_id) VALUES (?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, title, tag, favourite, content, imageUrl, fileUrl, note_owner_id);
    }

    public void delete(Long id){
        String sql = "DELETE FROM Note n WHERE n.id = :id";
        jdbcTemplate.update(sql, id);
    }

    public void update(String title, String tag, Boolean favourite, String content, String imageUrl, String fileUrl, Long id){
        String sql = "UPDATE Note n SET n.title = ? n.tag = ?, n.favourite = ?, n.content = ?, n.imageUrl = ?, n.fileUrl = ? WHERE n.id = ?";
        jdbcTemplate.update(sql, title, tag, favourite, content, imageUrl, fileUrl, id);
    }
}
