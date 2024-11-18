package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.mappers.SharedNoteMapper;
import org.example.pracainzynierska.mappers.SharedNoteWithDetailsMapper;
import org.example.pracainzynierska.models.SharedNote;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SharedNoteRepository {

    private final JdbcTemplate jdbcTemplate;

    public SharedNoteRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public SharedNote findSharedNote(String id){
        String sql = "SELECT * from \"SharedNote\" WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, new SharedNoteMapper(), id);
    }

    public List<SharedNoteWithDetailsDto> findAllUserSharedNote(String email){
        String sql = "SELECT sn.note_id, sn.shared_with_user_email, n.title, n.tag, n.favourite, n.content, n.color, n.\"fileUrl\", n.note_owner_id from \"SharedNote\" AS sn " +
                "JOIN \"Note\" AS n ON sn.note_id = n.id " +
                "WHERE sn.shared_with_user_email LIKE ?";
        return jdbcTemplate.query(sql, new SharedNoteWithDetailsMapper(), email);
    }

    public void create(String id, String note_id, String email){
        String sql = "INSERT INTO \"SharedNote\" (id, note_id, shared_with_user_email) VALUES (?, ?, ?);";
        jdbcTemplate.update(sql, id, note_id, email);
    }

    public void delete(String id){
        String sql = "DELETE FROM \"SharedNote\" WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

}
